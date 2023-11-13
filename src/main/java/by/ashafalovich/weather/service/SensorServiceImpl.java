package by.ashafalovich.weather.service;

import by.ashafalovich.weather.dto.Response;
import by.ashafalovich.weather.dto.SensorRequest;
import by.ashafalovich.weather.mapper.MeasurementMapper;
import by.ashafalovich.weather.mapper.SensorMapper;
import by.ashafalovich.weather.model.Measurement;
import by.ashafalovich.weather.model.Sensor;
import by.ashafalovich.weather.repository.MeasurementRepository;
import by.ashafalovich.weather.repository.SensorRepository;
import by.ashafalovich.weather.utils.ErrorUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService {

    @Value("${openWeather.baseUrl}")
    private String BASE_URL;
    @Value("${openWeather.APPID}")
    private String API_KEY;
    @Value("${openWeather.unitsMetric}")
    private String UNITS_METRIC;
    private static final String AMPERSAND = "&";

    private final RestTemplate restTemplate;
    private final SensorRepository sensorRepository;
    private final MeasurementRepository measurementRepository;
    private final SensorMapper sensorMapper;
    private final MeasurementMapper measurementMapper;

    private String getOpenWeatherUrl(String city) {
        return BASE_URL + city + AMPERSAND + API_KEY + AMPERSAND + UNITS_METRIC;
    }

    private boolean isWeatherDataAvailable(String city) {
        try {
            ResponseEntity<JsonNode> response = restTemplate
                    .getForEntity(getOpenWeatherUrl(city), JsonNode.class);
            if (response.hasBody() && response.getStatusCode().is2xxSuccessful()) {
                return true;
            } else {
                log.error("Error in HTTP request. Status code: {}, Response body: {}",
                        response.getStatusCode(), response.getBody());
                return false;
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            return false;
        }
    }

    private void executeMeasurement(Sensor sensor) {
        try {
            ResponseEntity<JsonNode> response = restTemplate
                    .getForEntity(getOpenWeatherUrl(sensor.getName()), JsonNode.class);
            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                JsonNode jsonResponse = response.getBody();
                Float temperature = jsonResponse.path("main").path("temp").floatValue();
                String weatherType = jsonResponse.path("weather").get(0).path("main").textValue();
                Measurement measurement = Measurement.builder()
                        .key(sensor.getKey())
                        .value(temperature)
                        .raining("Rain".equalsIgnoreCase(weatherType))
                        .measurementTime(LocalDateTime.now())
                        .build();
                measurementRepository.save(measurement);
            } else {
                log.error("Error in HTTP request. Status code: {}, Response body: {}",
                        response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
        }
    }

    @Override
    public Response initiateSensor(UUID key) {
        try {
            Optional<Sensor> sensorOptional = sensorRepository.findByKey(key);
            if (sensorOptional.isPresent()) {
                Sensor sensor = sensorOptional.get();
                if (!sensor.getIsActive()) {
                    sensor.setIsActive(true);
                    sensorRepository.save(sensor);
                    if (isWeatherDataAvailable(sensor.getName())) {
                        executeMeasurement(sensor);
                    } else {
                        return ErrorUtils.getResponseWithErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                                "Error while getting data from the server.");
                    }
                    Optional<Measurement> latestMeasurement = measurementRepository
                            .findFirstByKeyOrderByMeasurementTimeDesc(key);
                    if (latestMeasurement.isPresent()) {
                        return measurementMapper.toResponse(latestMeasurement.get());
                    } else {
                        return ErrorUtils.getResponseWithErrorMessage(HttpStatus.NOT_FOUND,
                                "Measurement not found.");
                    }
                } else {
                    return ErrorUtils.getResponseWithErrorMessage(HttpStatus.CONFLICT,
                            "Sensor is already active.");
                }
            } else {
                return ErrorUtils.getResponseWithErrorMessage(HttpStatus.BAD_REQUEST,
                        "Invalid or missing sensor key.");
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            return ErrorUtils.getResponseWithErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred.");
        }
    }

    @Scheduled(fixedDelayString = "#{ T(java.util.concurrent.ThreadLocalRandom).current().nextInt(12000) + 3000 }")
    @Transactional
    public void periodicMeasurements() {
        try {
            List<Sensor> activeSensors = sensorRepository.findByIsActiveTrue();
            for (Sensor sensor : activeSensors) {
                if (isWeatherDataAvailable(sensor.getName())) {
                    executeMeasurement(sensor);
                } else {
                    log.warn("Data not available for sensor {}. Skipping measurement.", sensor.getName());
                }
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred during periodic measurements: {}", e.getMessage());
        }
    }

    @Override
    public Response registration(SensorRequest sensorRequest) {
        try {
            if (null != sensorRequest.getName()
                    && sensorRequest.getName().length() >= 3
                    && sensorRequest.getName().length() <= 30) {
                Optional<Sensor> sensorOptional = sensorRepository.findByName(sensorRequest.getName());
                if (sensorOptional.isEmpty()) {
                    Sensor sensor = Sensor.builder()
                            .name(sensorRequest.getName())
                            .build();
                    Sensor savedSensor = sensorRepository.save(sensor);
                    return sensorMapper.toResponse(savedSensor);
                } else {
                    return ErrorUtils.getResponseWithErrorMessage(HttpStatus.CONFLICT,
                            "Sensor with this name already exists.");
                }
            } else {
                return ErrorUtils.getResponseWithErrorMessage(HttpStatus.BAD_REQUEST,
                        "Invalid or missing sensor name.");
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred during sensor registration: {}", e.getMessage());
            return ErrorUtils.getResponseWithErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred.");
        }
    }
}
package by.ashafalovich.weather.service;

import by.ashafalovich.weather.dto.*;
import by.ashafalovich.weather.mapper.MeasurementMapper;
import by.ashafalovich.weather.mapper.SensorMapper;
import by.ashafalovich.weather.model.Measurement;
import by.ashafalovich.weather.model.Sensor;
import by.ashafalovich.weather.repository.MeasurementRepository;
import by.ashafalovich.weather.repository.SensorRepository;
import by.ashafalovich.weather.utils.ErrorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ServerServiceImpl implements ServerService {

    private final SensorRepository sensorRepository;
    private final MeasurementRepository measurementRepository;
    private final SensorMapper sensorMapper;
    private final MeasurementMapper measurementMapper;

    @PostConstruct
    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void updateSensorStatus() {
        try {
            LocalDateTime time = LocalDateTime.now().minusMinutes(1);
            List<Measurement> measurements = measurementRepository
                    .findAllByMeasurementTimeAfterOrderByMeasurementTimeDesc(time);
            List<Sensor> sensors = sensorRepository.findAll();
            sensors.forEach(sensor -> {
                try {
                    boolean isActive = measurements.stream()
                            .anyMatch(measurement -> sensor.getKey().equals(measurement.getKey()));
                    sensor.setIsActive(isActive);
                    sensorRepository.save(sensor);
                } catch (Exception e) {
                    log.error("Error updating sensor: {}", e.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("Error retrieving data: {}", e.getMessage());
        }
    }

    @Override
    public Response getAllActiveSensors() {
        try {
            List<Sensor> sensors = sensorRepository.findByIsActiveTrue();
            if (!sensors.isEmpty()) {
                List<SensorDto> sensorDtoList = sensors.stream()
                        .map(sensorMapper::toDto)
                        .collect(Collectors.toList());
                return new SensorListResponse(sensorDtoList);
            } else {
                return ErrorUtils.getResponseWithErrorMessage(HttpStatus.NOT_FOUND,
                        "There are currently no active sensors, or none have been registered on the server.");
            }
        } catch (Exception e) {
            log.error("Error while retrieving active sensors: {}", e.getMessage());
            return ErrorUtils.getResponseWithErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while processing the request.");
        }
    }

    @Override
    public Response getLatestMeasurements(UUID key) {
        try {
            Optional<Sensor> sensorOptional = sensorRepository.findByKey(key);
            if (sensorOptional.isPresent()) {
                List<Measurement> measurements = measurementRepository.findTop20ByKeyOrderByMeasurementTimeDesc(key);
                if (!measurements.isEmpty()) {
                    List<MeasurementDto> measurementDtoList = measurements.stream()
                            .map(measurementMapper::toDto)
                            .collect(Collectors.toList());
                    return new MeasurementListResponse(measurementDtoList);
                } else {
                    return ErrorUtils.getResponseWithErrorMessage(HttpStatus.NOT_FOUND,
                            "No measurements available for the specified sensor.");
                }
            } else {
                return ErrorUtils.getResponseWithErrorMessage(HttpStatus.BAD_REQUEST,
                        "Invalid or missing sensor key.");
            }
        } catch (Exception e) {
            log.error("Error while retrieving latest measurements: {}", e.getMessage());
            return ErrorUtils.getResponseWithErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while processing the request.");
        }
    }

    @Override
    public Response getActualSensorMeasurements() {
        try {
            LocalDateTime time = LocalDateTime.now().minusMinutes(1);
            List<Measurement> measurements = measurementRepository
                    .findAllByMeasurementTimeAfterOrderByMeasurementTimeDesc(time);
            if (!measurements.isEmpty()) {
                Map<UUID, List<Measurement>> measurementsBySensorKey = measurements.stream()
                        .collect(Collectors.groupingBy(Measurement::getKey));
                List<SensorWithMeasurementDto> sensorWithMeasurementDtoList = measurementsBySensorKey.entrySet().stream()
                        .map(entry -> new SensorWithMeasurementDto(
                                sensorMapper.toDto(sensorRepository.findByKey(entry.getKey())
                                        .orElseThrow()),
                                entry.getValue().stream()
                                        .map(measurementMapper::toDto)
                                        .collect(Collectors.toList())))
                        .collect(Collectors.toList());
                return new SensorWithMeasurementListResponse(sensorWithMeasurementDtoList);
            } else {
                return ErrorUtils.getResponseWithErrorMessage(HttpStatus.NOT_FOUND,
                        "No actual measurements available.");
            }
        } catch (Exception e) {
            log.error("Error while retrieving actual measurements: {}", e.getMessage());
            return ErrorUtils.getResponseWithErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An error occurred while processing the request.");
        }
    }
}
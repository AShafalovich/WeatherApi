package by.ashafalovich.weather.controller;

import by.ashafalovich.weather.dto.MeasurementResponse;
import by.ashafalovich.weather.dto.SensorRequest;
import by.ashafalovich.weather.dto.SensorResponse;
import by.ashafalovich.weather.service.SensorService;
import by.ashafalovich.weather.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sensors")
@Api(value = "SensorController", description = "Operations related to sensors")
public class SensorController {

    private final SensorService sensorService;

    @PostMapping("/registration")
    @ApiOperation(value = "Register a new sensor",
            response = SensorResponse.class,
            notes = "Provide details to register a new sensor. Returns information about the registered sensor.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Registration successful.", response = SensorResponse.class),
            @ApiResponse(code = 400, message = "Invalid or missing sensor name."),
            @ApiResponse(code = 401, message = "Invalid credentials or authentication token."),
            @ApiResponse(code = 403, message = "Access is restricted."),
            @ApiResponse(code = 404, message = "The requested resource is not available."),
            @ApiResponse(code = 409, message = "Sensor with this name already exists."),
            @ApiResponse(code = 500, message = "An unexpected error occurred.")
    })
    public ResponseEntity<Object> registration(@RequestBody SensorRequest sensor) {
        return ResponseUtils.answer(sensorService.registration(sensor));
    }

    @PostMapping("/{key}/measurements")
    @ApiOperation(value = "Initiate sensor measurements",
            response = MeasurementResponse.class,
            notes = "Initiate measurements for a specific sensor. Returns information about the initiated measurements.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Initialization successful.", response = MeasurementResponse.class),
            @ApiResponse(code = 400, message = "Sensor is already active or Invalid or missing sensor key."),
            @ApiResponse(code = 401, message = "Invalid credentials or authentication token."),
            @ApiResponse(code = 403, message = "Access is restricted."),
            @ApiResponse(code = 404, message = "The requested resource is not available."),
            @ApiResponse(code = 405, message = "Measurement not found."),
            @ApiResponse(code = 409, message = "Sensor is already active."),
            @ApiResponse(code = 500, message = "Error while getting data from the server.")
    })
    public ResponseEntity<Object> initiateSensor(@PathVariable UUID key) {
        return ResponseUtils.answer(sensorService.initiateSensor(key));
    }
}

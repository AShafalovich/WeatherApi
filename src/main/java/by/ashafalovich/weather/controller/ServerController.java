package by.ashafalovich.weather.controller;

import by.ashafalovich.weather.dto.MeasurementListResponse;
import by.ashafalovich.weather.dto.SensorListResponse;
import by.ashafalovich.weather.dto.SensorWithMeasurementListResponse;
import by.ashafalovich.weather.service.ServerService;
import by.ashafalovich.weather.utils.ResponseUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sensors")
@Api(value = "ServerController", description = "Operations related to servers")
public class ServerController {

    private final ServerService serverService;

    @GetMapping
    @ApiOperation(value = "Get all active sensors",
            response = SensorListResponse.class,
            notes = "Retrieve information about all active sensors")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Information received successfully.", response = SensorListResponse.class),
            @ApiResponse(code = 401, message = "Invalid credentials or authentication token."),
            @ApiResponse(code = 403, message = "Access is restricted."),
            @ApiResponse(code = 404, message = "There are currently no active sensors, or none have been registered on the server."),
            @ApiResponse(code = 500, message = "An error occurred while processing the request.")
    })
    public ResponseEntity<Object> getAllActiveSensors() {
        return ResponseUtils.answer(serverService.getAllActiveSensors());
    }

    @GetMapping("/measurements")
    @ApiOperation(value = "Get actual sensor measurements",
            response = SensorWithMeasurementListResponse.class,
            notes = "Retrieve the actual measurements for all sensors")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Measurements received successfully", response = SensorWithMeasurementListResponse.class),
            @ApiResponse(code = 401, message = "Invalid credentials or authentication token."),
            @ApiResponse(code = 403, message = "Access is restricted."),
            @ApiResponse(code = 404, message = "No actual measurements available."),
            @ApiResponse(code = 500, message = "An error occurred while processing the request.")
    })
    public ResponseEntity<Object> getActualSensorMeasurements() {
        return ResponseUtils.answer(serverService.getActualSensorMeasurements());
    }

    @GetMapping("/{key}/measurements")
    @ApiOperation(value = "Get latest measurements",
            response = MeasurementListResponse.class,
            notes = "Retrieve the latest measurements for a specific sensor")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Measurements received successfully", response = MeasurementListResponse.class),
            @ApiResponse(code = 400, message = "Invalid or missing sensor key."),
            @ApiResponse(code = 401, message = "Invalid credentials or authentication token."),
            @ApiResponse(code = 403, message = "Access is restricted."),
            @ApiResponse(code = 404, message = "No measurements available for the specified sensor."),
            @ApiResponse(code = 500, message = "An error occurred while processing the request.")
    })
    public ResponseEntity<Object> getLatestMeasurements(@PathVariable UUID key) {
        return ResponseUtils.answer(serverService.getLatestMeasurements(key));
    }
}
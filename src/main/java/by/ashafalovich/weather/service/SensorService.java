package by.ashafalovich.weather.service;

import by.ashafalovich.weather.dto.Response;
import by.ashafalovich.weather.dto.SensorRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.UUID;


@Api(tags = "SensorService", description = "Operations related to sensors")
public interface SensorService {

    @ApiOperation(value = "Register a new sensor", notes = "Provide details to register a new sensor")
    Response registration(
            @ApiParam(value = "Sensor details for registration", required = true)
                    SensorRequest sensor);

    @ApiOperation(value = "Initiate sensor measurements", notes = "Initiate measurements for a specific sensor")
    Response initiateSensor(
            @ApiParam(value = "Key of the sensor to initiate measurements", required = true)
                    UUID key);
}
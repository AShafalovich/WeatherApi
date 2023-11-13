package by.ashafalovich.weather.service;

import by.ashafalovich.weather.dto.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.UUID;

@Api(tags = "ServerService", description = "Operations related to server and sensors")
public interface ServerService {

    @ApiOperation(value = "Get all active sensors", notes = "Retrieve information about all active sensors")
    Response getAllActiveSensors();

    @ApiOperation(value = "Get latest measurements", notes = "Retrieve the latest measurements for a specific sensor")
    Response getLatestMeasurements(
            @ApiParam(value = "Key of the sensor to retrieve latest measurements", required = true)
                    UUID key);

    @ApiOperation(value = "Get actual sensor measurements", notes = "Retrieve the actual measurements for all active sensors")
    Response getActualSensorMeasurements();
}
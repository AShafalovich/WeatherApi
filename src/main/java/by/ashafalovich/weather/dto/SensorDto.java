package by.ashafalovich.weather.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "DTO representing a sensor")
public class SensorDto {

    @ApiModelProperty(notes = "Unique identifier for the sensor")
    private UUID key;

    @ApiModelProperty(notes = "Name of the sensor")
    private String name;

    @ApiModelProperty(notes = "Flag indicating whether the sensor is active")
    private Boolean isActive;
}
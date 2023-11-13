package by.ashafalovich.weather.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Request model for creating a sensor")
public class SensorRequest {

    @ApiModelProperty(notes = "Name of the sensor")
    private String name;
}
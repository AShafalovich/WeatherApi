package by.ashafalovich.weather.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "DTO representing a sensor with measurements")
public class SensorWithMeasurementDto {

    @ApiModelProperty(notes = "Sensor information")
    private SensorDto sensor;

    @ApiModelProperty(notes = "List of measurements")
    private List<MeasurementDto> measurements;
}
package by.ashafalovich.weather.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "DTO representing a measurement")
public class MeasurementDto {

    @ApiModelProperty(notes = "Value of the measurement")
    private Float value;
    @ApiModelProperty(notes = "Flag indicating whether it is raining during the measurement")
    private Boolean raining;
    @ApiModelProperty(notes = "Time of the measurement")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime measurementTime;
}
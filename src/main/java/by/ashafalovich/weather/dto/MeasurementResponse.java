package by.ashafalovich.weather.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Response representing a measurement")
public class MeasurementResponse extends Response {

    @ApiModelProperty(notes = "Value of the measurement")
    private Float value;

    @ApiModelProperty(notes = "Flag indicating whether it is raining during the measurement")
    private Boolean raining;
}

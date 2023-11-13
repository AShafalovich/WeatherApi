package by.ashafalovich.weather.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Response containing a list of latest measurements")
public class MeasurementListResponse extends Response {

    @ApiModelProperty(notes = "List of latest measurements")
    private List<MeasurementDto> latestMeasurements;
}
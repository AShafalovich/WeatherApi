package by.ashafalovich.weather.mapper;

import by.ashafalovich.weather.dto.MeasurementDto;
import by.ashafalovich.weather.dto.MeasurementResponse;
import by.ashafalovich.weather.model.Measurement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MeasurementMapper {

    @Mapping(target = "code", ignore = true)
    @Mapping(target = "message", ignore = true)
    MeasurementResponse toResponse(Measurement measurement);

    MeasurementDto toDto(Measurement measurement);
}
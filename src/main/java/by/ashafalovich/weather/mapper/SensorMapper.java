package by.ashafalovich.weather.mapper;

import by.ashafalovich.weather.dto.SensorDto;
import by.ashafalovich.weather.dto.SensorResponse;
import by.ashafalovich.weather.model.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SensorMapper {

    @Mapping(target = "code", ignore = true)
    @Mapping(target = "message", ignore = true)
    SensorResponse toResponse(Sensor sensor);

    @Mapping(target = "key", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    SensorDto toDto(Sensor sensor);
}
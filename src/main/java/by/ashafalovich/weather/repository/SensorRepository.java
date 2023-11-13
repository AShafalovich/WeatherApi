package by.ashafalovich.weather.repository;

import by.ashafalovich.weather.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    Optional<Sensor> findByName(String name);

    List<Sensor> findByIsActiveTrue();

    Optional<Sensor> findByKey(UUID key);
}
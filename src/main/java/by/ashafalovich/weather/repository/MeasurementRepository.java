package by.ashafalovich.weather.repository;

import by.ashafalovich.weather.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    Optional<Measurement> findFirstByKeyOrderByMeasurementTimeDesc(UUID key);

    List<Measurement> findTop20ByKeyOrderByMeasurementTimeDesc(UUID key);

    List<Measurement> findAllByMeasurementTimeAfterOrderByMeasurementTimeDesc(LocalDateTime time);
}
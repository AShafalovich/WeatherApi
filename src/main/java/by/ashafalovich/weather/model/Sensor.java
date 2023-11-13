package by.ashafalovich.weather.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "sensors")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "key", columnDefinition = "BINARY(16)")
    private UUID key;

    @Column(name = "sensor_name")
    private String name;

    @Builder.Default
    @Column(name = "active")
    private Boolean isActive = false;
}
CREATE TABLE sensors
(
    key         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sensor_name VARCHAR(30) NOT NULL,
    active      BOOLEAN     NOT NULL,
    CONSTRAINT unique_sensor_name UNIQUE (sensor_name),
    CONSTRAINT unique_sensor_key UNIQUE (key)
);

CREATE TABLE measurements
(
    measurement_id   SERIAL PRIMARY KEY,
    key              UUID REFERENCES sensors (key),
    value            DECIMAL NOT NULL,
    raining          BOOLEAN NOT NULL,
    measurement_time TIMESTAMP DEFAULT now(),
    CONSTRAINT fk_sensor FOREIGN KEY (key) REFERENCES sensors (key)
)
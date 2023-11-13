# Weather Monitoring System

A Spring Boot application for weather monitoring, allowing users to register sensors and receive real-time weather measurements.

## Table of Contents

- [Description](#description)
- [Requirements](#requirements)
- [Error processing](#Error-processing)
- [API Documentation](#api-documentation)
- [Build](#build)
- [Error processing](#Error-processing)


## Description

The Weather Monitoring System is designed to track weather data from various sensors. Users can register sensors, initiate measurements, and retrieve the latest weather information.

## Requirements

- Programming language: Java 17
- Framework: Spring Boot
- Build: Gradle
- Database: PostgresSQL
- Database migration: Flyway
- API documentation: Swagger
- Others: OpenWeather API

## API Documentation

http://localhost:8080/swagger-ui/index.htm

## Build

0. Create a Postgres Database named "weather".

1. Open a terminal or command line.
   
2. Go to the directory where you want to host the repository:
   
```bash
cd /path/to/your/directory
```

3. Clone the repository:

```bash
git clone https://github.com/AShafalovich/WeatherApi.git
```

4. Build the project using Gradle:
   
```bash
gradlew build
```

5. Launch the application:   

```bash
java -jar build/libs/Weather-1.0-SNAPSHOT.jar
```

## Functionality

1. Sensor registration:
   
POST http://localhost:8080/sensors/registration

Request example:
```json
{
"name": "Sensor name"
}
```

Response example:
```json
{
“key”: ”8bcb5ffa-ff4d-4214-a727-bb01ab90ceaa”
}
```

2. Sensor initialization:
   
POST http://localhost:8080/sensors/{key}/measurements

Response example:
```json
{
“value”: 24.5,
“raining”: false
}
```

3. Request to receive all active sensors:
   
GET http://localhost:8080/sensors

Response example:
```json
{
    "activeSensors": [
        {
            "name": "Kiev"
        },
        {
            "name": "Warsaw"
        }
    ]
}
```

4. Request to receive information about the last 20 sensor measurements:
   
GET http://localhost:8080/sensors/{key}/measurements

Response example:
```json
{
    "latestMeasurements": [
        {
            "value": 4.86,
            "raining": false,
            "measurementTime": "2023-11-13 13:44:29"
        },
        {
            "value": 4.86,
            "raining": false,
            "measurementTime": "2023-11-13 13:44:17"
        }
    ]
}
```

5. Request for up-to-date information from all sensors. Measurements whose time does not differ from request time for more than one minute.

GET http://localhost:8080/sensors/measurements

Response example:
```json
{
    "actualSensorMeasurements": [
        {
            "sensor": {
                "name": "Minsk"
            },
            "measurements": [
                {
                    "value": 7.71,
                    "raining": false,
                    "measurementTime": "2023-11-13 16:03:40"
                }
            ]
        },
        {
            "sensor": {
                "name": "Moscow"
            },
            "measurements": [
                {
                    "value": 8.79,
                    "raining": false,
                    "measurementTime": "2023-11-13 16:03:40"
                },
                {
                    "value": 8.79,
                    "raining": false,
                    "measurementTime": "2023-11-13 16:03:32"
                }
            ]
        },
        {
            "sensor": {
                "name": "Kemer"
            },
            "measurements": [
                {
                    "value": 21.02,
                    "raining": true,
                    "measurementTime": "2023-11-13 16:03:40"
                },
                {
                    "value": 21.02,
                    "raining": true,
                    "measurementTime": "2023-11-13 16:03:32"
                }
            ]
        }
    ]
}
```
## Error processing

The API processes and returns error messages when there is an invalid request or internal server problems.

Some examples of error responses:
```json
{
    "code": 400,
    "message": "Invalid or missing sensor name."
}
```
```json
{
    "code": 404,
    "message": "There are currently no active sensors, or none have been registered on the server."
}
```
```json
{
    "code": 409,
    "message": "Sensor with this name already exists."
}
```

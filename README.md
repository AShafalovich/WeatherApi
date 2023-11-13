# Weather Monitoring System

A Spring Boot application for weather monitoring, allowing users to register sensors and receive real-time weather measurements.

## Table of Contents

- [Description](#description)
- [Requirements](#requirements)
- [API Documentation](#api-documentation)
- [Functionality](#functionality)
- [Error processing](#Error-processing)

## Description

The Weather Monitoring System is designed to track weather data from various sensors. Users can register sensors, initiate measurements, and retrieve the latest weather information.

## Requirements

- Programming language: Java 17
- Framework: Spring Boot
- Database: PostgresSBL.
- API documentation: Swagger.

## API Documentation

{server.ip}/swagger-ui/index.htm

## Functionality

1. Sensor registration:
   
POST {server.ip}/sensors/registration

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
   
POST {server.ip}/sensors/{key}/measurements

Response example:
```json
{
“value”: 24.5,
“raining”: false
}
```

3. Request to receive all active sensors:
   
GET {server.ip}/sensors

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
   
GET {server.ip}/sensors/{key}/measurements

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

GET {server.ip}/sensors/measurements

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

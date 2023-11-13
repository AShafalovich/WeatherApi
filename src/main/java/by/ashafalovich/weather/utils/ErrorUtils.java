package by.ashafalovich.weather.utils;

import by.ashafalovich.weather.dto.Response;
import org.springframework.http.HttpStatus;

public class ErrorUtils {

    public static Response getResponseWithErrorMessage(HttpStatus status, String customMessage) {
        return Response.builder()
                .code(status.value())
                .message(customMessage != null ? customMessage : status.getReasonPhrase())
                .build();
    }
}
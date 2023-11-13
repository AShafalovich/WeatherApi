package by.ashafalovich.weather.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    @ApiOperation(value = "Get a configured RestTemplate",
            notes = "This method provides a configured RestTemplate for making HTTP requests.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved a RestTemplate"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        restTemplate.getMessageConverters().add(converter);
        return restTemplate;
    }
}
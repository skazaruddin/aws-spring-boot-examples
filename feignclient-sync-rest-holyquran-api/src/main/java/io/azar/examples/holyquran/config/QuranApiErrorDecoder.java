package io.azar.examples.holyquran.config;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.azar.examples.holyquran.dto.ApiError;
import io.azar.examples.holyquran.exceptions.BusinessException;
import io.azar.examples.holyquran.exceptions.TechnicalException;

import java.io.IOException;

public class QuranApiErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    public QuranApiErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() <= 499) {
            try {
                ApiError apiError = objectMapper.readValue(response.body().asInputStream(), ApiError.class);
                return new BusinessException(apiError);
            } catch (IOException e) {
                // Error parsing JSON response, return a generic exception
                return new Exception("Exception while parsing error response");
            }
        } else if (response.status() >= 500 && response.status() <= 599) {
            try {
                ApiError apiError = objectMapper.readValue(response.body().asInputStream(), ApiError.class);
                return new TechnicalException(apiError);
            } catch (IOException e) {
                // Error parsing JSON response, return a generic exception
                return new Exception("Exception while parsing error response");
            }
        } else {
            return new Exception("Exception while getting surah");
        }
    }
}

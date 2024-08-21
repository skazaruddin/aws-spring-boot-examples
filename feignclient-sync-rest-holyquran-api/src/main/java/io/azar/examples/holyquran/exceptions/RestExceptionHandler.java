package io.azar.examples.holyquran.exceptions;

import io.azar.examples.holyquran.dto.ApiError;
import io.azar.examples.holyquran.exceptions.BusinessException;
import io.azar.examples.holyquran.exceptions.TechnicalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
class RestExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex) {
        return ResponseEntity.badRequest().body(ex.getApiError());
    }

    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<ApiError> handleTechnicalException(TechnicalException ex) {
        return ResponseEntity.internalServerError().body(ex.getApiError());
    }

}

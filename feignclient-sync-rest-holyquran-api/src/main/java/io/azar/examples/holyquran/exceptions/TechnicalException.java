package io.azar.examples.holyquran.exceptions;

import io.azar.examples.holyquran.dto.ApiError;

import org.springframework.core.NestedRuntimeException;

public class TechnicalException extends NestedRuntimeException {
    private final ApiError apiError;

    public TechnicalException(ApiError apiError) {
        super("Technical Exception");
        this.apiError = apiError;
    }

    public TechnicalException(String msg, ApiError apiError) {
        super(msg);
        this.apiError = apiError;
    }

    public TechnicalException(String msg, Throwable cause, ApiError apiError) {
        super(msg, cause);
        this.apiError = apiError;
    }

    public ApiError getApiError() {
        return apiError;
    }
}

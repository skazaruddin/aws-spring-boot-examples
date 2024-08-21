package io.azar.examples.holyquran.exceptions;

import io.azar.examples.holyquran.dto.ApiError;
import org.springframework.core.NestedRuntimeException;

public class BusinessException extends NestedRuntimeException {
    private final ApiError apiError;

    public BusinessException(ApiError apiError) {
        super("Business Exception");
        this.apiError = apiError;
    }
    public BusinessException(String msg, ApiError apiError) {
        super(msg);
        this.apiError = apiError;
    }

    public BusinessException(String msg, Throwable cause, ApiError apiError) {
        super(msg, cause);
        this.apiError = apiError;
    }

    public ApiError getApiError() {
        return apiError;
    }
}

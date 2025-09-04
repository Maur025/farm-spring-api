package com.kernotec.farm.parametric.exception;

import com.kernotec.core.exception.custom.base.ApiException;

public class RequestStateException extends ApiException {

    public RequestStateException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public RequestStateException(String key, String messageParam) {
        super(formatMessage(key), messageParam);
    }

    public RequestStateException(String key, String messageParam, Integer code) {
        super(formatMessage(key), messageParam, code);
    }

    public RequestStateException(String key, String messageParam, Integer code, String fieldName) {
        super(formatMessage(key), messageParam, code, fieldName);
    }

    public RequestStateException(String key, String messageParam, Integer code, String fieldName,
        String additionalMessage)
    {
        super(formatMessage(key), messageParam, code, fieldName, additionalMessage);
    }

    private static String formatMessage(String key) {
        return String.format("exception.request.state.%s.message", key);
    }
}

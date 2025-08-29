package com.kernotec.farm.exception;

import com.kernotec.core.exception.custom.base.ApiException;

public class OperatorException extends ApiException {

    public OperatorException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public OperatorException(String key, String messageParam) {
        super(formatMessage(key), messageParam);
    }

    public OperatorException(String key, String messageParam, Integer code) {
        super(formatMessage(key), messageParam, code);
    }

    public OperatorException(String key, String messageParam, Integer code, String fieldName) {
        super(formatMessage(key), messageParam, code, fieldName);
    }

    public OperatorException(String key, String messageParam, Integer code, String fieldName,
        String additionalMessage)
    {
        super(formatMessage(key), messageParam, code, fieldName, additionalMessage);
    }

    private static String formatMessage(String key) {
        return String.format("exception.operator.%s.message", key);
    }
}

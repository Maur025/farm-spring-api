package com.kernotec.farm.parametric.exception;

import com.kernotec.core.exception.custom.base.ApiException;

public class ActivityTypeException extends ApiException {

    public ActivityTypeException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public ActivityTypeException(String key, String messageParam) {
        super(formatMessage(key), messageParam);
    }

    public ActivityTypeException(String key, String messageParam, Integer code) {
        super(formatMessage(key), messageParam, code);
    }

    public ActivityTypeException(String key, String messageParam, Integer code, String fieldName) {
        super(formatMessage(key), messageParam, code, fieldName);
    }

    public ActivityTypeException(String key, String messageParam, Integer code, String fieldName,
        String additionalMessage)
    {
        super(formatMessage(key), messageParam, code, fieldName, additionalMessage);
    }

    private static String formatMessage(String key) {
        return String.format("exception.activity.type.%s.message", key);
    }
}

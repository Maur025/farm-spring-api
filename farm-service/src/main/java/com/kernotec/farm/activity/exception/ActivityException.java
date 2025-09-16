package com.kernotec.farm.activity.exception;

import com.kernotec.core.exception.custom.base.ApiException;

public class ActivityException extends ApiException {

    public ActivityException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public ActivityException(String key, String messageParam) {
        super(formatMessage(key), messageParam);
    }

    public ActivityException(String key, String messageParam, Integer code) {
        super(formatMessage(key), messageParam, code);
    }

    public ActivityException(String key, String messageParam, Integer code, String fieldName) {
        super(formatMessage(key), messageParam, code, fieldName);
    }

    public ActivityException(String key, String messageParam, Integer code, String fieldName,
        String additionalMessage)
    {
        super(formatMessage(key), messageParam, code, fieldName, additionalMessage);
    }

    private static String formatMessage(String key) {
        return String.format("exception.activity.%s.message", key);
    }
}

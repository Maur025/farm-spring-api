package com.kernotec.farm.parametric.exception;

import com.kernotec.core.exception.custom.base.ApiException;

public class GroupStateException extends ApiException {

    public GroupStateException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public GroupStateException(String key, String messageParam) {
        super(formatMessage(key), messageParam);
    }

    public GroupStateException(String key, String messageParam, Integer code) {
        super(formatMessage(key), messageParam, code);
    }

    public GroupStateException(String key, String messageParam, Integer code, String fieldName) {
        super(formatMessage(key), messageParam, code, fieldName);
    }

    public GroupStateException(String key, String messageParam, Integer code, String fieldName,
        String additionalMessage)
    {
        super(formatMessage(key), messageParam, code, fieldName, additionalMessage);
    }

    private static String formatMessage(String key) {
        return String.format("exception.group.state.%s.message", key);
    }
}

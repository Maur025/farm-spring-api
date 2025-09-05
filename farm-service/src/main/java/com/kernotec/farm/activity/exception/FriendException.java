package com.kernotec.farm.activity.exception;

import com.kernotec.core.exception.custom.base.ApiException;

public class FriendException extends ApiException {

    public FriendException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public FriendException(String key, String messageParam) {
        super(formatMessage(key), messageParam);
    }

    public FriendException(String key, String messageParam, Integer code) {
        super(formatMessage(key), messageParam, code);
    }

    public FriendException(String key, String messageParam, Integer code, String fieldName) {
        super(formatMessage(key), messageParam, code, fieldName);
    }

    public FriendException(String key, String messageParam, Integer code, String fieldName,
        String additionalMessage)
    {
        super(formatMessage(key), messageParam, code, fieldName, additionalMessage);
    }

    private static String formatMessage(String key) {
        return String.format("exception.friend.%s.message", key);
    }
}

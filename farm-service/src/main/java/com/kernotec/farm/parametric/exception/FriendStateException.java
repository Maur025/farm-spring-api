package com.kernotec.farm.parametric.exception;

import com.kernotec.core.exception.custom.base.ApiException;

public class FriendStateException extends ApiException {

    public FriendStateException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public FriendStateException(String key, String messageParam) {
        super(formatMessage(key), messageParam);
    }

    public FriendStateException(String key, String messageParam, Integer code) {
        super(formatMessage(key), messageParam, code);
    }

    public FriendStateException(String key, String messageParam, Integer code, String fieldName) {
        super(formatMessage(key), messageParam, code, fieldName);
    }

    public FriendStateException(String key, String messageParam, Integer code, String fieldName,
        String additionalMessage)
    {
        super(formatMessage(key), messageParam, code, fieldName, additionalMessage);
    }

    private static String formatMessage(String key) {
        return String.format("exception.friend.state.%s.message", key);
    }
}

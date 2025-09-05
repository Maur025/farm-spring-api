package com.kernotec.farm.activity.exception;

import com.kernotec.core.exception.custom.base.ApiException;

public class AccountGroupException extends ApiException {

    public AccountGroupException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public AccountGroupException(String key, String messageParam) {
        super(formatMessage(key), messageParam);
    }

    public AccountGroupException(String key, String messageParam, Integer code) {
        super(formatMessage(key), messageParam, code);
    }

    public AccountGroupException(String key, String messageParam, Integer code, String fieldName) {
        super(formatMessage(key), messageParam, code, fieldName);
    }

    public AccountGroupException(String key, String messageParam, Integer code, String fieldName,
        String additionalMessage)
    {
        super(formatMessage(key), messageParam, code, fieldName, additionalMessage);
    }

    private static String formatMessage(String key) {
        return String.format("exception.account.group.%s.message", key);
    }
}

package com.kernotec.farm.account.exception;

import com.kernotec.core.exception.custom.base.ApiException;

public class AccountFollowProfileException extends ApiException {

    public AccountFollowProfileException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public AccountFollowProfileException(String key, String messageParam) {
        super(formatMessage(key), messageParam);
    }

    public AccountFollowProfileException(String key, String messageParam, Integer code) {
        super(formatMessage(key), messageParam, code);
    }

    public AccountFollowProfileException(String key, String messageParam, Integer code,
        String fieldName)
    {
        super(formatMessage(key), messageParam, code, fieldName);
    }

    public AccountFollowProfileException(String key, String messageParam, Integer code,
        String fieldName, String additionalMessage)
    {
        super(formatMessage(key), messageParam, code, fieldName, additionalMessage);
    }

    private static String formatMessage(String key) {
        return String.format("exception.account.follow.profile.%s.message", key);
    }
}

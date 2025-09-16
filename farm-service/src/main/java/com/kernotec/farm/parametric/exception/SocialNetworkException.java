package com.kernotec.farm.parametric.exception;

import com.kernotec.core.exception.custom.base.ApiException;

public class SocialNetworkException extends ApiException {

    public SocialNetworkException(String messageParam) {
        super("exception.default.message", messageParam);
    }

    public SocialNetworkException(String key, String messageParam) {
        super(formatMessage(key), messageParam);
    }

    public SocialNetworkException(String key, String messageParam, Integer code) {
        super(formatMessage(key), messageParam, code);
    }

    public SocialNetworkException(String key, String messageParam, Integer code, String fieldName) {
        super(formatMessage(key), messageParam, code, fieldName);
    }

    public SocialNetworkException(String key, String messageParam, Integer code, String fieldName,
        String additionalMessage)
    {
        super(formatMessage(key), messageParam, code, fieldName, additionalMessage);
    }

    private static String formatMessage(String key) {
        return String.format("exception.social.network.%s.message", key);
    }
}

package com.kernotec.farmauth.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.farmauth.jpa.enums.TokenTypeEnum;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class OpenIdConnectTokenResponse {

    private String accessToken;
    private String refreshToken;
    private TokenTypeEnum tokenType;
    private Long expiresIn;
    private Long refreshExpiresIn;
    private String scope;
}

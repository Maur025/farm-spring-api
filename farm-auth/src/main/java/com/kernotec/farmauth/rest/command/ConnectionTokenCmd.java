package com.kernotec.farmauth.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farmauth.jpa.enums.GrantTypeEnum;
import com.kernotec.farmauth.rest.dto.request.OpenIdConnectTokenRequest;
import com.kernotec.farmauth.rest.dto.response.OpenIdConnectTokenResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ConnectionTokenCmd extends
    AbstractTransactionalRequiredCommand<ConnectionTokenCmd.Request, OpenIdConnectTokenResponse>
{

    private final AuthLoginWithPasswordCmd authLoginWithPasswordCmd;

    @Override
    protected OpenIdConnectTokenResponse run(Request request) {

        return switch (request.getGrantType()) {
            case password -> authLoginWithPasswordCmd.withRequest(
                    AuthLoginWithPasswordCmd.Request.builder()
                        .authRequest(request.getTokenRequest())
                        .build())
                .execute();
            case refresh_token -> null;
        };
    }

    @Builder
    @Getter
    public static class Request {

        private final GrantTypeEnum grantType;
        private final OpenIdConnectTokenRequest tokenRequest;
    }
}

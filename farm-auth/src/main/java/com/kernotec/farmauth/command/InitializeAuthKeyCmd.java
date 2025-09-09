package com.kernotec.farmauth.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farmauth.jpa.entity.AuthKey;
import com.kernotec.farmauth.jpa.service.AuthKeyService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InitializeAuthKeyCmd extends
    AbstractTransactionalRequiredCommand<InitializeAuthKeyCmd.Request, UUID>
{

    private final AuthKeyService authKeyService;

    @Override
    protected UUID run(Request request) {
        AuthKey authKey = new AuthKey();

        authKey.setRealm(request.getRealm());
        authKey.setKid(request.getKid());
        authKey.setPrivateKey(request.getPrivateKey());
        authKey.setPublicKey(request.getPublicKey());
        authKey.setActive(request.getActive());

        authKey = authKeyService.save(authKey);
        return authKey.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        private final String realm;
        @NotNull
        private final String kid;
        @NotNull
        private final String privateKey;
        @NotNull
        private final String publicKey;
        @NotNull
        private final Boolean active;
    }
}

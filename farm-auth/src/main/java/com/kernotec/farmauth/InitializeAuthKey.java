package com.kernotec.farmauth;

import com.kernotec.farmauth.command.InitializeAuthKeyCmd;
import com.kernotec.farmauth.jpa.entity.AuthKey;
import com.kernotec.farmauth.jpa.service.AuthKeyService;
import com.nimbusds.jose.jwk.RSAKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitializeAuthKey implements CommandLineRunner {

    private final AuthKeyService authKeyService;
    private final InitializeAuthKeyCmd initializeAuthKeyCmd;

    public InitializeAuthKey(AuthKeyService authKeyService,
        InitializeAuthKeyCmd initializeAuthKeyCmd)
    {
        this.authKeyService = authKeyService;
        this.initializeAuthKeyCmd = initializeAuthKeyCmd;
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<AuthKey> authKey = authKeyService.findByRealmAndActive("farm-auth", true);

        if (authKey.isPresent()) {
            log.info("Auth Key already initialized");
        } else {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            TextEncryptor textEncryptor = Encryptors.text("pass-test", "12345678");
            String publicEncrypted = textEncryptor.encrypt(keyPair.getPublic()
                .toString());

            String privateEncrypted = textEncryptor.encrypt(keyPair.getPrivate()
                .toString());

            initializeAuthKeyCmd.withRequest(InitializeAuthKeyCmd.Request.builder()
                    .realm("farm-auth")
                    .kid("farm-auth-key")
                    .publicKey(publicEncrypted)
                    .privateKey(privateEncrypted)
                    .active(true)
                    .build())
                .execute();

            RSAKey rsaKey = new RSAKey.Builder(
                (java.security.interfaces.RSAPublicKey) keyPair.getPublic()).privateKey(
                    keyPair.getPrivate())
                .keyID("farm-auth-key")
                .build();

            log.info("Auth Key not initialized, key pair pu generated {}", publicEncrypted);
            log.info("Auth Key not initialized, key pair pri generated {}", privateEncrypted);
        }
    }
}

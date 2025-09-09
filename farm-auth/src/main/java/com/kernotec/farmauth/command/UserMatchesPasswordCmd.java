package com.kernotec.farmauth.command;

import com.kernotec.core.command.AbstractCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserMatchesPasswordCmd extends
    AbstractCommand<UserMatchesPasswordCmd.Request, Boolean>
{

    @Override
    protected Boolean run(Request request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(request.getPassword(), request.getHashedPassword());
    }

    @Builder
    @Getter
    public static class Request {

        @NotBlank
        @NotNull
        private final String password;
        @NotNull
        @NotBlank
        private final String hashedPassword;
    }
}

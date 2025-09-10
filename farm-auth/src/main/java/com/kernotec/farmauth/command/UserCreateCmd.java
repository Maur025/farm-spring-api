package com.kernotec.farmauth.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.farmauth.jpa.entity.User;
import com.kernotec.farmauth.jpa.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCreateCmd extends
    AbstractTransactionalRequiredCommand<UserCreateCmd.Request, UUID>
{

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected UUID run(Request request) {
        User user = new User();

        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedOn(ZonedDateTime.now());

        user = userService.save(user);
        return user.getId();
    }

    @Builder
    @Getter
    public static class Request {

        @NotNull
        @NotBlank
        private final String name;
        @NotNull
        private final String lastName;
        @NotNull
        @NotBlank
        private final String username;
        @NotNull
        @NotBlank
        private final String password;
    }
}

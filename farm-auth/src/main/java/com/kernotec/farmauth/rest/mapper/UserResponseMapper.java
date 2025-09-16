package com.kernotec.farmauth.rest.mapper;

import com.kernotec.farmauth.jpa.entity.User;
import com.kernotec.farmauth.rest.dto.response.UserResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface UserResponseMapper {

    UserResponse toResponse(UUID id);

    UserResponse toResponse(User user);

    List<UserResponse> toResponse(List<User> userList);

    Set<UserResponse> toResponse(Set<User> userSet);
}

package com.kernotec.farm.audit.user.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserDataResponse {

    private String id;
    private String username;
    private String name;
    private List<String> roles;
    private Long authTime;
}

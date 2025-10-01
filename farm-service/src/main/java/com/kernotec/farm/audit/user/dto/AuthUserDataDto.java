package com.kernotec.farm.audit.user.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserDataDto {

    private String id;
    private String username;
    private String name;
    private List<String> roles;
    private Long authTime;
}

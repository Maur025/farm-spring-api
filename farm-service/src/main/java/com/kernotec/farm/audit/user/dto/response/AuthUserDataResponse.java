package com.kernotec.farm.audit.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class AuthUserDataResponse {

    private String id;
    private String username;
    private String name;
    private List<String> roles;
    private Long authTime;
}

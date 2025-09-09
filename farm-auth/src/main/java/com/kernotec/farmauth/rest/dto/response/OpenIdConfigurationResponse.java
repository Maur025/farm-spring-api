package com.kernotec.farmauth.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class OpenIdConfigurationResponse {

    private String issuer;
    private String authorization_endpoint;
    private String token_endpoint;
    private String userinfo_endpoint;
    private String jwks_uri;
    private List<String> response_types_supported;
    private List<String> subject_types_supported;
    private List<String> id_token_signing_alg_values_supported;
}

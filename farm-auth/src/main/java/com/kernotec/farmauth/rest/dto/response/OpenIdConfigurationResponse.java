package com.kernotec.farmauth.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class OpenIdConfigurationResponse {

    private String issuer;
    @JsonProperty("authorization_endpoint")
    private String authorizationEndpoint;
    @JsonProperty("token_endpoint")
    private String tokenEndpoint;
    @JsonProperty("userinfo_endpoint")
    private String userinfoEndpoint;
    @JsonProperty("jwks_uri")
    private String jwksUri;
    @JsonProperty("response_types_supported")
    private List<String> responseTypesSupported;
    @JsonProperty("subject_types_supported")
    private List<String> subjectTypesSupported;
    @JsonProperty("id_token_signing_alg_values_supported")
    private List<String> idTokenSigningAlgValuesSupported;
}

package com.kernotec.farmauth.config;

import com.kernotec.farmauth.jpa.enums.RefreshTokenSecureEnum;
import com.kernotec.farmauth.jpa.enums.TimeMeasureEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oauth2-config")
@Getter
@Setter
public class AuthConfigProperties {

    private String secretKey;
    private String audience;
    private Integer authTokenExp;
    private TimeMeasureEnum authTokenTypeExp;
    private Integer refreshTokenExp;
    private TimeMeasureEnum refreshTokenTypeExp;
    private RefreshTokenSecureEnum refreshTokenSecure;
}

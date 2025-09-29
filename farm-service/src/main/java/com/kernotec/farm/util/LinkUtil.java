package com.kernotec.farm.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LinkUtil {

    public String getIdentityFacebookOfLink(String link) {
        if (link == null) {
            return null;
        }

        String linkLastPart = link.substring(link.lastIndexOf('/') + 1);
        String identityUsername = linkLastPart;

        if (linkLastPart.contains("?id=")) {
            identityUsername = linkLastPart.substring(linkLastPart.indexOf("?id=") + 4);
        }

        return identityUsername;
    }
}

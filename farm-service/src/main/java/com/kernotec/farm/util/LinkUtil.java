package com.kernotec.farm.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LinkUtil {

    public String getIdentityFacebookOfLink(String link) {
        if (link == null) {
            return null;
        }

        if (link.length() - 1 == link.lastIndexOf("/")) {
            link = link.substring(0, link.lastIndexOf("/"));
        }

        String linkLastPart = link.substring(link.lastIndexOf('/') + 1);
        String identityUsername = linkLastPart;

        if (linkLastPart.contains("?id=")) {
            identityUsername = linkLastPart.substring(linkLastPart.indexOf("?id=") + 4);
        }

        return identityUsername;
    }
}

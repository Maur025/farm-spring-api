package com.kernotec.farm.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kernotec.core.test.UnitTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

@Slf4j
class LinkUtilTest extends UnitTest {

    @InjectMocks
    private LinkUtil linkUtil;

    @BeforeEach
    public void openMocks() {
        super.openMocks();
    }

    @DisplayName("should get identity Facebook of link")
    @Test
    void shouldGetIdentityFacebookOfLink() {
        String linkWithId = "https://www.facebook.com/profile.php?id=61581513357209";

        String linkIdentity = linkUtil.getIdentityFacebookOfLink(linkWithId);

        assertEquals("61581513357209", linkIdentity);
    }

    @DisplayName("should get identity Facebook of link with username")
    @Test
    void shouldGetIdentityFacebookOfLinkWithUsername() {
        String linkWithUsername = "https://www.facebook.com/john.doe.123/";

        String linkIdentity = linkUtil.getIdentityFacebookOfLink(linkWithUsername);

        assertEquals("john.doe.123", linkIdentity);
    }
}
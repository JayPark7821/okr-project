package kr.objet.okrproject.application.user;

import kr.objet.okrproject.application.user.fixture.OAuth2UserInfoFixture;
import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.domain.guest.service.GuestService;
import kr.objet.okrproject.domain.token.service.RefreshTokenService;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import kr.objet.okrproject.domain.user.service.UserService;
import kr.objet.okrproject.infrastructure.user.auth.info.GoogleOAuth2UserInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@SpringBootTest
class UserFacadeTest {

    @Autowired
    UserFacade userFacade;

    @MockBean
    private UserService userService;
    @MockBean
    private GuestService guestService;
    @MockBean
    private RefreshTokenService refreshTokenService;
    @MockBean
    private BCryptPasswordEncoder passwordEncoder;


    @Test
    void 로그인이_정상적으로_동작하는_경우() {
        String provider = "GOOGLE";
        String idToken = "idToken";

        OAuth2UserInfo userInfoFixture = OAuth2UserInfoFixture.get("1", "name", "email", "url");
        User userFixture = UserFixture.create();
        when(userService.getUserInfoFromIdToken(any(),any()))
                .thenReturn(userInfoFixture);

        when(userService.findUserBy(any())).thenReturn(userFixture);
        when(userService.isJoining(any(), any())).thenReturn(false);
        when(refreshTokenService.generateRefreshToken(any())).thenReturn("refresh-token");

        UserInfo.Response response = assertDoesNotThrow(() -> userFacade.loginWithSocialIdToken(provider, idToken));
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
        assertNull(response.getGuestUuid());
    }


}
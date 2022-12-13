package kr.objet.okrproject.application.user;

import kr.objet.okrproject.application.user.fixture.OAuth2UserInfoFixture;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.guest.GuestCommand;
import kr.objet.okrproject.domain.user.UserInfo;
import kr.objet.okrproject.domain.user.auth.TokenVerifyProcessor;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Transactional
@SpringBootTest
class UserFacadeIntegrationTest {
    private static final String PROVIDER_TYPE = "LOCAL";
    private static final String ID_TOKEN = "test-token";

    @Autowired
    private UserFacade sut;

    @MockBean
    private TokenVerifyProcessor processor;

    @BeforeEach
    void setUp() {
        given(processor.verifyIdToken(any(ProviderType.class), anyString()))
                .willReturn(OAuth2UserInfoFixture.create("test-id", "test-name", "test@email.com", ""));
    }

    @Test
    @DisplayName("가입한 유저 정보가 없을 때 idToken을 통해 로그인을 시도하면 기대하는 응답(Guest)을 반환한다.")
    void loginWithSocialIdToken_when_before_join() {
        final UserInfo.Response response = sut.loginWithSocialIdToken(PROVIDER_TYPE, ID_TOKEN);

        assertGuestLoginResponse(response);
    }

    @Test
    @DisplayName("게스트 정보가 없을 때 join()을 호출하면 기대하는 예외를 던진다.")
    void join_before_guest_login() {
        final GuestCommand.Join command = createJoinCommand();

        assertThatThrownBy(() -> sut.join(command))
                .isExactlyInstanceOf(OkrApplicationException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_JOIN_INFO);
    }

    @Test
    @Sql("/insert-guest-user.sql")
    @DisplayName("게스트 정보가 있을 때 join()을 호출하면 기대하는 응답을 반환한다.")
    void join_after_guest_login() {
        final GuestCommand.Join command = createJoinCommand();

        final UserInfo.Response response = sut.join(command);

        assertJoinResponse(response);
    }

    @Test
    @Sql("/insert-user.sql")
    @DisplayName("가입한 유저 정보가 있을 때 join()을 호출하면 기대하는 예외를 던진다.")
    void join_again_when_after_join() {
        final GuestCommand.Join command = createJoinCommand();

        assertThatThrownBy(() -> sut.join(command))
                .isExactlyInstanceOf(OkrApplicationException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ALREADY_JOINED_USER);
    }

    @Test
    @Sql("/insert-user.sql")
    @DisplayName("가입한 유저 정보가 있을 때 loginWithSocialIdToken()을 호출하면 기대하는 응답을 반환한다.")
    void loginWithSocialIdToken_when_after_join() {
        final UserInfo.Response response = sut.loginWithSocialIdToken(PROVIDER_TYPE, ID_TOKEN);

        assertJoinResponse(response);
    }

    @Test
    @Sql("/insert-user.sql")
    @DisplayName("가입한 유저 정보와 다른 ProviderType으로 loginWithSocialIdToken()을 호출하면 기대하는 예외를 던진다.")
    void loginWithSocialIdToken_when_after_join_and_with_another_provider() {
        assertThatThrownBy(() -> sut.loginWithSocialIdToken("GOOGLE", ID_TOKEN))
                .isExactlyInstanceOf(OkrApplicationException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MISS_MATCH_PROVIDER);
    }

    private GuestCommand.Join createJoinCommand() {
        return new GuestCommand.Join("guest-uuid", "user-name", "test@email.com", "WEB_SERVER_DEVELOPER");
    }

    private void assertGuestLoginResponse(final UserInfo.Response response) {
        assertThat(response).hasNoNullFieldsOrPropertiesExcept("roleType", "jobFieldDetail", "accessToken", "refreshToken");
        assertThat(response.getGuestUuid()).isNotBlank();
    }

    private void assertJoinResponse(final UserInfo.Response joinResponse) {
        assertThat(joinResponse).hasNoNullFieldsOrPropertiesExcept("guestUuid");
        assertThat(joinResponse.getAccessToken()).isNotBlank();
        assertThat(joinResponse.getRefreshToken()).isNotBlank();
    }

}
/**

# insert-guest-user.sql
INSERT INTO guest (guset_uuid, guest_id, guest_name, email, provider_type, profile_image_url)
VALUES ('guest-uuid', 'test-id', 'test-name', 'test@email.com', 'LOCAL', '');

# insert-user.sql
INSERT INTO user_table (created_date, last_modified_date, user_id, username, password, email, email_verified_yn,
                        profile_image_url, provider_type, role_type, job)
VALUES ('2022-12-11', '2022-12-11', 'test-id', 'test-username', 'test-pw', 'test@email.com', 'Y', '', 'LOCAL',
        'ADMIN', 'WEB_SERVER_DEVELOPER');

**/
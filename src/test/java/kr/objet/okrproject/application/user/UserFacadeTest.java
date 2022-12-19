package kr.objet.okrproject.application.user;

import kr.objet.okrproject.application.user.fixture.GuestCommandFixture;
import kr.objet.okrproject.application.user.fixture.GuestFixture;
import kr.objet.okrproject.application.user.fixture.OAuth2UserInfoFixture;
import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.guest.Guest;
import kr.objet.okrproject.domain.guest.GuestCommand;
import kr.objet.okrproject.domain.guest.service.GuestService;
import kr.objet.okrproject.domain.token.service.RefreshTokenService;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.UserInfo;
import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import kr.objet.okrproject.domain.user.enums.jobtype.JobTypeMapper;
import kr.objet.okrproject.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserFacadeTest {

	private UserFacade sut;
	@Mock
	private UserService userService;
	@Mock
	private GuestService guestService;
	@Mock
	private RefreshTokenService refreshTokenService;
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	@Mock
	private JobTypeMapper jobTypeMapper;

	private final String PROVIDER = "GOOGLE";
	private final ProviderType PROVIDER_TYPE = ProviderType.of(PROVIDER);

	private final String ID_TOKEN = "idToken";

	private final OAuth2UserInfo userInfoFixture = OAuth2UserInfoFixture.create("1", "name", "email", "url");

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		sut = new UserFacade(
			userService,
			guestService,
			refreshTokenService,
			passwordEncoder,
			jobTypeMapper
		);

		ReflectionTestUtils.setField(sut, "secretKey", "secretKey-abcdefg-abc-abc-abc11122");
		ReflectionTestUtils.setField(sut, "expiredTimeMs", 1000L);
	}

	@Test
	void 로그인이_정상적으로_동작하는_경우() {
		//given
		User userFixture = UserFixture.create();
		given(userService.getUserInfoFromIdToken(eq(PROVIDER_TYPE), eq(ID_TOKEN))).willReturn(userInfoFixture);
		given(userService.findUserBy(userInfoFixture.getEmail())).willReturn(userFixture);
		given(userService.isJoining(eq(userFixture), eq(PROVIDER_TYPE))).willReturn(false);
		given(refreshTokenService.generateRefreshToken(userFixture.getEmail())).willReturn("refresh-token");

		//when
		UserInfo.Response response = assertDoesNotThrow(() -> sut.loginWithSocialIdToken(PROVIDER, ID_TOKEN));

		//then
		assertNotNull(response.getAccessToken());
		assertNotNull(response.getRefreshToken());
		assertNull(response.getGuestUuid());
	}

	@Test
	void 임시_회원등록() throws Exception {
		//given
		Guest guestFixture = GuestFixture.create();
		given(userService.getUserInfoFromIdToken(eq(PROVIDER_TYPE), eq(ID_TOKEN))).willReturn(userInfoFixture);
		given(userService.findUserBy(userInfoFixture.getEmail())).willReturn(null);
		given(userService.isJoining(eq(null), eq(PROVIDER_TYPE))).willReturn(true);
		given(guestService.registerGuest(any())).willReturn(guestFixture);

		//when
		UserInfo.Response response = assertDoesNotThrow(() -> sut.loginWithSocialIdToken(PROVIDER, ID_TOKEN));

		//then
		assertNull(response.getAccessToken());
		assertNull(response.getRefreshToken());
		assertNotNull(response.getGuestUuid());
	}

	@Test
	void 게스트_회원가입_성공() throws Exception {
		//given
		GuestCommand.Join command = GuestCommandFixture.create();
		Guest guestFixture = GuestFixture.create();
		User userFixture = UserFixture.create();

		given(userService.findUserBy(eq(command.getEmail()))).willReturn(null);
		given(guestService.retrieveGuest(command)).willReturn(guestFixture);
		given(refreshTokenService.generateRefreshToken(guestFixture.getEmail())).willReturn("refresh-token");
		given(userService.store(any())).willReturn(userFixture);

		//when
		UserInfo.Response response = assertDoesNotThrow(() -> sut.join(command));

		//then
		assertNotNull(response.getAccessToken());
		assertNotNull(response.getRefreshToken());
		assertNull(response.getGuestUuid());
	}

	@Test
	void 회원가입_실패_이미가입됨() throws Exception {
		//given
		GuestCommand.Join command = GuestCommandFixture.create();
		User userFixture = UserFixture.create();

		given(userService.findUserBy(command.getEmail())).willReturn(userFixture);
		//when
		OkrApplicationException exception = assertThrows(OkrApplicationException.class,
			() -> sut.join(command));
		//then
		assertEquals("이미 가입된 회원입니다.", exception.getMessage());
	}

	@Test
	void 회원가입_실패_등록된_게스트_없음() throws Exception {
		//given
		GuestCommand.Join command = GuestCommandFixture.create();
		User userFixture = UserFixture.create();

		given(userService.findUserBy(command.getEmail())).willReturn(null);
		given(guestService.retrieveGuest(command)).willReturn(null);
		//when
		OkrApplicationException exception = assertThrows(OkrApplicationException.class,
			() -> sut.join(command));
		//then
		assertEquals("잘못된 가입 정보 입니다.", exception.getMessage());
	}

}
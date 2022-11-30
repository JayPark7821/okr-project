package kr.objet.okrproject.application.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.objet.okrproject.common.utils.JwtTokenUtils;
import kr.objet.okrproject.domain.guest.GuestCommand;
import kr.objet.okrproject.domain.guest.GuestInfo;
import kr.objet.okrproject.domain.guest.service.GuestService;
import kr.objet.okrproject.domain.token.service.RefreshTokenService;
import kr.objet.okrproject.domain.user.UserInfo;
import kr.objet.okrproject.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacade {

	private final UserService userService;
	private final GuestService guestService;
	private final RefreshTokenService refreshTokenService;

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.token.access-expired-time-ms}")
	private Long expiredTimeMs;

	public UserInfo.Response loginWithSocialIdToken(String provider, String idToken) {
		UserInfo.Main userInfo = userService.getUserInfoFromIdToken(provider, idToken);

		boolean isJoining = userService.isJoining(userInfo, provider);

		if (isJoining) {
			GuestInfo.Main guestInfo = guestService.registerGuest(new GuestCommand.RegisterGuest(userInfo));
			return UserInfo.Response.join(guestInfo);

		} else {
			String accessToken = JwtTokenUtils.generateToken(userInfo.getEmail(), secretKey, expiredTimeMs);
			String refreshToken = refreshTokenService.generateRefreshToken(userInfo.getEmail());

			return UserInfo.Response.login(userInfo,accessToken,refreshToken);
		}
	}

}

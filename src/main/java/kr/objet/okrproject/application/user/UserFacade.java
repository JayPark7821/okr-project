package kr.objet.okrproject.application.user;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
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
	private final BCryptPasswordEncoder passwordEncoder;

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.token.access-expired-time-ms}")
	private Long expiredTimeMs;

	public UserInfo.Response join(GuestCommand.Join command) {
		if (Objects.isNull(userService.findUserInfoBy(command.getEmail()))) {
			throw new OkrApplicationException(ErrorCode.ALREADY_JOINED_USER);
		}
		GuestInfo.Main guestInfo = guestService.retrieveGuest(command);

		if (Objects.isNull(guestInfo)) {
			throw new OkrApplicationException(ErrorCode.INVALID_JOIN_INFO);
		}
		String accessToken = JwtTokenUtils.generateToken(guestInfo.getEmail(), secretKey, expiredTimeMs);
		String refreshToken = refreshTokenService.generateRefreshToken(guestInfo.getEmail());

		return UserInfo.Response.login(userService.store(command.toUserEntity(guestInfo, generateTempPw())),accessToken,refreshToken);
	}

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

	private String generateTempPw() {
		String uuid = UUID.randomUUID().toString();
		System.out.println("uuid = " + uuid);
		String initialPw = passwordEncoder.encode(uuid);
		System.out.println("initialPw = " + initialPw);
		return initialPw;

	}

}

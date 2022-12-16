package kr.objet.okrproject.application.user;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.common.utils.JwtTokenUtils;
import kr.objet.okrproject.domain.guest.Guest;
import kr.objet.okrproject.domain.guest.GuestCommand;
import kr.objet.okrproject.domain.guest.service.GuestService;
import kr.objet.okrproject.domain.token.service.RefreshTokenService;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.UserInfo;
import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import kr.objet.okrproject.domain.user.enums.jobtype.JobField;
import kr.objet.okrproject.domain.user.enums.jobtype.JobTypeInfo;
import kr.objet.okrproject.domain.user.enums.jobtype.JobTypeMapper;
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
	private final JobTypeMapper jobTypeMapper;

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.token.access-expired-time-ms}")
	private Long expiredTimeMs;

	public UserInfo.Response join(GuestCommand.Join command) {
		if (!Objects.isNull(userService.findUserBy(command.getEmail()))) {
			throw new OkrApplicationException(ErrorCode.ALREADY_JOINED_USER);
		}
		Guest guest = guestService.retrieveGuest(command);

		if (Objects.isNull(guest)) {
			throw new OkrApplicationException(ErrorCode.INVALID_JOIN_INFO);
		}
		String accessToken = JwtTokenUtils.generateToken(guest.getEmail(), secretKey, expiredTimeMs);
		String refreshToken = refreshTokenService.generateRefreshToken(guest.getEmail());

		return UserInfo.Response.login(userService.store(command.toUserEntity(guest, generateTempPw())), accessToken,
			refreshToken);
	}

	public UserInfo.Response loginWithSocialIdToken(String provider, String idToken) {

		ProviderType providerType = ProviderType.of(provider);
		OAuth2UserInfo userInfo = userService.getUserInfoFromIdToken(providerType, idToken);
		User user = userService.findUserBy(userInfo.getEmail());

		boolean isJoining = userService.isJoining(user, providerType);

		if (isJoining) {
			Guest guest = guestService.registerGuest(new GuestCommand.RegisterGuest(userInfo, providerType));
			return UserInfo.Response.join(guest);
		} else {
			String accessToken = JwtTokenUtils.generateToken(user.getEmail(), secretKey, expiredTimeMs);
			String refreshToken = refreshTokenService.generateRefreshToken(user.getEmail());
			return UserInfo.Response.login(user, accessToken, refreshToken);
		}
	}

	public List<JobTypeInfo.Response> getJobType() {
		return jobTypeMapper.get("JobField");
	}

	public List<JobTypeInfo.Response> getJobTypeDetail(JobField jobField) {
		return jobField.getDetailList()
			.stream()
			.map(JobTypeInfo.Response::new)
			.collect(Collectors.toList());
	}

	private String generateTempPw() {
		String uuid = UUID.randomUUID().toString();
		System.out.println("uuid = " + uuid);
		return passwordEncoder.encode(uuid);

	}

}

package kr.objet.okrproject.domain.token.service.impl;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.common.utils.HeaderUtil;
import kr.objet.okrproject.common.utils.JwtTokenUtils;
import kr.objet.okrproject.domain.token.RefreshToken;
import kr.objet.okrproject.domain.token.service.RefreshTokenReader;
import kr.objet.okrproject.domain.token.service.RefreshTokenService;
import kr.objet.okrproject.domain.token.service.RefreshTokenStore;
import kr.objet.okrproject.domain.user.UserInfo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final RefreshTokenReader refreshTokenReader;
	private final RefreshTokenStore refreshTokenStore;
	@Value("${jwt.secret-key}")
	private String secretKey;
	@Value("${jwt.token.refresh-expired-time-ms}")
	private Long refreshExpiredTimeMs;
	@Value("${jwt.token.access-expired-time-ms}")
	private Long accessExpiredTimeMs;
	private final static long THREE_DAYS_MSEC = 259200000;

	@Override
	@Transactional
	public String generateRefreshToken(String email) {
		String refreshTokenString = JwtTokenUtils.generateToken(email, secretKey, refreshExpiredTimeMs);
		Optional<RefreshToken> refreshToken = refreshTokenReader.findRefreshTokenByEmail(email);

		if (refreshToken.isPresent()) {
			refreshToken.get().updateRefreshToken(refreshTokenString);
		} else {
			refreshTokenStore.store(new RefreshToken(email, refreshTokenString));
		}

		return refreshTokenString;
	}

	@Override
	public UserInfo.Token reGenerateRefreshToken(HttpServletRequest request) {
		String token = HeaderUtil.getToken("Authorization", request);
		String email = JwtTokenUtils.getEmail(token, secretKey);

		refreshTokenReader.findRefreshTokenByEmailAndRefreshToken(email, token)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_TOKEN));

		Date now = new Date();
		long validTime = JwtTokenUtils.getExpired(token, secretKey).getTime() - now.getTime();
		String accessToken = JwtTokenUtils.generateToken(email, secretKey, accessExpiredTimeMs);

		if (validTime <= THREE_DAYS_MSEC) {
			return new UserInfo.Token(accessToken, generateRefreshToken(email));
		} else {
			return new UserInfo.Token(accessToken, null);
		}
	}
}

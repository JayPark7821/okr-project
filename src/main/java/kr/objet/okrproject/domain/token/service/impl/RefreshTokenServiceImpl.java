package kr.objet.okrproject.domain.token.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.objet.okrproject.common.utils.JwtTokenUtils;
import kr.objet.okrproject.domain.token.RefreshToken;
import kr.objet.okrproject.domain.token.service.RefreshTokenReader;
import kr.objet.okrproject.domain.token.service.RefreshTokenService;
import kr.objet.okrproject.domain.token.service.RefreshTokenStore;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {

	private final RefreshTokenReader refreshTokenReader;
	private final RefreshTokenStore refreshTokenStore;
	@Value("${jwt.secret-key}")
	private String secretKey;
	@Value("${jwt.refresh-token.expired-time-ms}")
	private Long expiredTimeMs;

	@Override
	@Transactional
	public String generateRefreshToken(String email) {
		String refreshTokenString = JwtTokenUtils.generateToken(email, secretKey, expiredTimeMs);
		Optional<RefreshToken> refreshToken = refreshTokenReader.findRefreshTokenByEmail(email);

		if (refreshToken.isPresent()) {
			refreshToken.get().updateRefreshToken(refreshTokenString);
		} else {
			refreshTokenStore.store(new RefreshToken(email, refreshTokenString));
		}

		return refreshTokenString;
	}
}

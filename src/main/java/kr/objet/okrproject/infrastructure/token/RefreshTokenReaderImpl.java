package kr.objet.okrproject.infrastructure.token;

import java.util.Optional;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.token.RefreshToken;
import kr.objet.okrproject.domain.token.service.RefreshTokenReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenReaderImpl implements RefreshTokenReader {

	private final RefreshTokenRepository repository;

	@Override
	public Optional<RefreshToken> findRefreshTokenByEmail(String email) {
		return repository.findByUserEmail(email);
	}

	@Override
	public Optional<RefreshToken> findRefreshTokenByEmailAndRefreshToken(String email, String token) {
		return repository.findRefreshTokenByEmailAndRefreshToken(email, token);
	}

}

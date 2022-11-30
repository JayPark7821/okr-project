package kr.objet.okrproject.infrastructure.token;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.token.RefreshToken;
import kr.objet.okrproject.domain.token.service.RefreshTokenStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenStoreImpl implements RefreshTokenStore {

	private final RefreshTokenRepository repository;
	@Override
	public RefreshToken store(RefreshToken refreshToken) {
		return repository.saveAndFlush(refreshToken);
	}

}

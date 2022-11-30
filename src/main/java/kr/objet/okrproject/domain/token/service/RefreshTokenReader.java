package kr.objet.okrproject.domain.token.service;

import java.util.Optional;

import kr.objet.okrproject.domain.token.RefreshToken;

public interface RefreshTokenReader {

	Optional<RefreshToken> findRefreshTokenByEmail(String email);
}

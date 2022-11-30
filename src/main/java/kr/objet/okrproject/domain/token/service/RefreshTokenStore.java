package kr.objet.okrproject.domain.token.service;

import kr.objet.okrproject.domain.token.RefreshToken;

public interface RefreshTokenStore {

	RefreshToken store(RefreshToken refreshToken);
}

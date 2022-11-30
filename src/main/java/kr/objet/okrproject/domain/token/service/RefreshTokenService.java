package kr.objet.okrproject.domain.token.service;

public interface RefreshTokenService {

	String generateRefreshToken(String email);
}

package kr.objet.okrproject.domain.token.service;

import javax.servlet.http.HttpServletRequest;

import kr.objet.okrproject.domain.user.UserInfo;

public interface RefreshTokenService {

	String generateRefreshToken(String email);

	UserInfo.Token reGenerateRefreshToken(HttpServletRequest request);
}

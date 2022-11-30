package kr.objet.okrproject.infrastructure.token;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.objet.okrproject.domain.token.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	RefreshToken findByUserId(String userId);
	RefreshToken findByUserIdAndRefreshToken(String userId, String refreshToken);
}

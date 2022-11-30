package kr.objet.okrproject.infrastructure.token;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.objet.okrproject.domain.token.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByUserEmail(String email);
	Optional<RefreshToken> findByUserEmailAndRefreshToken(String email, String refreshToken);
}

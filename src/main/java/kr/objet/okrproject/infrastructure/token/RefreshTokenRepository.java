package kr.objet.okrproject.infrastructure.token;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.objet.okrproject.domain.token.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByUserEmail(String email);

	Optional<RefreshToken> findByUserEmailAndRefreshToken(String email, String refreshToken);

	@Query("select r "
		+ "from RefreshToken  r "
		+ "where r.refreshToken =:token "
		+ "and r.userEmail =:email")
	Optional<RefreshToken> findRefreshTokenByEmailAndRefreshToken(@Param("email") String email,
		@Param("token") String token);

}

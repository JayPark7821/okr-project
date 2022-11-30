package kr.objet.okrproject.infrastructure.guest;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.objet.okrproject.domain.guest.Guest;

public interface GuestRepository extends JpaRepository<Guest, String> {

	@Query("select g from Guest g where g.guestUuid = :guestUuid and g.email = :email")
	Optional<Guest> findGuestByParams(@Param("guestUuid") String guestUuid, @Param("email") String email);
}

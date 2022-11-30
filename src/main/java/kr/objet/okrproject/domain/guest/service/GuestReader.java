package kr.objet.okrproject.domain.guest.service;

import java.util.Optional;

import kr.objet.okrproject.domain.guest.Guest;

public interface GuestReader {
	Optional<Guest> findGuestByUuidAndEmail(String uuId, String email);
}

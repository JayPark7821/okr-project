package kr.objet.okrproject.infrastructure.guest;

import java.util.Optional;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.guest.Guest;
import kr.objet.okrproject.domain.guest.service.GuestReader;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GuestReaderImpl implements GuestReader {

	private final GuestRepository repository;

	@Override
	public Optional<Guest> findGuestByUuidAndEmail(String uuId, String email) {
		return repository.findGuestByParams(uuId, email);
	}
}

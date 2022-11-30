package kr.objet.okrproject.infrastructure.guest;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.guest.Guest;
import kr.objet.okrproject.domain.guest.service.GuestStore;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GuestStoreImpl implements GuestStore {

	private final GuestRepository repository;

	@Override
	public Guest store(Guest guest) {
		return repository.save(guest);
	}
}

package kr.objet.okrproject.domain.guest.service.impl;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.guest.Guest;
import kr.objet.okrproject.domain.guest.GuestCommand;
import kr.objet.okrproject.domain.guest.service.GuestReader;
import kr.objet.okrproject.domain.guest.service.GuestService;
import kr.objet.okrproject.domain.guest.service.GuestStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

	private final GuestReader guestReader;
	private final GuestStore guestStore;

	@Override
	public Guest registerGuest(GuestCommand.RegisterGuest command) {
		return guestStore.store(command.toEntity());
	}

	@Override
	public Guest retrieveGuest(GuestCommand.Join command) {
		return guestReader.findGuestByUuidAndEmail(command.getGuestUuId(),
			command.getEmail()).orElse(null);
	}

}

package kr.objet.okrproject.domain.guest.service.impl;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.guest.Guest;
import kr.objet.okrproject.domain.guest.GuestCommand;
import kr.objet.okrproject.domain.guest.GuestInfo;
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
	public GuestInfo.Main retrieveGuestInfo(String guestId) {
		Guest guest = guestReader.getGuestBy(guestId);
		return new GuestInfo.Main(guest);
	}

	@Override
	public Guest registerGuest(GuestCommand.RegisterGuest command) {
		Guest guest = command.toEntity();
		return guestStore.store(guest);
	}

}

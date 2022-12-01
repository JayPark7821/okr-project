package kr.objet.okrproject.domain.guest.service;

import kr.objet.okrproject.domain.guest.Guest;
import kr.objet.okrproject.domain.guest.GuestCommand;
import kr.objet.okrproject.domain.guest.GuestInfo;

public interface GuestService {

	Guest registerGuest(GuestCommand.RegisterGuest command);

	Guest retrieveGuest(GuestCommand.Join command);

}

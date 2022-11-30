package kr.objet.okrproject.domain.guest.service;

import kr.objet.okrproject.domain.guest.GuestCommand;
import kr.objet.okrproject.domain.guest.GuestInfo;

public interface GuestService {

	GuestInfo.Main registerGuest(GuestCommand.RegisterGuest command);

}

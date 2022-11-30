package kr.objet.okrproject.application.user;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.guest.GuestCommand;
import kr.objet.okrproject.domain.guest.GuestInfo;
import kr.objet.okrproject.domain.guest.service.GuestService;
import kr.objet.okrproject.domain.user.UserInfo;
import kr.objet.okrproject.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacade {

	private final UserService userService;
	private final GuestService guestService;

	public UserInfo.Response loginWithSocialIdToken(String provider, String idToken) {
		UserInfo.Main userInfo = userService.getUserInfoFromIdToken(provider, idToken);

		boolean isJoining = userService.isJoining(userInfo, provider);

		if (isJoining) {
			GuestInfo.Main guestInfo = guestService.registerGuest(new GuestCommand.RegisterGuest(userInfo));
			return UserInfo.Response.join(guestInfo, null, null);
		} else {
			return UserInfo.Response.login(userInfo, null, null);
		}
	}

}

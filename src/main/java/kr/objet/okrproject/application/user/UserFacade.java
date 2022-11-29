package kr.objet.okrproject.application.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.user.UserInfo;
import kr.objet.okrproject.domain.user.service.UserService;
import kr.objet.okrproject.interfaces.user.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacade {

	private final UserService userService;

	public UserDto.LoginResponse loginWithSocialIdToken(HttpServletRequest request, String provider, String idToken) {
		UserInfo.Main userInfo = userService.loginWithSocialIdToken(request, provider, idToken);

		return null;
	}

}

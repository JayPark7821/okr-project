package kr.objet.okrproject.interfaces.notification;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.objet.okrproject.application.notification.NotificationFacade;
import kr.objet.okrproject.common.Response;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.common.utils.ClassUtils;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationApiController {

	private final NotificationFacade notificationFacade;

	@GetMapping
	public ResponseEntity<Response<List<NotificationDto.Response>>> getNotifications(Authentication authentication) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		List<NotificationDto.Response> response =
			notificationFacade.getNotifications(user)
				.stream()
				.map(NotificationDto.Response::new)
				.collect(Collectors.toList());

		return Response
			.success(
				HttpStatus.OK,
				response
			);
	}

	@GetMapping("/{token}")
	public ResponseEntity<Response<Void>> updateNotificationStatus(
		@PathVariable("token") String token,
		Authentication authentication
	) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		notificationFacade.updateNotificationStatus(user, token);

		return Response
			.success(
				HttpStatus.OK
			);
	}

}

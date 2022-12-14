package kr.objet.okrproject.interfaces.initiative;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.objet.okrproject.application.initiative.InitiativeFacade;
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
@RequestMapping("/api/v1/initiative")
public class InitiativeApiController {

	private final InitiativeFacade initiativeFacade;

	@PostMapping
	public ResponseEntity<Response<String>> registerKeyResult(
		@RequestBody @Valid InitiativeDto.Save requestDto,
		Authentication authentication) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		return Response
			.success(
				HttpStatus.CREATED,
				initiativeFacade.registerInitiative(requestDto.toCommand(), user)
			);
	}

	@GetMapping("/{keyResultToken}")
	public ResponseEntity<Response<Page<InitiativeDto.Response>>> searchInitiatives(
		@PathVariable("keyResultToken") String keyResultToken,
		Authentication authentication,
		Pageable page
	) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		Page<InitiativeDto.Response> response =
			initiativeFacade.searchInitiatives(keyResultToken, user, page)
				.map(InitiativeDto.Response::new);

		return Response
			.success(
				HttpStatus.OK,
				response
			);
	}

}

package kr.objet.okrproject.interfaces.initiative;

import kr.objet.okrproject.application.initiative.InitiativeFacade;
import kr.objet.okrproject.common.Response;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.common.utils.ClassUtils;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import static kr.objet.okrproject.common.utils.DateFormatValidator.validateDate;
import static kr.objet.okrproject.common.utils.DateFormatValidator.validateYearMonth;

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

	@GetMapping("/date/{date}")
	public ResponseEntity<Response<List<InitiativeDto.Response>>> searchInitiativesByDate(
			@PathVariable("date")  String date,
			Authentication authentication
	) {

		LocalDate searchDate = validateDate(date);

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		List<InitiativeDto.Response> response =
				initiativeFacade.searchInitiativesByDate(searchDate, user)
						.stream()
						.map(InitiativeDto.Response::new)
						.collect(Collectors.toList());

		return Response
			.success(
				HttpStatus.OK,
				response
			);
	}

	@GetMapping("/yearmonth/{yearmonth}")
	public ResponseEntity<Response<List<String>>> searchActiveInitiativesByDate(
			@PathVariable("yearmonth")  String yearMonth,
			Authentication authentication
	) {

		YearMonth searchYearMonth = validateYearMonth(yearMonth);

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		List<String> results = initiativeFacade.searchActiveInitiativesByDate(searchYearMonth, user);

		return Response
			.success(
				HttpStatus.OK,
				results
			);
	}

	@PutMapping("/{initiativeToken}/done")
	public ResponseEntity<Response<String>> setInitiativeStatusToDone(
			@PathVariable("initiativeToken")  String token,
			Authentication authentication
	) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		return Response
			.success(
				HttpStatus.OK,
				initiativeFacade.setInitiativeStatusToDone(token, user)
			);
	}
}

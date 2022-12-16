package kr.objet.okrproject.interfaces.feedback;

import kr.objet.okrproject.application.feedback.FeedbackFacade;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
public class FeedbackApiController {

	private final FeedbackFacade feedbackFacade;

	@PostMapping
	public ResponseEntity<Response<String>> registerFeedback(
		@RequestBody @Valid FeedbackDto.Save requestDto,
		Authentication authentication
	) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		return Response
			.success(
				HttpStatus.CREATED,
				feedbackFacade.registerFeedback(requestDto.toCommand(), user)
			);
	}

	@GetMapping
	public ResponseEntity<Response<Page<FeedbackDto.Response>>> getAllFeedbackList(
		String searchRange,
		Pageable pageable,
		Authentication authentication
	) {
		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		return Response
			.success(
				HttpStatus.OK,
				feedbackFacade.getAllFeedbackList(searchRange, user, pageable)
					.map(FeedbackDto.Response::new)
			);
	}

	@GetMapping("/{initiativeToken}")
	public ResponseEntity<Response<FeedbackDto.IniFeedbackResponse>> getAllFeedbackListForInitiative(
		@PathVariable("initiativeToken") String token,
		Authentication authentication
	) {
		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		return Response
			.success(
				HttpStatus.OK,
				new FeedbackDto.IniFeedbackResponse(feedbackFacade.getAllFeedbackListForInitiative(token, user))
			);
	}

	@GetMapping("/count")
	public ResponseEntity<Response<Integer>> getCountForFeedbackToGive(Authentication authentication) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		return Response
			.success(
				HttpStatus.OK,
				feedbackFacade.getCountForFeedbackToGive(user)
			);
	}
}

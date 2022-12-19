package kr.objet.okrproject.interfaces.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import kr.objet.okrproject.application.user.UserFacade;
import kr.objet.okrproject.common.Response;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.common.utils.ClassUtils;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.UserInfo;
import kr.objet.okrproject.domain.user.enums.jobtype.JobField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserApiController {

	private final UserFacade userFacade;

	@Operation(summary = "idToken 로그인 처리", description = "idToken 로그인 처리 <br> <br> 로그인 성공시 response body에 access token을담아서 return <br> <br> 이후 권한이 필요한 요청에서는 header - Authorization 속성에 'Bearer ' + access token 을 담아 요청")
	@PostMapping("/login/{provider}/{idToken}")
	public ResponseEntity<Response<UserDto.LoginResponse>> loginWithSocialIdToken(
		@PathVariable("provider") String providerType,
		@PathVariable("idToken") String idToken,
		HttpServletRequest request
	) {
		UserInfo.Response response = userFacade.loginWithSocialIdToken(providerType, idToken);

		return Response.success(HttpStatus.OK, new UserDto.LoginResponse(response));
	}

	@Operation(summary = "회원 가입 처리", description = "회원가입 처리")
	@PostMapping("/join")
	public ResponseEntity<Response<UserDto.LoginResponse>> join(
		@RequestBody @Valid UserDto.RegisterRequest request
	) {
		UserInfo.Response response = userFacade.join(request.toCommand());

		return Response.success(HttpStatus.OK, new UserDto.LoginResponse(response));
	}

	@GetMapping("/job/category")
	public ResponseEntity<Response<List<JobTypeDto.Response>>> getJobCategory() {

		List<JobTypeDto.Response> response =
			userFacade.getJobType().stream()
				.map(JobTypeDto.Response::new)
				.collect(Collectors.toList());

		return Response.success(
			HttpStatus.OK,
			response
		);
	}

	@GetMapping("/job/{category}/fields")
	public ResponseEntity<Response<List<JobTypeDto.Response>>> getJobCategory(
		@PathVariable("category") String category
	) {
		JobField jobField = JobField.of(category);
		List<JobTypeDto.Response> response =
			userFacade.getJobTypeDetail(jobField).stream()
				.map(JobTypeDto.Response::new)
				.collect(Collectors.toList());

		return Response.success(
			HttpStatus.OK,
			response
		);
	}

	@GetMapping
	public ResponseEntity<Response<UserDto.Response>> getUserInfo(
		Authentication authentication
	) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		return Response.success(
			HttpStatus.OK,
			new UserDto.Response(user)
		);
	}

	@GetMapping("/refresh")
	public ResponseEntity<Response<UserDto.Token>> refreshToken(HttpServletRequest request) {
		return Response.success(
			HttpStatus.OK,
			userFacade.reGenerateToken(request).toDto()
		);
	}

}

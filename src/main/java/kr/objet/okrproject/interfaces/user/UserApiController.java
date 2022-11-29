package kr.objet.okrproject.interfaces.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import kr.objet.okrproject.application.user.UserFacade;
import kr.objet.okrproject.common.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserApiController {

	private final UserFacade userFacade;

	@Operation(summary = "idToken 로그인 처리", description = "idToken 로그인 처리 <br> <br> 로그인 성공시 response body에 access token을담아서 return <br> <br> 이후 권한이 필요한 요청에서는 header - Authorization 속성에 'Bearer ' + access token 을 담아 요청")
	@GetMapping("/login/{provider}/{idToken}")
	public ResponseEntity<Response<UserDto.LoginResponse>> loginWithSocialIdToken(
		@PathVariable("provider") String providerType,
		@PathVariable("idToken") String idToken,
		HttpServletRequest request) {

		return Response.success(HttpStatus.OK, userFacade.loginWithSocialIdToken(request, providerType, idToken));
	}
}

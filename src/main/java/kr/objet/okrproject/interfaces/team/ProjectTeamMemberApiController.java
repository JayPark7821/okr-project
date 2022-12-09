package kr.objet.okrproject.interfaces.team;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.objet.okrproject.application.team.ProjectTeamMemberFacade;
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
@RequestMapping("/api/v1/team")
@Tag(name = "TeamController", description = "OKR 프로젝트 팀 등록&조회")
public class ProjectTeamMemberApiController {

	private final ProjectTeamMemberFacade projectTeamMemberFacade;

	@PostMapping("/invite")
	public ResponseEntity<Response<ProjectTeamMemberDto.saveResponse>> inviteTeamMembers(
		@RequestBody @Valid ProjectTeamMemberDto.saveRequest requestDto,
		Authentication authentication
	) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(projectTeamMemberFacade.inviteTeamMembers(requestDto, user));
	}
}

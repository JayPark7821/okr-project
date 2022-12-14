package kr.objet.okrproject.interfaces.project;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

import kr.objet.okrproject.application.project.ProjectFacade;
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
@RequestMapping("/api/v1/project")
public class ProjectMasterApiController {

	private final ProjectFacade projectFacade;

	@PostMapping
	public ResponseEntity<Response<String>> registerProject(
		@RequestBody @Valid ProjectMasterDto.Save requestDto,
		Authentication authentication
	) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		return Response
			.success(
				HttpStatus.CREATED,
				projectFacade.registerProject(requestDto.toCommand(), user)
			);
	}

	@GetMapping
	public ResponseEntity<Response<Page<ProjectMasterDto.Response>>> findProject(
		String sortType,
		String includeFinishedProjectYN,
		Authentication authentication,
		Pageable pageable
	) {

		String finishedProjectYN = includeFinishedProjectYN == null ? "N" : includeFinishedProjectYN.toUpperCase();

		if (finishedProjectYN.matches("[YN]")) {

			User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

			Page<ProjectMasterDto.Response> response =
				projectFacade.retrieveProject(
					SortType.of(sortType),
					finishedProjectYN,
					user,
					pageable
				).map(ProjectMasterDto.Response::new);

			return Response
				.success(
					HttpStatus.OK,
					response
				);
		} else {
			throw new OkrApplicationException(ErrorCode.INVALID_FINISHED_RPOJECT_YN);
		}
	}

	@GetMapping("/{projectToken}")
	public ResponseEntity<Response<ProjectMasterDto.DetailResponse>> searchProjectDetail(
		@PathVariable("projectToken") String projectToken,
		Authentication authentication
	) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		ProjectMasterDto.DetailResponse detailResponse =
			new ProjectMasterDto.DetailResponse(
				projectFacade.searchProjectDetail(projectToken, user)
			);

		return Response
			.success(
				HttpStatus.OK,
				detailResponse
			);
	}

	@GetMapping("/{projectToken}/side")
	public ResponseEntity<Response<ProjectMasterDto.ProgressResponse>> searchProjectProgressDetail(
		@PathVariable("projectToken") String projectToken,
		Authentication authentication
	) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		ProjectMasterDto.ProgressResponse progressResponse =
			new ProjectMasterDto.ProgressResponse(
				projectFacade.searchProjectProgressDetail(projectToken, user)
			);

		return Response
			.success(
				HttpStatus.OK,
				progressResponse
			);
	}

	@GetMapping("/calendar/{yearMonth}")
	public ResponseEntity<Response<List<ProjectMasterDto.CalendarResponse>>> searchProjectsForCalendar(
		@PathVariable("yearMonth") String yearMonth,
		Authentication authentication
	) {

		YearMonth searchYearMonth = validateYearMonth(yearMonth);

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		List<ProjectMasterDto.CalendarResponse> calendarResponse =
			projectFacade.searchProjectsForCalendar(searchYearMonth, user)
				.stream()
				.map(ProjectMasterDto.CalendarResponse::new)
				.collect(Collectors.toList());

		return Response
			.success(
				HttpStatus.OK,
				calendarResponse
			);
	}

	private static YearMonth validateYearMonth(String yearMonth) {
		try {
			return yearMonth == null ? YearMonth.now() :
				YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
		} catch (Exception e) {
			throw new OkrApplicationException(ErrorCode.INVALID_YEARMONTH_FORMAT);
		}
	}
}

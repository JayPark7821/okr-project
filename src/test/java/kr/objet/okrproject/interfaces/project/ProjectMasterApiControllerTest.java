package kr.objet.okrproject.interfaces.project;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.objet.okrproject.application.project.ProjectFacade;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.config.WithMockCustomUser;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.user.User;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(controllers = ProjectMasterApiController.class)
class ProjectMasterApiControllerTest {

	private final String url = "/api/v1/project";
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	MockMvc mvc;
	@MockBean
	private ProjectFacade projectFacade;

	@Test
	@WithMockCustomUser(seq = 1L, email = "test@test.com")
	void 프로젝트_등록_성공() throws Exception {
		//given
		ProjectMasterDto.Save projectSaveDto = ProjectSaveDtoFixture.create(
			ProjectSaveDtoFixture.getDateString(-5, "yyyy-MM-dd"),
			ProjectSaveDtoFixture.getDateString(0, "yyyy-MM-dd"),
			3,
			3
		);
		//when
		mvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsBytes(projectSaveDto))
				.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isCreated());

		//then
		then(projectFacade)
			.should(times(1))
			.registerProject(any(ProjectMasterCommand.RegisterProjectMaster.class), any(User.class));
	}

	@Test
	void 프로젝트_등록_실패_권한없음() throws Exception {
		//given
		ProjectMasterDto.Save projectSaveDto = ProjectSaveDtoFixture.create(
			ProjectSaveDtoFixture.getDateString(-5, "yyyy-MM-dd"),
			ProjectSaveDtoFixture.getDateString(0, "yyyy-MM-dd"),
			3,
			3
		);
		//when

		mvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsBytes(projectSaveDto))
				.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockCustomUser(seq = 1L, email = "test@test.com")
	void 프로젝트_등록_실패_keyResult_3개_초과() throws Exception {
		//given
		ProjectMasterDto.Save projectSaveDto = ProjectSaveDtoFixture.create(
			ProjectSaveDtoFixture.getDateString(-5, "yyyy-MM-dd"),
			ProjectSaveDtoFixture.getDateString(0, "yyyy-MM-dd"),
			5,
			5
		);
		//when
		mvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsBytes(projectSaveDto))
				.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockCustomUser(seq = 1L, email = "test@test.com")
	void 프로젝트_조회_성공() throws Exception {
		//given
		SortType sort = SortType.RECENTLY_CREATE;
		String includeFinishedProjectYN = "Y";
		given(projectFacade.retrieveProject(eq(sort), eq(includeFinishedProjectYN), any(User.class),
			any(Pageable.class))).
			willReturn(mock(Page.class));
		//when
		MvcResult mvcResult = mvc.perform(
				get(url + "?sortType=" + sort.getCode() + "&includeFinishedProjectYN=" + includeFinishedProjectYN)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();
	}

	@Test
	@WithMockCustomUser(seq = 1L, email = "test@test.com")
	void 프로젝트_조회_실패_종료프로젝트_검색조건_오류() throws Exception {
		//given
		SortType sort = SortType.RECENTLY_CREATE;
		//when
		MvcResult mvcResult = mvc.perform(get(url + "?sortType=" + sort.getCode() + "&includeFinishedProjectYN=a")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andReturn();

		//then
		assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
			.contains(ErrorCode.INVALID_FINISHED_RPOJECT_YN.getMessage());

	}

	@Test
	@WithMockCustomUser(seq = 1L, email = "test@test.com")
	void 프로젝트_조회_실패_정렬타입_검색조건_오류() throws Exception {
		//given

		//when
		MvcResult mvcResult = mvc.perform(get(url + "?sortType=" + "gereytfff" + "&includeFinishedProjectYN=Y")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andReturn();

		//then
		assertThat(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8))
			.contains(ErrorCode.INVALID_SORT_TYPE.getMessage());
	}

}
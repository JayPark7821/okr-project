package kr.objet.okrproject.interfaces.project;

import static kr.objet.okrproject.interfaces.project.ProjectSaveDtoFixture.*;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.objet.okrproject.application.project.ProjectFacade;
import kr.objet.okrproject.config.WithMockCustomUser;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.user.User;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(controllers = ProjectMasterApiController.class)
class ProjectMasterApiTest {

	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	MockMvc mvc;
	@MockBean
	private ProjectFacade projectFacade;
	private final String url = "/api/v1/project";

	@Test
	@WithMockCustomUser(seq = 1L, email = "test@test.com")
	void 프로젝트_등록_성공() throws Exception {
		//given
		ProjectSaveDto projectSaveDto = getProjectSaveDto(5, 0, 3, 3, "yyyy-MM-dd");

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
		ProjectSaveDto projectSaveDto = getProjectSaveDto(5, 0, 3, 3, "yyyy-MM-dd");

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
		ProjectSaveDto projectSaveDto = getProjectSaveDto(5, 0, 5, 5, "yyyy-MM-dd");

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
	void 프로젝트_등록_실패_프로젝트_종료일이_오늘이전() throws Exception {
		//given
		ProjectSaveDto projectSaveDto = getProjectSaveDto(5, 1, 0, 3, "yyyy-MM-dd");

		//when
		MvcResult mvcResult = mvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsBytes(projectSaveDto))
				.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andReturn();
		//then
		assertThat(mvcResult.getResponse().getContentAsString()).contains("종료 일짜가 지난 프로젝트입니다.");
	}

}
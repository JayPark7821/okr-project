package kr.objet.okrproject.interfaces.project;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.objet.okrproject.application.project.ProjectFacade;
import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.config.WithMockCustomUser;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.user.User;

@WebMvcTest(controllers = ProjectMasterApiController.class)
@AutoConfigureMockMvc()
class ProjectMasterApiTest {

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
		ProjectSaveDto projectSaveDto = ProjectSaveDtoFixture.create("2022-01-12", "2022-12-30");
		User user = UserFixture.create();
		//when
		mvc.perform(post("/api/v1/project")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsBytes(projectSaveDto))
				.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isOk());

		//then
		then(projectFacade)
			.should(times(1))
			.registerProject(any(ProjectMasterCommand.RegisterProjectMaster.class), any(User.class));
	}

}
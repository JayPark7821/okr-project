package kr.objet.okrproject.interfaces.team;

import static org.mockito.ArgumentMatchers.*;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.objet.okrproject.application.team.ProjectTeamMemberFacade;
import kr.objet.okrproject.config.WithMockCustomUser;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.interfaces.project.ProjectMasterApiController;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(controllers = ProjectMasterApiController.class)
class ProjectTeamMemberApiControllerTest {

	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	MockMvc mvc;
	@MockBean
	private ProjectTeamMemberFacade projectTeamMemberFacade;
	private final String url = "/api/v1/team";

	@Test
	@WithMockCustomUser(seq = 1L, email = "test@test.com")
	void 팀원초대_성공() throws Exception {
		//given
		ProjectTeamMemberDto.saveRequest requestDto = ProjectTeamMemberSaveDtoFixture.create();

		//when
		mvc.perform(post(url + "/invite")
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsBytes(requestDto))
				.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isCreated());

		//then
		then(projectTeamMemberFacade)
			.should(times(1))
			.inviteTeamMembers(any(ProjectTeamMemberCommand.RegisterProjectTeamMember.class), any(User.class));
	}

}
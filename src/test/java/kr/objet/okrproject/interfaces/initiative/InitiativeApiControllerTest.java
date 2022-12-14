package kr.objet.okrproject.interfaces.initiative;

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

import kr.objet.okrproject.application.initiative.InitiativeFacade;
import kr.objet.okrproject.config.WithMockCustomUser;
import kr.objet.okrproject.domain.initiative.service.InitiativeCommand;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.keyresult.service.fixture.KeyResultFixture;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.interfaces.project.ProjectSaveDtoFixture;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(controllers = InitiativeApiController.class)
class InitiativeApiControllerTest {

	private final String url = "/api/v1/initiative";
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	MockMvc mvc;
	@MockBean
	private InitiativeFacade initiativeFacade;

	@Test
	@WithMockCustomUser(seq = 1L, email = "test@test.com")
	void initiative_등록_성공() throws Exception {
		//given
		String initiativeName = "ini name";
		String initiativeDetail = "initiative detail";
		KeyResult keyResult = KeyResultFixture.create();
		InitiativeDto.Save dto = InitiativeDto.Save.builder()
			.keyResultToken(keyResult.getKeyResultToken())
			.edt(ProjectSaveDtoFixture.getDateString(-6, "yyyy-MM-dd"))
			.sdt(ProjectSaveDtoFixture.getDateString(-10, "yyyy-MM-dd"))
			.detail(initiativeDetail)
			.name(initiativeName)
			.build();

		//when
		mvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsBytes(dto))
				.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isCreated());

		//then
		then(initiativeFacade)
			.should(times(1))
			.registerInitiative(any(InitiativeCommand.registerInitiative.class), any(User.class));
	}

}
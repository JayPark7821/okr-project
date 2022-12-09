package kr.objet.okrproject.integration;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.objet.okrproject.common.utils.JwtTokenUtils;
import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.infrastructure.keyresult.ProjectKeyResultRepository;
import kr.objet.okrproject.infrastructure.project.ProjectMasterRepository;
import kr.objet.okrproject.infrastructure.team.ProjectTeamMemberRepository;
import kr.objet.okrproject.infrastructure.user.UserRepository;
import kr.objet.okrproject.interfaces.project.ProjectSaveDto;
import kr.objet.okrproject.interfaces.project.ProjectSaveDtoFixture;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProjectMasterIntegrationTest {

	@Autowired
	private ProjectMasterRepository projectMasterRepository;
	@Autowired
	private ProjectTeamMemberRepository projectTeamMemberRepository;
	@Autowired
	private ProjectKeyResultRepository projectKeyResultRepository;
	@Autowired
	private UserRepository userRepository;

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.token.access-expired-time-ms}")
	private Long expiredTimeMs;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	private MockMvc mvc;

	private final String url = "/api/v1/project";

	@Test
	void 프로젝트_등록_성공() throws Exception {
		//given
		//TODO : 통합테스트시 인증인가 처리 방법
		User user = userRepository.findUserByEmail("user1673@naver.com").get();
		String token = JwtTokenUtils.generateToken(user.getEmail(), secretKey, expiredTimeMs);
		int keyResultSize = 3;

		ProjectSaveDto dto = ProjectSaveDtoFixture.getProjectSaveDto(5, 0, keyResultSize, keyResultSize, "yyyy-MM-dd");

		//when
		MvcResult mvcResult = mvc.perform(post(url)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsBytes(dto))
			)
			.andDo(print())
			.andExpect(status().isCreated())
			.andReturn();

		//then
		//TODO : 어떤식으로 검증???? Response객체
		Response response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Response.class);

		Optional<ProjectMaster> projectMaster = projectMasterRepository
			.findByProjectMasterToken(response.getResult());
		assertThat(projectMaster.isEmpty()).isFalse();
		assertThat(projectMaster.get().getName()).isEqualTo(dto.getName());

		List<ProjectTeamMember> teamMember = projectTeamMemberRepository
			.findProjectTeamMembersByUser(user);
		assertThat(teamMember.size() > 0).isTrue();
		// TODO : 저장요청한 projectMaster랑 실제 db에서 가져온 projectMaster 같은지 비교하려면 테스트 코드를 위해서 레파지토리의 쿼리를 fetch join으로 변경해야하는게 맞는지...
		assertThat(teamMember.get(0).getProjectRoleType()).isEqualTo(ProjectRoleType.LEADER);
		assertThat(teamMember.get(0).isNew()).isTrue();

		List<ProjectKeyResult> keyResult = projectKeyResultRepository
			.findProjectKeyResultsByProjectMaster(projectMaster.get());
		assertThat(keyResult.size() == keyResultSize).isTrue();
		for (ProjectKeyResult projectKeyResult : keyResult) {
			assertThat(dto.getKeyResults()).contains(projectKeyResult.getName());
		}
	}
}
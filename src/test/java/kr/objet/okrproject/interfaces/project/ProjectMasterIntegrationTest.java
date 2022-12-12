package kr.objet.okrproject.interfaces.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.utils.JwtTokenUtils;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.infrastructure.initiative.InitiativeRepository;
import kr.objet.okrproject.infrastructure.keyresult.KeyResultRepository;
import kr.objet.okrproject.infrastructure.project.ProjectMasterRepository;
import kr.objet.okrproject.infrastructure.team.TeamMemberRepository;
import kr.objet.okrproject.infrastructure.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class ProjectMasterIntegrationTest {

	private final String ProjectUrl = "/api/v1/project";

	private final String keyResultUrl = "/api/v1/keyresult";
	private final String initiativeUrl = "/api/v1/initiative";
	private final String projectLeaderEmail = "projectMasterTest@naver.com";
	private final String projectSdt = ProjectSaveDtoFixture.getDateString(-5, "yyyy-MM-dd");
	private final String projectEdt = ProjectSaveDtoFixture.getDateString(2, "yyyy-MM-dd");
	private final List<KeyResult> keyResults = new ArrayList<>();
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	private ProjectMasterRepository projectMasterRepository;
	@Autowired
	private TeamMemberRepository teamMemberRepository;
	@Autowired
	private KeyResultRepository keyResultRepository;
	@Autowired
	private InitiativeRepository initiativeRepository;
	@Autowired
	private UserRepository userRepository;
	@Value("${jwt.secret-key}")
	private String secretKey;
	@Value("${jwt.token.access-expired-time-ms}")
	private Long expiredTimeMs;
	@Autowired
	private MockMvc mvc;
	private User user;
	private String token;
	private ProjectMaster projectMaster;

	@BeforeEach
	void init() {
		if (Objects.isNull(user)) {
			//TODO : 통합테스트시 인증 처리 방법
			user = userRepository.findUserByEmail(projectLeaderEmail).get();
			token = JwtTokenUtils.generateToken(user.getEmail(), secretKey, expiredTimeMs);
		}
	}

	@Order(1)
	@Test
	void 프로젝트_등록_성공() throws Exception {
		//given
		int keyResultSize = 3;
		ProjectSaveDto dto = ProjectSaveDtoFixture.create(projectSdt, projectEdt, keyResultSize, keyResultSize);

		//when
		MvcResult mvcResult = mvc.perform(post(ProjectUrl)
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
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		String projectToken = jsonNode.get("result").asText();

		// ProjectMaster 검증
		Optional<ProjectMaster> projectMaster = projectMasterRepository
			.findByProjectMasterToken(projectToken);
		assertThat(projectMaster.isEmpty()).isFalse();
		assertThat(projectMaster.get().getName()).isEqualTo(dto.getName());

		this.projectMaster = projectMaster.get();

		// TeamMember 검증
		List<TeamMember> teamMember = teamMemberRepository
			.findProjectTeamMembersByUser(user);
		assertThat(teamMember.size() > 0).isTrue();
		// TODO : 저장요청한 projectMaster랑 실제 db에서 가져온 projectMaster 같은지 비교하려면 테스트 코드를 위해서 레파지토리의 쿼리를 fetch join으로 변경해야하는게 맞는지...
		assertThat(teamMember.get(0).getProjectRoleType()).isEqualTo(ProjectRoleType.LEADER);
		assertThat(teamMember.get(0).isNew()).isTrue();

		// KeyResult 검증
		List<KeyResult> keyResult = keyResultRepository
			.findProjectKeyResultsByProjectMaster(projectMaster.get());
		assertThat(keyResult.size() == keyResultSize).isTrue();
		for (KeyResult projectKeyResult : keyResult) {
			assertThat(dto.getKeyResults()).contains(projectKeyResult.getName());
			keyResults.add(projectKeyResult);
		}
	}

	@Test
	void 프로젝트_등록_실패_로그인X() throws Exception {
		//given
		int keyResultSize = 3;
		ProjectSaveDto dto = ProjectSaveDtoFixture.create(projectSdt, projectEdt, keyResultSize, keyResultSize);

		//when
		MvcResult mvcResult = mvc.perform(post(ProjectUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsBytes(dto))
			)
			.andDo(print())
			.andExpect(status().isUnauthorized())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		assertThat(jsonNode.get("message").asText()).isEqualTo(ErrorCode.INVALID_TOKEN.getMessage());
	}

	@Test
	void 프로젝트_등록_실패_keyResult_3개_이상() throws Exception {
		//given
		int keyResultSize = 4;
		ProjectSaveDto dto = ProjectSaveDtoFixture.create(projectSdt, projectEdt, keyResultSize, keyResultSize);

		//when
		MvcResult mvcResult = mvc.perform(post(ProjectUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsBytes(dto))
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		assertThat(jsonNode.get("message").asText()).contains("Key Result는 3개 까지만 등록이 가능합니다.");
	}

	@Test
	void 프로젝트_등록_실패_날짜_포멧_오류() throws Exception {
		//given
		int keyResultSize = 3;

		ProjectSaveDto dto = ProjectSaveDtoFixture.create(
			ProjectSaveDtoFixture.getDateString(-5, "yyyyMMdd"),
			ProjectSaveDtoFixture.getDateString(0, "yyyyMMdd"),
			keyResultSize,
			keyResultSize
		);

		//when
		MvcResult mvcResult = mvc.perform(post(ProjectUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsBytes(dto))
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		assertThat(jsonNode.get("message").asText()).contains("8자리의 yyyy-MM-dd 형식이어야 합니다.");
	}

	@Test
	void 프로젝트_등록_실패_프로젝트_종료날짜_오늘이전() throws Exception {
		//given
		int keyResultSize = 3;
		ProjectSaveDto dto = ProjectSaveDtoFixture.create(
			ProjectSaveDtoFixture.getDateString(-5, "yyyy-MM-dd"),
			ProjectSaveDtoFixture.getDateString(-3, "yyyy-MM-dd"),
			keyResultSize,
			keyResultSize
		);

		//when
		MvcResult mvcResult = mvc.perform(post(ProjectUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsBytes(dto))
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		assertThat(jsonNode.get("message").asText()).contains(ErrorCode.INVALID_PROJECT_END_DATE.getMessage());
	}

	@Test
	void 프로젝트_등록_실패_프로젝트_종료날짜가_시작일짜_이전() throws Exception {
		//given
		int keyResultSize = 3;
		ProjectSaveDto dto = ProjectSaveDtoFixture.create(
			ProjectSaveDtoFixture.getDateString(-1, "yyyy-MM-dd"),
			ProjectSaveDtoFixture.getDateString(-3, "yyyy-MM-dd"),
			keyResultSize,
			keyResultSize
		);

		//when
		MvcResult mvcResult = mvc.perform(post(ProjectUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsBytes(dto))
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andReturn();

		//then

		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		assertThat(jsonNode.get("message").asText()).contains(ErrorCode.INVALID_PROJECT_SDT_EDT.getMessage());
	}


}
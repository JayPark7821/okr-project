package kr.objet.okrproject.integration;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;
import kr.objet.okrproject.interfaces.team.ProjectTeamMemberDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.objet.okrproject.common.exception.ErrorCode;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

	private User user;
	private String token;
	private final String ProjectUrl = "/api/v1/project";
	private final String ProjectTeamUrl = "/api/v1/team";
	private final String projectLeaderEmail = "user1671@naver.com";
	private String projectToken;

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
		ProjectSaveDto dto = ProjectSaveDtoFixture.getProjectSaveDto(5, 0, keyResultSize, keyResultSize, "yyyy-MM-dd");

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
		projectToken = jsonNode.get("result").asText();

		Optional<ProjectMaster> projectMaster = projectMasterRepository
			.findByProjectMasterToken(projectToken);
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

	@Test
	void 프로젝트_등록_실패_로그인X() throws Exception {
		//given
		int keyResultSize = 3;
		ProjectSaveDto dto = ProjectSaveDtoFixture.getProjectSaveDto(5, 0, keyResultSize, keyResultSize, "yyyy-MM-dd");

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
		ProjectSaveDto dto = ProjectSaveDtoFixture.getProjectSaveDto(5, 0, keyResultSize, keyResultSize, "yyyy-MM-dd");

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
		ProjectSaveDto dto = ProjectSaveDtoFixture.getProjectSaveDto(5, 0, keyResultSize, keyResultSize, "yyyyMMdd");

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
		ProjectSaveDto dto = ProjectSaveDtoFixture.getProjectSaveDto(5, 3, keyResultSize, keyResultSize, "yyyy-MM-dd");

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
		ProjectSaveDto dto = ProjectSaveDtoFixture.getProjectSaveDto(1, 3, keyResultSize, keyResultSize, "yyyy-MM-dd");

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

	@Order(2)
	@Test
	void 팀원_초대_성공() throws Exception {
		//given
		ProjectTeamMemberDto.saveRequest dto =
				new ProjectTeamMemberDto.saveRequest(projectToken, List.of("user1671@naver.com", "user1342@naver.com"));
		//when
		MvcResult mvcResult = mvc.perform(post(ProjectTeamUrl+"/invite")
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding(StandardCharsets.UTF_8)
						.content(objectMapper.writeValueAsBytes(dto))
				)
				.andDo(print())
				.andExpect(status().isCreated())
				.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		System.out.println("jsonNode = " + jsonNode);

	}
}
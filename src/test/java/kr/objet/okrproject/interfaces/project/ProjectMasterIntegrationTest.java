package kr.objet.okrproject.interfaces.project;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.utils.JwtTokenUtils;
import kr.objet.okrproject.domain.initiative.Initiative;
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
import kr.objet.okrproject.interfaces.initiative.InitiativeSaveDto;
import kr.objet.okrproject.interfaces.keyresult.KeyResultSaveDto;
import kr.objet.okrproject.interfaces.project.ProjectSaveDto;
import kr.objet.okrproject.interfaces.project.ProjectSaveDtoFixture;
import kr.objet.okrproject.interfaces.team.TeamMemberDto;

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



	@Order(6)
	@Test
	void keyResult_등록_성공() throws Exception {
		//given
		KeyResultSaveDto dto = KeyResultSaveDto.builder()
			.name("testKeyResult")
			.projectToken(projectMaster.getProjectMasterToken())
			.build();
		//when
		MvcResult mvcResult = mvc.perform(post(keyResultUrl)
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
		String keyResultToken = jsonNode.get("result").asText();
		KeyResult savedKeyResult = keyResultRepository.findByKeyResultToken(keyResultToken).orElseThrow();
		assertThat(savedKeyResult.getName()).isEqualTo(dto.getName());
	}

	@Order(7)
	@Test
	void keyResult_등록_실패_요청_유저가_해당_프로젝트에_맴버X() throws Exception {
		//given
		KeyResultSaveDto dto = KeyResultSaveDto.builder()
			.name("testKeyResult")
			.projectToken(projectMaster.getProjectMasterToken())
			.build();

		String token = JwtTokenUtils.generateToken("user3@naver.com", secretKey, expiredTimeMs);

		//when
		MvcResult mvcResult = mvc.perform(post(keyResultUrl)
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
		String message = jsonNode.get("message").toString();
		assertThat(message).contains(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());
	}

	@Order(8)
	@Test
	void initiative_등록_성공() throws Exception {
		// given
		String initiativeName = "ini name";
		String initiativeDetail = "initiative detail";
		InitiativeSaveDto dto = InitiativeSaveDto.builder()
			.keyResultToken(keyResults.get(0).getKeyResultToken())
			.edt(projectEdt)
			.sdt(projectSdt)
			.detail(initiativeDetail)
			.name(initiativeName)
			.build();

		//when
		MvcResult mvcResult = mvc.perform(post(initiativeUrl)
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
		String initiativeToken = jsonNode.get("result").asText();

		Initiative initiative = initiativeRepository.findByInitiativeToken(initiativeToken).orElseThrow();
		assertThat(initiative.getName()).isEqualTo(initiativeName);
		assertThat(initiative.getDetail()).isEqualTo(initiativeDetail);
	}

	@Order(8)
	@Test
	void initiative_등록_실패_프로젝트참여자X() throws Exception {
		// given
		String initiativeName = "ini name";
		String initiativeDetail = "initiative detail";
		InitiativeSaveDto dto = InitiativeSaveDto.builder()
			.keyResultToken(keyResults.get(0).getKeyResultToken())
			.edt(projectEdt)
			.sdt(projectSdt)
			.detail(initiativeDetail)
			.name(initiativeName)
			.build();

		String token = JwtTokenUtils.generateToken("user3@naver.com", secretKey, expiredTimeMs);

		//when
		MvcResult mvcResult = mvc.perform(post(initiativeUrl)
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
		String message = jsonNode.get("message").asText();
		assertThat(message).isEqualTo(ErrorCode.INVALID_KEYRESULT_TOKEN.getMessage());

	}

	@Order(8)
	@Test
	void initiative_등록_실패_마감일이_오늘_이전() throws Exception {

		// given
		String initiativeName = "ini name";
		String initiativeDetail = "initiative detail";
		InitiativeSaveDto dto = InitiativeSaveDto.builder()
			.keyResultToken(keyResults.get(0).getKeyResultToken())
			.edt(ProjectSaveDtoFixture.getDateString(-1, "yyyy-MM-dd"))
			.sdt(projectSdt)
			.detail(initiativeDetail)
			.name(initiativeName)
			.build();

		//when
		MvcResult mvcResult = mvc.perform(post(initiativeUrl)
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
		String message = jsonNode.get("message").asText();
		assertThat(message).isEqualTo(ErrorCode.INVALID_END_DATE_FOR_INITIATIVE.getMessage());

	}

	@Order(8)
	@Test
	void initiative_등록_실패_마감일이_시작일_이전() throws Exception {
		// given
		String initiativeName = "ini name";
		String initiativeDetail = "initiative detail";
		InitiativeSaveDto dto = InitiativeSaveDto.builder()
			.keyResultToken(keyResults.get(0).getKeyResultToken())
			.edt(ProjectSaveDtoFixture.getDateString(0, "yyyy-MM-dd"))
			.sdt(ProjectSaveDtoFixture.getDateString(1, "yyyy-MM-dd"))
			.detail(initiativeDetail)
			.name(initiativeName)
			.build();

		//when
		MvcResult mvcResult = mvc.perform(post(initiativeUrl)
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
		String message = jsonNode.get("message").asText();
		assertThat(message).isEqualTo(ErrorCode.INVALID_END_DATE_FOR_INITIATIVE_SDT.getMessage());

	}

	@Order(8)
	@Test
	void initiative_등록_실패_마감일이_프로젝트_기간_사이X() throws Exception {
		// given
		String initiativeName = "ini name";
		String initiativeDetail = "initiative detail";
		InitiativeSaveDto dto = InitiativeSaveDto.builder()
			.keyResultToken(keyResults.get(0).getKeyResultToken())
			.edt(ProjectSaveDtoFixture.getDateString(-6, "yyyy-MM-dd"))
			.sdt(ProjectSaveDtoFixture.getDateString(-10, "yyyy-MM-dd"))
			.detail(initiativeDetail)
			.name(initiativeName)
			.build();

		//when
		MvcResult mvcResult = mvc.perform(post(initiativeUrl)
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
		String message = jsonNode.get("message").asText();
		assertThat(message).isEqualTo(ErrorCode.INVALID_INITIATIVE_END_DATE.getMessage());

	}

}
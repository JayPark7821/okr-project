package kr.objet.okrproject.interfaces.project;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProjectMasterIntegrationTest {

	private final String ProjectUrl = "/api/v1/project";

	private final String keyResultUrl = "/api/v1/keyresult";
	private final String initiativeUrl = "/api/v1/initiative";
	private final String projectSdt = ProjectSaveDtoFixture.getDateString(-5, "yyyy-MM-dd");
	private final String projectEdt = ProjectSaveDtoFixture.getDateString(2, "yyyy-MM-dd");

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
	private String saveTestToken;
	private String retrieveTestToken;
	private String calendarTestToken;
	private ProjectMaster projectMaster;

	@BeforeEach
	void init() {
		if (Objects.isNull(user)) {
			String projectLeaderEmail = "projectMasterTest@naver.com";
			String retrieveTestUser = "projectMasterRetrieveTest@naver.com";
			String calendarTestUser = "projectCalendarTest@naver.com";
			saveTestToken = JwtTokenUtils.generateToken(projectLeaderEmail, secretKey, expiredTimeMs);
			retrieveTestToken = JwtTokenUtils.generateToken(retrieveTestUser, secretKey, expiredTimeMs);
			calendarTestToken = JwtTokenUtils.generateToken(calendarTestUser, secretKey, expiredTimeMs);

		}
	}

	@Test
	void 프로젝트_등록_성공() throws Exception {
		//given
		int keyResultSize = 3;
		ProjectMasterDto.Save dto = ProjectSaveDtoFixture.create(projectSdt, projectEdt, keyResultSize, keyResultSize);

		//when
		MvcResult mvcResult = mvc.perform(post(ProjectUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + saveTestToken)
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
		ProjectMaster projectMaster = projectMasterRepository
			.findByProjectMasterToken(projectToken).orElseThrow();
		assertThat(projectMaster.getName()).isEqualTo(dto.getName());

		// TeamMember 검증
		TeamMember teamMember = teamMemberRepository
			.findByProjectMasterAndUser(projectMaster, "projectMasterTest@naver.com").orElseThrow();
		// TODO : 저장요청한 projectMaster랑 실제 db에서 가져온 projectMaster 같은지 비교하려면 테스트 코드를 위해서 레파지토리의 쿼리를 fetch join으로 변경해야하는게 맞는지...
		assertThat(teamMember.getProjectRoleType()).isEqualTo(ProjectRoleType.LEADER);
		assertThat(teamMember.isNew()).isTrue();

		// KeyResult 검증
		List<KeyResult> keyResult = keyResultRepository
			.findProjectKeyResultsByProjectMaster(projectMaster);
		assertThat(keyResult.size() == keyResultSize).isTrue();
		for (KeyResult projectKeyResult : keyResult) {
			assertThat(dto.getKeyResults()).contains(projectKeyResult.getName());
		}
	}

	@Test
	void 프로젝트_등록_실패_로그인X() throws Exception {
		//given
		int keyResultSize = 3;
		ProjectMasterDto.Save dto = ProjectSaveDtoFixture.create(projectSdt, projectEdt, keyResultSize, keyResultSize);

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
		ProjectMasterDto.Save dto = ProjectSaveDtoFixture.create(projectSdt, projectEdt, keyResultSize, keyResultSize);

		//when
		MvcResult mvcResult = mvc.perform(post(ProjectUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + saveTestToken)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.content(objectMapper.writeValueAsBytes(dto))
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		assertThat(jsonNode.get("message").asText()).contains("Key Result는 1~3개 까지만 등록이 가능합니다.");
	}

	@Test
	void 프로젝트_등록_실패_날짜_포멧_오류() throws Exception {
		//given
		int keyResultSize = 3;

		ProjectMasterDto.Save dto = ProjectSaveDtoFixture.create(
			ProjectSaveDtoFixture.getDateString(-5, "yyyyMMdd"),
			ProjectSaveDtoFixture.getDateString(0, "yyyyMMdd"),
			keyResultSize,
			keyResultSize
		);

		//when
		MvcResult mvcResult = mvc.perform(post(ProjectUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + saveTestToken)
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
		ProjectMasterDto.Save dto = ProjectSaveDtoFixture.create(
			ProjectSaveDtoFixture.getDateString(-5, "yyyy-MM-dd"),
			ProjectSaveDtoFixture.getDateString(-3, "yyyy-MM-dd"),
			keyResultSize,
			keyResultSize
		);

		//when
		MvcResult mvcResult = mvc.perform(post(ProjectUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + saveTestToken)
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
		ProjectMasterDto.Save dto = ProjectSaveDtoFixture.create(
			ProjectSaveDtoFixture.getDateString(-1, "yyyy-MM-dd"),
			ProjectSaveDtoFixture.getDateString(-3, "yyyy-MM-dd"),
			keyResultSize,
			keyResultSize
		);

		//when
		MvcResult mvcResult = mvc.perform(post(ProjectUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + saveTestToken)
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

	@Test
	void 프로젝트_조회_성공_완료된_프로젝트_포함_생성일자_최신순() throws Exception {
		//given
		SortType sort = SortType.RECENTLY_CREATE;
		//when

		MvcResult mvcResult = mvc.perform(
				get(ProjectUrl + "?sortType=" + sort.getCode() + "&includeFinishedProjectYN=Y")
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + retrieveTestToken)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		JsonNode contents = jsonNode.get("result").get("content");
		assertThat(contents.size()).isEqualTo(5);
		assertThat(contents.get(0).get("name").asText()).isEqualTo("프로젝트 조회 테스트용 프로젝트(프로젝트 완료2)");
		assertThat(contents.get(1).get("name").asText()).isEqualTo("프로젝트 조회 테스트용 프로젝트(프로젝트 60)");
		assertThat(contents.get(2).get("name").asText()).isEqualTo("프로젝트 조회 테스트용 프로젝트(프로젝트 70)");
		assertThat(contents.get(3).get("name").asText()).isEqualTo("프로젝트 조회 테스트용 프로젝트(프로젝트 완료)");
		assertThat(contents.get(4).get("name").asText()).isEqualTo("프로젝트 조회 테스트용 프로젝트");
	}

	@Test
	void 프로젝트_조회_성공_완료된_프로젝트_제외_진척율_LOW() throws Exception {
		//given
		SortType sort = SortType.PROGRESS_LOW;
		//when
		MvcResult mvcResult = mvc.perform(
				get(ProjectUrl + "?sortType=" + sort.getCode() + "&includeFinishedProjectYN=n")
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + retrieveTestToken)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		JsonNode contents = jsonNode.get("result").get("content");
		assertThat(contents.size()).isEqualTo(3);
		assertThat(contents.get(0).get("name").asText()).isEqualTo("프로젝트 조회 테스트용 프로젝트");
		assertThat(contents.get(1).get("name").asText()).isEqualTo("프로젝트 조회 테스트용 프로젝트(프로젝트 60)");
		assertThat(contents.get(2).get("name").asText()).isEqualTo("프로젝트 조회 테스트용 프로젝트(프로젝트 70)");
	}

	@Test
	void 프로젝트_조회_성공_완료된_프로젝트_제외_DEADLINE_CLOSE() throws Exception {
		//given
		SortType sort = SortType.DEADLINE_CLOSE;
		//when
		MvcResult mvcResult = mvc.perform(
				get(ProjectUrl + "?sortType=" + sort.getCode() + "&includeFinishedProjectYN=n")
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + retrieveTestToken)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		JsonNode contents = jsonNode.get("result").get("content");
		assertThat(contents.size()).isEqualTo(3);
		assertThat(contents.get(0).get("name").asText()).isEqualTo("프로젝트 조회 테스트용 프로젝트(프로젝트 60)");
		assertThat(contents.get(0).get("teamMembers").size()).isEqualTo(3);
		assertThat(contents.get(1).get("name").asText()).isEqualTo("프로젝트 조회 테스트용 프로젝트");
		assertThat(contents.get(2).get("name").asText()).isEqualTo("프로젝트 조회 테스트용 프로젝트(프로젝트 70)");

	}

	@Test
	void 프로젝트_조회_실패_종료프로젝트_검색조건_오류() throws Exception {
		//given
		SortType sort = SortType.RECENTLY_CREATE;
		//when
		MvcResult mvcResult = mvc.perform(
				get(ProjectUrl + "?sortType=" + sort.getCode() + "&includeFinishedProjectYN=a")
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + retrieveTestToken)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		assertThat(jsonNode.get("message").asText())
			.contains(ErrorCode.INVALID_FINISHED_RPOJECT_YN.getMessage());

	}

	@Test
	void 프로젝트_조회_실패_정렬타입_검색조건_오류() throws Exception {
		//given

		//when
		MvcResult mvcResult = mvc.perform(get(ProjectUrl + "?sortType=" + "gereytfff" + "&includeFinishedProjectYN=Y")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + retrieveTestToken)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		assertThat(jsonNode.get("message").asText())
			.contains(ErrorCode.INVALID_SORT_TYPE.getMessage());
	}

	@Test
	void 프로젝트_상세_조회_성공() throws Exception {
		//given

		//when
		MvcResult mvcResult = mvc.perform(
				get(ProjectUrl + "/mst_K42334fffrgg6421")
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + retrieveTestToken)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		JsonNode contents = jsonNode.get("result").get("content");
		System.out.println("contents = " + contents);
	}

	@Test
	void 프로젝트_상세_조회_실패_참여X() throws Exception {
		//given

		//when
		MvcResult mvcResult = mvc.perform(
				get(ProjectUrl + "/mst_Kiwqnp1Nq6lbTNn0")
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + retrieveTestToken)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		assertThat(jsonNode.get("message").asText())
			.contains(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());
	}

	@Test
	void 프로젝트_진행도_조회_성공() throws Exception {
		//given
		LocalDate endDate = LocalDate.parse("2004-12-12", DateTimeFormatter.ISO_DATE);
		long until = LocalDate.now().until(endDate, ChronoUnit.DAYS);
		//when
		MvcResult mvcResult = mvc.perform(
				get(ProjectUrl + "/mst_K42334fffrgg6421" + "/side")
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + retrieveTestToken)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		JsonNode result = jsonNode.get("result");
		assertThat(result.get("dday").asText()).contains("+" + String.valueOf(until * -1));
		assertThat(result.get("progress").asText()).isEqualTo("100.0");
	}

	@Test
	void 프로젝트_달력_조회_성공() throws Exception {
		//given
		String yearMonth = "2022-11";
		//when
		MvcResult mvcResult = mvc.perform(
				get(ProjectUrl + "/calendar/" + yearMonth)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + calendarTestToken)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		JsonNode result = jsonNode.get("result");
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0).get("name").asText()).contains("프로젝트 for 달력 2");
		assertThat(result.get(1).get("name").asText()).contains("프로젝트 for 달력 4");
	}

	@Test
	void 프로젝트_달력_조회_성공_0건() throws Exception {
		//given
		String yearMonth = "2023-11";
		//when
		MvcResult mvcResult = mvc.perform(
				get(ProjectUrl + "/calendar/" + yearMonth)
					.header(HttpHeaders.AUTHORIZATION, "Bearer " + calendarTestToken)
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8)
					.with(SecurityMockMvcRequestPostProcessors.csrf())
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		JsonNode result = jsonNode.get("result");
		assertThat(result.size()).isEqualTo(0);
	}

}
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
	void ????????????_??????_??????() throws Exception {
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
		//TODO : ??????????????? ?????????? Response??????
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		String projectToken = jsonNode.get("result").asText();

		// ProjectMaster ??????
		ProjectMaster projectMaster = projectMasterRepository
			.findByProjectMasterToken(projectToken).orElseThrow();
		assertThat(projectMaster.getName()).isEqualTo(dto.getName());

		// TeamMember ??????
		TeamMember teamMember = teamMemberRepository
			.findByProjectMasterAndUser(projectMaster, "projectMasterTest@naver.com").orElseThrow();
		// TODO : ??????????????? projectMaster??? ?????? db?????? ????????? projectMaster ????????? ??????????????? ????????? ????????? ????????? ?????????????????? ????????? fetch join?????? ????????????????????? ?????????...
		assertThat(teamMember.getProjectRoleType()).isEqualTo(ProjectRoleType.LEADER);
		assertThat(teamMember.isNew()).isTrue();

		// KeyResult ??????
		List<KeyResult> keyResult = keyResultRepository
			.findProjectKeyResultsByProjectMaster(projectMaster);
		assertThat(keyResult.size() == keyResultSize).isTrue();
		for (KeyResult projectKeyResult : keyResult) {
			assertThat(dto.getKeyResults()).contains(projectKeyResult.getName());
		}
	}

	@Test
	void ????????????_??????_??????_?????????X() throws Exception {
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
	void ????????????_??????_??????_keyResult_3???_??????() throws Exception {
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
		assertThat(jsonNode.get("message").asText()).contains("Key Result??? 1~3??? ????????? ????????? ???????????????.");
	}

	@Test
	void ????????????_??????_??????_??????_??????_??????() throws Exception {
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
		assertThat(jsonNode.get("message").asText()).contains("8????????? yyyy-MM-dd ??????????????? ?????????.");
	}

	@Test
	void ????????????_??????_??????_????????????_????????????_????????????() throws Exception {
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
	void ????????????_??????_??????_????????????_???????????????_????????????_??????() throws Exception {
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
	void ????????????_??????_??????_?????????_????????????_??????_????????????_?????????() throws Exception {
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
		assertThat(contents.get(0).get("name").asText()).isEqualTo("???????????? ?????? ???????????? ????????????(???????????? ??????2)");
		assertThat(contents.get(1).get("name").asText()).isEqualTo("???????????? ?????? ???????????? ????????????(???????????? 60)");
		assertThat(contents.get(2).get("name").asText()).isEqualTo("???????????? ?????? ???????????? ????????????(???????????? 70)");
		assertThat(contents.get(3).get("name").asText()).isEqualTo("???????????? ?????? ???????????? ????????????(???????????? ??????)");
		assertThat(contents.get(4).get("name").asText()).isEqualTo("???????????? ?????? ???????????? ????????????");
	}

	@Test
	void ????????????_??????_??????_?????????_????????????_??????_?????????_LOW() throws Exception {
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
		assertThat(contents.get(0).get("name").asText()).isEqualTo("???????????? ?????? ???????????? ????????????");
		assertThat(contents.get(1).get("name").asText()).isEqualTo("???????????? ?????? ???????????? ????????????(???????????? 60)");
		assertThat(contents.get(2).get("name").asText()).isEqualTo("???????????? ?????? ???????????? ????????????(???????????? 70)");
	}

	@Test
	void ????????????_??????_??????_?????????_????????????_??????_DEADLINE_CLOSE() throws Exception {
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
		assertThat(contents.get(0).get("name").asText()).isEqualTo("???????????? ?????? ???????????? ????????????(???????????? 60)");
		assertThat(contents.get(0).get("teamMembers").size()).isEqualTo(3);
		assertThat(contents.get(1).get("name").asText()).isEqualTo("???????????? ?????? ???????????? ????????????");
		assertThat(contents.get(2).get("name").asText()).isEqualTo("???????????? ?????? ???????????? ????????????(???????????? 70)");

	}

	@Test
	void ????????????_??????_??????_??????????????????_????????????_??????() throws Exception {
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
	void ????????????_??????_??????_????????????_????????????_??????() throws Exception {
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
	void ????????????_??????_??????_??????() throws Exception {
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
	void ????????????_??????_??????_??????_??????X() throws Exception {
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
	void ????????????_?????????_??????_??????() throws Exception {
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
	void ????????????_??????_??????_??????() throws Exception {
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
		assertThat(result.get(0).get("name").asText()).contains("???????????? for ?????? 2");
		assertThat(result.get(1).get("name").asText()).contains("???????????? for ?????? 4");
	}

	@Test
	void ????????????_??????_??????_??????_0???() throws Exception {
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
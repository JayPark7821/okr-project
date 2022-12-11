package kr.objet.okrproject.integration;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import kr.objet.okrproject.interfaces.keyresult.ProjectKeyResultSaveDto;
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
	private final String ProjectKeyResultUrl = "/api/v1/keyresult";
	private final String projectLeaderEmail = "main@naver.com";
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
		String projectToken = jsonNode.get("result").asText();

		Optional<ProjectMaster> projectMaster = projectMasterRepository
				.findByProjectMasterToken(projectToken);
		assertThat(projectMaster.isEmpty()).isFalse();
		assertThat(projectMaster.get().getName()).isEqualTo(dto.getName());
		this.projectMaster = projectMaster.get();
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
				new ProjectTeamMemberDto.saveRequest(projectMaster.getProjectMasterToken(), List.of("user1342@naver.com", "user1343@naver.com"));
		//when
		MvcResult mvcResult = mvc.perform(post(ProjectTeamUrl + "/invite")
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
		String addedEmails = jsonNode.get("result").get("addedEmailList").toString();
		List<ProjectTeamMember> teamMemberList = projectTeamMemberRepository.findTeamMembersByProjectId(this.projectMaster.getId());
		List<String> memberEmailList = teamMemberList.stream()
				.map(t -> t.getUser().getEmail())
				.collect(Collectors.toList());
		assertThat(memberEmailList).contains("user1342@naver.com", "user1343@naver.com");
		assertThat(addedEmails).contains("user1342@naver.com", "user1343@naver.com");
	}


	@Order(3)
	@Test
	void 팀원_초대_실패_리더가아님() throws Exception {
		//given
		ProjectTeamMemberDto.saveRequest dto =
				new ProjectTeamMemberDto.saveRequest(projectMaster.getProjectMasterToken(), List.of("user1342@naver.com", "user1343@naver.com"));

		String token = JwtTokenUtils.generateToken("user1342@naver.com", secretKey, expiredTimeMs);
		//when
		MvcResult mvcResult = mvc.perform(post(ProjectTeamUrl + "/invite")
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
		assertThat(jsonNode.get("message").asText()).isEqualTo(ErrorCode.USER_IS_NOT_LEADER.getMessage());

	}

	@Order(4)
	@Test
	void 팀원_초대_실패_추가된팀원이없음() throws Exception {
		//given
		ProjectTeamMemberDto.saveRequest dto =
				new ProjectTeamMemberDto.saveRequest(projectMaster.getProjectMasterToken(), List.of("user1342@naver.com", "user1343@naver.com"));


		//when
		MvcResult mvcResult = mvc.perform(post(ProjectTeamUrl + "/invite")
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
		assertThat(jsonNode.get("message").asText()).isEqualTo(ErrorCode.NO_USERS_ADDED.getMessage());

	}

	@Order(5)
	@Test
	void 팀원_초대_이메일_유효성검사_가입가능() throws Exception {
		//given
		String email = "user1344@naver.com";

		//when
		MvcResult mvcResult = mvc.perform(get(ProjectTeamUrl + "/invite/" + projectMaster.getProjectMasterToken()  +"/"+ email)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding(StandardCharsets.UTF_8)
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		String addedEmails = jsonNode.get("result").toString();
		assertThat(addedEmails).contains(email);
	}


	@Order(5)
	@Test
	void 팀원_초대_이메일_유효성검사_가입불가_이미_프로젝트에_초대된_유저() throws Exception {
		//given
		String email = "user1342@naver.com";

		//when
		MvcResult mvcResult = mvc.perform(get(ProjectTeamUrl + "/invite/" + projectMaster.getProjectMasterToken()  +"/"+ email)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding(StandardCharsets.UTF_8)
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		String message = jsonNode.get("message").toString();
		assertThat(message).contains(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());
	}

	@Order(5)
	@Test
	void 팀원_초대_이메일_유효성검사_가입불가_자기자신() throws Exception {
		//given

		//when
		MvcResult mvcResult = mvc.perform(get(ProjectTeamUrl + "/invite/" + projectMaster.getProjectMasterToken() +"/"+ projectLeaderEmail)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding(StandardCharsets.UTF_8)
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		String message = jsonNode.get("message").toString();
		assertThat(message).contains(ErrorCode.NOT_AVAIL_INVITE_MYSELF.getMessage());
	}

	@Order(5)
	@Test
	void 팀원_초대_이메일_유효성검사_가입불가_가입된_유저X() throws Exception {
		//given
		String email = "noUser@naver.com";

		//when
		MvcResult mvcResult = mvc.perform(get(ProjectTeamUrl + "/invite/" + projectMaster.getProjectMasterToken()  +"/"+ email)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.characterEncoding(StandardCharsets.UTF_8)
				)
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		String message = jsonNode.get("message").toString();
		assertThat(message).contains(ErrorCode.INVALID_USER_EMAIL.getMessage());
	}

	@Order(6)
	@Test
	void keyResult_등록_성공() throws Exception {
		//given
		ProjectKeyResultSaveDto dto = ProjectKeyResultSaveDto.builder()
				.name("testKeyResult")
				.projectToken(projectMaster.getProjectMasterToken())
				.build();
		//when
		MvcResult mvcResult = mvc.perform(post(ProjectKeyResultUrl)
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
		ProjectKeyResult savedKeyResult = projectKeyResultRepository.findByProjectKeyResultToken(keyResultToken).orElseThrow();
		assertThat(savedKeyResult.getName()).isEqualTo(dto.getName());
	}

	@Order(7)
	@Test
	void keyResult_등록_실패_요청_유저가_해당_프로젝트에_맴버X() throws Exception {
		//given
		ProjectKeyResultSaveDto dto = ProjectKeyResultSaveDto.builder()
				.name("testKeyResult")
				.projectToken(projectMaster.getProjectMasterToken())
				.build();

		String token = JwtTokenUtils.generateToken("user1344@naver.com", secretKey, expiredTimeMs);

		//when
		MvcResult mvcResult = mvc.perform(post(ProjectKeyResultUrl)
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

}
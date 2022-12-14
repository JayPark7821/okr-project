package kr.objet.okrproject.interfaces.team;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.utils.JwtTokenUtils;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.infrastructure.project.ProjectMasterRepository;
import kr.objet.okrproject.infrastructure.team.TeamMemberRepository;
import kr.objet.okrproject.infrastructure.user.UserRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TeamMemberIntegrationTest {

	private final String teamUrl = "/api/v1/team";

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProjectMasterRepository projectMasterRepository;
	@Autowired
	private TeamMemberRepository teamMemberRepository;
	@Value("${jwt.secret-key}")
	private String secretKey;
	@Value("${jwt.token.access-expired-time-ms}")
	private Long expiredTimeMs;
	@Autowired
	private MockMvc mvc;

	private final String projectLeaderEmail = "teamMemberTest@naver.com";
	private User user;
	private String token;
	private ProjectMaster projectMaster;

	@BeforeEach
	void init() {
		if (Objects.isNull(user)) {
			//TODO : 통합테스트시 인증 처리 방법
			user = userRepository.findUserByEmail(projectLeaderEmail).get();
			token = JwtTokenUtils.generateToken(user.getEmail(), secretKey, expiredTimeMs);
			projectMaster = projectMasterRepository.findByProjectMasterToken("mst_Kiwqnp1Nq6lbTNn0").orElseThrow();

		}
	}

	@Test
	void 팀원_초대_성공() throws Exception {
		//given
		TeamMemberDto.saveRequest dto =
			new TeamMemberDto.saveRequest(projectMaster.getProjectMasterToken(),
				List.of("user3@naver.com", "user4@naver.com"));
		//when
		MvcResult mvcResult = mvc.perform(post(teamUrl + "/invite")
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
		List<TeamMember> teamMemberList = teamMemberRepository.findTeamMembersByProjectId(this.projectMaster.getId());
		List<String> memberEmailList = teamMemberList.stream()
			.map(t -> t.getUser().getEmail())
			.collect(Collectors.toList());
		assertThat(memberEmailList).contains("teamMemberTest@naver.com", "user1@naver.com", "user2@naver.com",
			"user3@naver.com", "user4@naver.com");
		assertThat(addedEmails).contains("user3@naver.com", "user4@naver.com");
	}

	@Test
	void 팀원_초대_실패_리더가아님() throws Exception {
		//given
		TeamMemberDto.saveRequest dto =
			new TeamMemberDto.saveRequest(projectMaster.getProjectMasterToken(),
				List.of("user3@naver.com", "user4@naver.com"));

		String token = JwtTokenUtils.generateToken("user1@naver.com", secretKey, expiredTimeMs);
		//when
		MvcResult mvcResult = mvc.perform(post(teamUrl + "/invite")
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

	@Test
	void 팀원_초대_실패_추가된팀원이없음() throws Exception {
		//given
		TeamMemberDto.saveRequest dto =
			new TeamMemberDto.saveRequest(projectMaster.getProjectMasterToken(),
				List.of("user1@naver.com", "user2@naver.com"));

		//when
		MvcResult mvcResult = mvc.perform(post(teamUrl + "/invite")
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

	@Test
	void 팀원_초대_이메일_유효성검사_가입가능() throws Exception {
		//given
		String email = "user5@naver.com";

		//when
		MvcResult mvcResult = mvc.perform(
				get(teamUrl + "/invite/" + projectMaster.getProjectMasterToken() + "/" + email)
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

	@Test
	void 팀원_초대_이메일_유효성검사_가입불가_이미_프로젝트에_초대된_유저() throws Exception {
		//given
		String email = "user1@naver.com";

		//when
		MvcResult mvcResult = mvc.perform(
				get(teamUrl + "/invite/" + projectMaster.getProjectMasterToken() + "/" + email)
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

	@Test
	void 팀원_초대_이메일_유효성검사_가입불가_자기자신() throws Exception {
		//given

		//when
		MvcResult mvcResult = mvc.perform(
				get(teamUrl + "/invite/" + projectMaster.getProjectMasterToken() + "/" + projectLeaderEmail)
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

	@Test
	void 팀원_초대_이메일_유효성검사_가입불가_가입된_유저X() throws Exception {
		//given
		String email = "noUser@naver.com";

		//when
		MvcResult mvcResult = mvc.perform(
				get(teamUrl + "/invite/" + projectMaster.getProjectMasterToken() + "/" + email)
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
}

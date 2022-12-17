package kr.objet.okrproject.interfaces.initiative;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.utils.JwtTokenUtils;
import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.infrastructure.initiative.InitiativeRepository;
import kr.objet.okrproject.infrastructure.project.ProjectMasterRepository;
import kr.objet.okrproject.infrastructure.user.UserRepository;
import kr.objet.okrproject.interfaces.project.ProjectSaveDtoFixture;
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

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InitiativeIntegrationTest {

	private final String initiativeUrl = "/api/v1/initiative";

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProjectMasterRepository projectMasterRepository;
	@Autowired
	private InitiativeRepository initiativeRepository;
	@Value("${jwt.secret-key}")
	private String secretKey;
	@Value("${jwt.token.access-expired-time-ms}")
	private Long expiredTimeMs;
	@Autowired
	private MockMvc mvc;

	private User user;
	private String projectLeaderToken;
	private String initiativeToken;
	private String projectStartDt = "2000-12-12";
	private String projectEndDt = "2023-12-14";
	private String keyResultToken = "key_325fdggrtQ25zQMs";

	@BeforeEach
	void init() {
		if (Objects.isNull(user)) {
			//TODO : 통합테스트시 인증 처리 방법
			String projectLeaderEmail = "initiativeTest@naver.com";
			String initiativeTestEmail = "initiativeRetrieveTest@naver.com";

			projectLeaderToken = JwtTokenUtils.generateToken(projectLeaderEmail, secretKey, expiredTimeMs);
			initiativeToken = JwtTokenUtils.generateToken(initiativeTestEmail, secretKey, expiredTimeMs);
		}
	}

	@Test
	void initiative_등록_성공() throws Exception {
		// given
		String initiativeName = "ini name";
		String initiativeDetail = "initiative detail";
		InitiativeDto.Save dto = InitiativeDto.Save.builder()
			.keyResultToken(keyResultToken)
			.edt(projectEndDt)
			.sdt(projectStartDt)
			.detail(initiativeDetail)
			.name(initiativeName)
			.build();

		//when
		MvcResult mvcResult = mvc.perform(post(initiativeUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + projectLeaderToken)
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

		ProjectMaster projectMaster = projectMasterRepository.findByProjectMasterToken("mst_K4e8a5s7d6lb6421").get();
		assertThat(projectMaster.getProgress()).isEqualTo(50.0D);

	}

	@Test
	void initiative_등록_실패_프로젝트참여자X() throws Exception {
		// given
		String initiativeName = "ini name";
		String initiativeDetail = "initiative detail";
		InitiativeDto.Save dto = InitiativeDto.Save.builder()
			.keyResultToken(keyResultToken)
			.edt(projectEndDt)
			.sdt(projectStartDt)
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

	@Test
	void initiative_등록_실패_마감일이_오늘_이전() throws Exception {

		// given
		String initiativeName = "ini name";
		String initiativeDetail = "initiative detail";
		InitiativeDto.Save dto = InitiativeDto.Save.builder()
			.keyResultToken(keyResultToken)
			.edt(ProjectSaveDtoFixture.getDateString(-1, "yyyy-MM-dd"))
			.sdt(projectStartDt)
			.detail(initiativeDetail)
			.name(initiativeName)
			.build();

		//when
		MvcResult mvcResult = mvc.perform(post(initiativeUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + projectLeaderToken)
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

	@Test
	void initiative_등록_실패_마감일이_시작일_이전() throws Exception {
		// given
		String initiativeName = "ini name";
		String initiativeDetail = "initiative detail";
		InitiativeDto.Save dto = InitiativeDto.Save.builder()
			.keyResultToken(keyResultToken)
			.edt(ProjectSaveDtoFixture.getDateString(0, "yyyy-MM-dd"))
			.sdt(ProjectSaveDtoFixture.getDateString(1, "yyyy-MM-dd"))
			.detail(initiativeDetail)
			.name(initiativeName)
			.build();

		//when
		MvcResult mvcResult = mvc.perform(post(initiativeUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + projectLeaderToken)
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

	@Test
	void initiative_등록_실패_마감일이_프로젝트_기간_사이X() throws Exception {
		// given
		String initiativeName = "ini name";
		String initiativeDetail = "initiative detail";
		InitiativeDto.Save dto = InitiativeDto.Save.builder()
			.keyResultToken(keyResultToken)
			.edt("2026-12-14")
			.sdt("2000-12-31")
			.detail(initiativeDetail)
			.name(initiativeName)
			.build();

		//when
		MvcResult mvcResult = mvc.perform(post(initiativeUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + projectLeaderToken)
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

	@Test
	void initiative_조회_2건() throws Exception {
		// given
		String keyResultToken = "key_t433t43fdQ25fzQMs";

		//when
		MvcResult mvcResult = mvc.perform(get(initiativeUrl + "/" + keyResultToken)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + initiativeToken)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();
		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		JsonNode result = jsonNode.get("result").get("content");
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.toString()).contains("detail1", "detail2");
	}

	@Test
	void initiative_날짜로_조회() throws Exception {
		// given
		String date = "20251112";

		//when
		MvcResult mvcResult = mvc.perform(get(initiativeUrl + "/date/" + date)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + initiativeToken)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();
		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		JsonNode result = jsonNode.get("result");
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.toString()).contains("detail6", "detail7");
	}

	@Test
	void month_각_날짜에_initiative가_있는지_조회() throws Exception {
		// given
		String date = "2027-11";

		//when
		MvcResult mvcResult = mvc.perform(get(initiativeUrl + "/yearmonth/" + date)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + initiativeToken)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();
		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		JsonNode result = jsonNode.get("result");
		assertThat(result.size()).isEqualTo(14);
	}
}

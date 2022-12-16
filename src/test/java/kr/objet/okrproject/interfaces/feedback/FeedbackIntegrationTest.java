package kr.objet.okrproject.interfaces.feedback;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;

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
import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.infrastructure.feedback.FeedbackRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FeedbackIntegrationTest {

	private final String feedbackUrl = "/api/v1/feedback";

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private FeedbackRepository feedbackRepository;
	@Value("${jwt.secret-key}")
	private String secretKey;
	@Value("${jwt.token.access-expired-time-ms}")
	private Long expiredTimeMs;
	@Autowired
	private MockMvc mvc;
	private String token;
	private String feedbackRetrieveToken;

	@BeforeEach
	void init() {
		String feedBackTestEmail = "feedbackTest@naver.com";
		String feedbackRetrieveEmail = "initiativeRetrieveTest@naver.com";
		token = JwtTokenUtils.generateToken(feedBackTestEmail, secretKey, expiredTimeMs);
		feedbackRetrieveToken = JwtTokenUtils.generateToken(feedbackRetrieveEmail, secretKey, expiredTimeMs);
	}

	@Test
	void feedback_등록_성공() throws Exception {
		// given
		String opinion = "정말 좋아요!!";
		String grade = "BEST_RESULT";
		String initiativeToken = "ini_ixYjj5nODqtb3AH8";
		String projectToken = "mst_qq2f4gbffrgg6421";
		FeedbackDto.Save dto = new FeedbackDto.Save(opinion, grade, projectToken, initiativeToken);

		//when
		MvcResult mvcResult = mvc.perform(post(feedbackUrl)
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
		String feedbackToken = jsonNode.get("result").asText();

		Feedback feedback = feedbackRepository.findByFeedbackToken(feedbackToken).orElseThrow();
		assertThat(feedback.getOpinion()).isEqualTo(opinion);
		assertThat(feedback.getGrade().getCode()).isEqualTo(grade);
	}

	@Test
	void feedback_등록_실패_initiativeToken_없음() throws Exception {
		// given
		String opinion = "정말 좋아요!!";
		String grade = "BEST_RESULT";
		String initiativeToken = "ini_ix32423feetb3AH8";
		String projectToken = "mst_qq2f4gbffrgg6421";
		FeedbackDto.Save dto = new FeedbackDto.Save(opinion, grade, projectToken, initiativeToken);

		//when
		MvcResult mvcResult = mvc.perform(post(feedbackUrl)
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
		assertThat(message).isEqualTo(ErrorCode.INVALID_INITIATIVE_TOKEN.getMessage());
	}

	@Test
	void feedback_등록_실패_완료되지않은_initiative() throws Exception {
		// given
		String opinion = "정말 좋아요!!";
		String grade = "BEST_RESULT";
		String initiativeToken = "ini_ix324gfODqtb3AH8";
		String projectToken = "mst_qq2f4gbffrgg6421";
		FeedbackDto.Save dto = new FeedbackDto.Save(opinion, grade, projectToken, initiativeToken);

		//when
		MvcResult mvcResult = mvc.perform(post(feedbackUrl)
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
		assertThat(message).isEqualTo(ErrorCode.INITIATIVE_IS_NOT_FINISHED.getMessage());
	}

	@Test
	void feedback_등록_실패_grade오류() throws Exception {
		// given
		String opinion = "정말 좋아요!!";
		String grade = "BEST_RESU222";
		String initiativeToken = "ini_ix324gfODqtb3AH8";
		String projectToken = "mst_qq2f4gbffrgg6421";
		FeedbackDto.Save dto = new FeedbackDto.Save(opinion, grade, projectToken, initiativeToken);

		//when
		MvcResult mvcResult = mvc.perform(post(feedbackUrl)
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
		assertThat(message).isEqualTo(ErrorCode.INVALID_FEEDBACK_TYPE.getMessage());
	}

	@Test
	void 피드백_전체_조회_성공() throws Exception {
		//given

		//when
		String mvcResult = mvc.perform(get(feedbackUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + feedbackRetrieveToken)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);
		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult);
		JsonNode result = jsonNode.get("result");
		assertThat(result.get("content").size()).isEqualTo(7);

	}

	@Test
	void 피드백_전체_조회_성공_ALL() throws Exception {
		//given

		//when
		String mvcResult = mvc.perform(get(feedbackUrl + "?searchRange=ALL")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + feedbackRetrieveToken)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);
		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult);
		JsonNode result = jsonNode.get("result");
		assertThat(result.get("content").size()).isEqualTo(7);

	}

	@Test
	void 피드백_전체_조회_실패_쿼리_파라미터_오류() throws Exception {
		//given

		//when
		String mvcResult = mvc.perform(get(feedbackUrl + "?searchRange=test")
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + feedbackRetrieveToken)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
			)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);
		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult);
		JsonNode result = jsonNode.get("message");
		assertThat(result.asText()).contains(ErrorCode.INVALID_SEARCH_RANGE_TYPE.getMessage());

	}
}

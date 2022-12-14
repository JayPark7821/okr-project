package kr.objet.okrproject.interfaces.feedback;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.utils.JwtTokenUtils;
import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.domain.notification.Notification;
import kr.objet.okrproject.infrastructure.feedback.FeedbackRepository;
import kr.objet.okrproject.infrastructure.notification.NotificationRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class FeedbackIntegrationTest {

	private final String feedbackUrl = "/api/v1/feedback";

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private FeedbackRepository feedbackRepository;
	@Autowired
	private NotificationRepository notificationRepository;
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
	void feedback_??????_??????() throws Exception {
		// given
		String opinion = "?????? ?????????!!";
		String grade = "BEST_RESULT";
		String initiativeToken = "ini_ix324gfODqtb3AH8";
		String projectToken = "mst_qq2f4gbffrgg6421";
		FeedbackDto.Save dto = new FeedbackDto.Save(opinion, grade, projectToken, initiativeToken);

		//when
		MvcResult mvcResult = mvc.perform(post(feedbackUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + feedbackRetrieveToken)
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

		List<Notification> notifications = notificationRepository.findAllByEmail("user7@naver.com");
		assertThat(notifications.size()).isEqualTo(1);
		assertThat(notifications.get(0).getMsg()).contains("??? ???????????? ???????????????!");
	}

	@Test
	void feedback_??????_??????_initiativeToken_??????() throws Exception {
		// given
		String opinion = "?????? ?????????!!";
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
	void feedback_??????_??????_??????????????????_initiative() throws Exception {
		// given
		String opinion = "?????? ?????????!!";
		String grade = "BEST_RESULT";
		String initiativeToken = "ini_ixYjj5F43frfdAH8";
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
	void feedback_??????_??????_grade??????() throws Exception {
		// given
		String opinion = "?????? ?????????!!";
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
	void feedback_??????_??????_?????????_?????????_initiative() throws Exception {
		// given
		String opinion = "?????? ?????????!!";
		String grade = "BEST_RESULT";
		String initiativeToken = "ini_ixYjj5nOD2233333331";
		String projectToken = "mst_K4e8a5s7d6lb6421";
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
		assertThat(message).isEqualTo(ErrorCode.CANNOT_FEEDBACK_MYSELF.getMessage());
	}

	@Test
	void feedback_??????_??????_??????_feedback???_??????() throws Exception {
		// given
		String opinion = "?????? ?????????!!";
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
	void ?????????_??????_??????_??????() throws Exception {
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
		assertThat(result.get("content").size()).isEqualTo(8);

	}

	@Test
	void ?????????_??????_??????_??????_ALL() throws Exception {
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
		assertThat(result.get("content").size()).isEqualTo(8);

	}

	@Test
	void ?????????_??????_??????_??????_??????_????????????_??????() throws Exception {
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

	@Test
	void initiative_?????????_??????_?????????Ini_false_fb??????_false() throws Exception {
		//given
		String initiativeToken = "ini_ix324gfODqtb3AH8";
		//when
		String mvcResult = mvc.perform(get(feedbackUrl + "/" + initiativeToken)
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
		assertThat(result.get("myInitiative").asText()).isEqualTo("false");
		assertThat(result.get("wroteFeedback").asText()).isEqualTo("false");
	}

	@Test
	void initiative_?????????_??????_?????????Ini_false_fb??????_true() throws Exception {
		//given
		String initiativeToken = "ini_ix324gfODqtb3AH8";
		String token = JwtTokenUtils.generateToken("notificationTest@naver.com", secretKey, expiredTimeMs);

		//when
		String mvcResult = mvc.perform(get(feedbackUrl + "/" + initiativeToken)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
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
		assertThat(result.get("myInitiative").asText()).isEqualTo("false");
		assertThat(result.get("wroteFeedback").asText()).isEqualTo("true");
	}

	@Test
	void initiative_?????????_??????_?????????Ini_true_fb??????_false() throws Exception {
		//given
		String initiativeToken = "ini_ixYjj5nODqtb3AH8";
		//when
		String mvcResult = mvc.perform(get(feedbackUrl + "/" + initiativeToken)
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
		assertThat(result.get("myInitiative").asText()).isEqualTo("true");
		assertThat(result.get("wroteFeedback").asText()).isEqualTo("false");
	}

	@Test
	void ?????????_?????????_count() throws Exception {
		//given

		//when
		String mvcResult = mvc.perform(get(feedbackUrl + "/count")
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
		assertThat(result.asText()).isEqualTo("2");

	}

	@Test
	void ?????????_?????????_count2() throws Exception {
		//given
		String token = JwtTokenUtils.generateToken("user7@naver.com", secretKey, expiredTimeMs);

		//when
		String mvcResult = mvc.perform(get(feedbackUrl + "/count")
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
		assertThat(result.asText()).isEqualTo("2");

	}

	@Test
	void ?????????_??????????????????_??????() throws Exception {
		//given
		String feedbackToken = "feedback_el6awefwaeyWx39";
		Feedback beforeFeedback = feedbackRepository.findByFeedbackToken(feedbackToken).orElseThrow();
		assertThat(beforeFeedback.isChecked()).isFalse();

		//when
		String mvcResult = mvc.perform(put(feedbackUrl + "/" + feedbackToken)
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
		Feedback feedback = feedbackRepository.findByFeedbackToken("feedback_el6awefwaeyWx39").orElseThrow();
		assertThat(result.asText()).isEqualTo("feedback_el6awefwaeyWx39");
		assertThat(feedback.isChecked()).isTrue();

	}

	@Test
	void ?????????_??????????????????_??????_??????_?????????????????????() throws Exception {
		//given
		String feedbackToken = "feedback_el6awefwaeyWx39";
		Feedback beforeFeedback = feedbackRepository.findByFeedbackToken(feedbackToken).orElseThrow();
		assertThat(beforeFeedback.isChecked()).isFalse();

		//when
		String mvcResult = mvc.perform(put(feedbackUrl + "/" + feedbackToken)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
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
		JsonNode message = jsonNode.get("message");
		Feedback feedback = feedbackRepository.findByFeedbackToken("feedback_el6awefwaeyWx39").orElseThrow();
		assertThat(message.asText()).isEqualTo(ErrorCode.INVALID_FEEDBACK_TOKEN.getMessage());
		assertThat(feedback.isChecked()).isFalse();

	}
}

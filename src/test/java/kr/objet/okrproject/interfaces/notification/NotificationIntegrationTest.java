package kr.objet.okrproject.interfaces.notification;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

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
import kr.objet.okrproject.domain.notification.Notification;
import kr.objet.okrproject.domain.notification.NotificationCheckType;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.infrastructure.notification.NotificationRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NotificationIntegrationTest {

	private final String notificationUrl = "/api/v1/notification";

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private NotificationRepository notificationRepository;
	@Value("${jwt.secret-key}")
	private String secretKey;
	@Value("${jwt.token.access-expired-time-ms}")
	private Long expiredTimeMs;
	@Autowired
	private MockMvc mvc;

	private final String userEmail = "notificationTest@naver.com";
	private User user;
	private String token;

	@BeforeEach
	void init() {
		if (Objects.isNull(user)) {
			token = JwtTokenUtils.generateToken(userEmail, secretKey, expiredTimeMs);
		}
	}

	@Test
	void ?????????_??????_??????() throws Exception {
		//given

		//when
		MvcResult mvcResult = mvc.perform(get(notificationUrl)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		//then
		JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
		JsonNode result = jsonNode.get("result");
		assertThat(result.size()).isEqualTo(5);
	}

	@Test
	void ?????????_????????????_??????_checked() throws Exception {
		//given
		String notiToken = "noti_111fey1SERx";
		Optional<Notification> beforeNotification =
			notificationRepository.findByNotificationToken(notiToken);

		assertThat(beforeNotification.get().getStatus()).isEqualTo(NotificationCheckType.NEW);

		//when

		mvc.perform(put(notificationUrl + "/" + notiToken)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
			).andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		Optional<Notification> afterNotification =
			notificationRepository.findByNotificationToken(notiToken);

		assertThat(afterNotification.get().getStatus()).isEqualTo(NotificationCheckType.CHECKED);
	}

	@Test
	void ?????????_????????????_??????_new_to_delete() throws Exception {
		//given
		String notiToken = "noti_111fey1SERx";
		Optional<Notification> beforeNotification =
			notificationRepository.findByNotificationToken(notiToken);

		assertThat(beforeNotification.get().getStatus()).isEqualTo(NotificationCheckType.NEW);

		//when

		String result = mvc.perform(delete(notificationUrl + "/" + notiToken)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
			).andDo(print())
			.andExpect(status().isBadRequest())
			.andReturn()
			.getResponse()
			.getContentAsString(StandardCharsets.UTF_8);

		JsonNode jsonNode = objectMapper.readTree(result);
		assertThat(jsonNode.get("message").asText()).isEqualTo(ErrorCode.INVALID_REQUEST.getMessage());
		Optional<Notification> afterNotification =
			notificationRepository.findByNotificationToken(notiToken);

		assertThat(afterNotification.get().getStatus()).isEqualTo(NotificationCheckType.NEW);
	}

	@Test
	void ?????????_????????????_??????_deleted() throws Exception {
		//given
		String notiToken = "noti_e144441Zey1SERx";
		Optional<Notification> beforeNotification =
			notificationRepository.findByNotificationToken(notiToken);

		assertThat(beforeNotification.get().getStatus()).isEqualTo(NotificationCheckType.CHECKED);

		//when

		mvc.perform(delete(notificationUrl + "/" + notiToken)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.characterEncoding(StandardCharsets.UTF_8)
			).andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		Optional<Notification> afterNotification =
			notificationRepository.findByNotificationToken(notiToken);

		assertThat(afterNotification.get().getStatus()).isEqualTo(NotificationCheckType.DELETED);
	}

}
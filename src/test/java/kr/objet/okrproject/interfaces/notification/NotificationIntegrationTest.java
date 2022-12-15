package kr.objet.okrproject.interfaces.notification;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.objet.okrproject.common.utils.JwtTokenUtils;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.infrastructure.notification.NotificationRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
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
    void 메시지_조회_성공() throws Exception {
        //given

        //when
        MvcResult mvcResult = mvc.perform(get(notificationUrl)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

        //then
        JsonNode jsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8));
        JsonNode result = jsonNode.get("result");
        assertThat(result.size()).isEqualTo(2);

    }
}
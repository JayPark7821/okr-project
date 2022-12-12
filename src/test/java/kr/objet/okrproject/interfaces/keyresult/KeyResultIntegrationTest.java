package kr.objet.okrproject.interfaces.keyresult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.utils.JwtTokenUtils;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.infrastructure.keyresult.KeyResultRepository;
import kr.objet.okrproject.infrastructure.project.ProjectMasterRepository;
import kr.objet.okrproject.infrastructure.user.UserRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@AutoConfigureMockMvc
public class KeyResultIntegrationTest {


    private final String keyResultUrl = "/api/v1/keyresult";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectMasterRepository projectMasterRepository;
    @Autowired
    private KeyResultRepository keyResultRepository;
    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.token.access-expired-time-ms}")
    private Long expiredTimeMs;
    @Autowired
    private MockMvc mvc;

    private final String projectLeaderEmail = "keyResultTest@naver.com";
    private User user;
    private String token;
    private ProjectMaster projectMaster;
    @BeforeEach
    void init() {
        if (Objects.isNull(user)) {
            //TODO : 통합테스트시 인증 처리 방법
            user = userRepository.findUserByEmail(projectLeaderEmail).get();
            token = JwtTokenUtils.generateToken(user.getEmail(), secretKey, expiredTimeMs);
            projectMaster = projectMasterRepository.findByProjectMasterToken("mst_Kiwqnp1Nq6lb6421").orElseThrow();

        }
    }


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


    @Test
    void keyResult_등록_실패_요청_유저가_해당_프로젝트에_맴버X() throws Exception {
        //given
        KeyResultSaveDto dto = KeyResultSaveDto.builder()
                .name("testKeyResult")
                .projectToken(projectMaster.getProjectMasterToken())
                .build();

        String token = JwtTokenUtils.generateToken("user7@naver.com", secretKey, expiredTimeMs);

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

}

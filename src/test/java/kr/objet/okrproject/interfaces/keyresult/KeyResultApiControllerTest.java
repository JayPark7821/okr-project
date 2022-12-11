package kr.objet.okrproject.interfaces.keyresult;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.objet.okrproject.application.keyresult.KeyResultFacade;
import kr.objet.okrproject.config.WithMockCustomUser;
import kr.objet.okrproject.domain.keyresult.service.KeyResultCommand;
import kr.objet.okrproject.domain.user.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(controllers = KeyResultApiController.class)
class KeyResultApiControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mvc;
    @MockBean
    private KeyResultFacade keyResultFacade;
    private final String url = "/api/v1/keyresult";

    @Test
    @WithMockCustomUser(seq = 1L, email = "test@test.com")
    void keyResult_등록_성공() throws Exception {
        //given
        KeyResultSaveDto dto = KeyResultSaveDtoFixture.create();

        //when
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsBytes(dto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andDo(print())
                .andExpect(status().isCreated());

        //then
        then(keyResultFacade)
                .should(times(1))
                .registerKeyResult(any(KeyResultCommand.RegisterKeyResult.class), any(User.class));
    }


}
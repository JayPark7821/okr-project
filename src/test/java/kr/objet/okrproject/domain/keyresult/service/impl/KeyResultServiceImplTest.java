package kr.objet.okrproject.domain.keyresult.service.impl;

import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.keyresult.service.KeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.KeyResultStore;
import kr.objet.okrproject.domain.keyresult.service.fixture.KeyResultCommandFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class KeyResultServiceImplTest {

    private KeyResultServiceImpl sut;

    @Mock
    private KeyResultStore keyResultStore;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        sut = new KeyResultServiceImpl(keyResultStore);
    }

    @Test
    void 키리절트_등록_성공() throws Exception {
        //given
        KeyResultCommand.RegisterKeyResultWithProject command = KeyResultCommandFixture.create();
        KeyResult keyResult = command.toEntity();
        given(keyResultStore.store(any())).willReturn(keyResult);

        //when
        KeyResult savedKeyResult = assertDoesNotThrow(() -> sut.registerKeyResult(command));

        //then
        assertNotNull(savedKeyResult);
        assertEquals(keyResult.getName(), savedKeyResult.getName());
    }
}
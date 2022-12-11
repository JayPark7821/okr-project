package kr.objet.okrproject.domain.keyresult.service.impl;

import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;
import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultStore;
import kr.objet.okrproject.domain.keyresult.service.fixture.ProjectKeyResultCommandFixture;
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
class ProjectKeyResultServiceImplTest {

    private ProjectKeyResultServiceImpl sut;

    @Mock
    private ProjectKeyResultStore projectKeyResultStore;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        sut = new ProjectKeyResultServiceImpl(projectKeyResultStore);
    }

    @Test
    void 키리절트_등록_성공() throws Exception {
        //given
        ProjectKeyResultCommand.RegisterProjectKeyResultWithProject command = ProjectKeyResultCommandFixture.create();
        ProjectKeyResult projectKeyResult = command.toEntity();
        given(projectKeyResultStore.store(any())).willReturn(projectKeyResult);

        //when
        ProjectKeyResult savedProjectKeyResult = assertDoesNotThrow(() -> sut.registerProjectKeyResult(command));

        //then
        assertNotNull(savedProjectKeyResult);
        assertEquals(projectKeyResult.getName(), savedProjectKeyResult.getName());
    }
}
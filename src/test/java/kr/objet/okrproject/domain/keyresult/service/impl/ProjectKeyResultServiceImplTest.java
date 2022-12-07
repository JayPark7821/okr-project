package kr.objet.okrproject.domain.keyresult.service.impl;

import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;
import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.fixture.ProjectKeyResultCommandFixture;
import kr.objet.okrproject.domain.project.service.impl.ProjectMasterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProjectKeyResultServiceImplTest {

    private ProjectKeyResultServiceImpl sut;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        sut = new ProjectKeyResultServiceImpl();
    }

    @Test
    void 키리절트_등록_성공() throws Exception {
        //given
        ProjectKeyResultCommand.RegisterProjectKeyResult command = ProjectKeyResultCommandFixture.create();
        //when
        ProjectKeyResult savedProjectKeyResult = assertDoesNotThrow(() -> sut.registerProjectKeyResult(command));

        //then
        assertNotNull(savedProjectKeyResult);
    }
}
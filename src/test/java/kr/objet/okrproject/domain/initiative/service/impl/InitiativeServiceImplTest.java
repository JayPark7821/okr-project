package kr.objet.okrproject.domain.initiative.service.impl;

import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.initiative.service.InitiativeReader;
import kr.objet.okrproject.domain.initiative.service.InitiativeStore;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InitiativeServiceImplTest {

	private InitiativeServiceImpl sut;

	@Mock
	private InitiativeStore initiativeStore;

	@Mock
	private InitiativeReader initiativeReader;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		sut = new InitiativeServiceImpl(initiativeReader, initiativeStore);
	}

	@Test
	void initiative_날짜_벨리데이션_성공() throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create(
			LocalDate.now().minusDays(5),
			LocalDate.now().minusDays(0)
		);

		//when
		assertDoesNotThrow(() -> sut.validateInitiativeDates(
				LocalDate.now().minusDays(2),
				LocalDate.now().minusDays(1),
				projectMaster
			)
		);

	}

	@Test
	void initiative_날짜_벨리데이션_실패_initiative_마감일이_프로젝트_시작일_이전() throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create(
			LocalDate.now().minusDays(5),
			LocalDate.now().minusDays(0)
		);

		//when
		assertThrows(OkrApplicationException.class,
			() -> sut.validateInitiativeDates(
				LocalDate.now().minusDays(10),
				LocalDate.now().minusDays(7),
					projectMaster
			)
		);

	}

	@Test
	void initiative_날짜_벨리데이션_실패_initiative_마감일이_프로젝트_마감일_이후() throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create(
			LocalDate.now().minusDays(5),
			LocalDate.now().minusDays(0)
		);

		//when
		assertThrows(OkrApplicationException.class,
			() -> sut.validateInitiativeDates(
				LocalDate.now().minusDays(10),
				LocalDate.now().plusDays(2),
					projectMaster
			)
		);
	}

	@Test
	void initiative_날짜_벨리데이션_실패_initiative_시작일이_프로젝트_마감일_이후() throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create(
			LocalDate.now().minusDays(5),
			LocalDate.now().minusDays(0)
		);

		//when
		assertThrows(OkrApplicationException.class,
			() -> sut.validateInitiativeDates(
				LocalDate.now().plusDays(2),
				LocalDate.now().plusDays(10),
					projectMaster
			)
		);
	}

	@Test
	void initiative_날짜_벨리데이션_실패_initiative_시작일이_프로젝트_시작일_이전() throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create(
			LocalDate.now().minusDays(5),
			LocalDate.now().minusDays(0)
		);

		//when
		assertThrows(OkrApplicationException.class,
			() -> sut.validateInitiativeDates(
				LocalDate.now().minusDays(10),
				LocalDate.now().minusDays(7),
					projectMaster
			)
		);
	}
}
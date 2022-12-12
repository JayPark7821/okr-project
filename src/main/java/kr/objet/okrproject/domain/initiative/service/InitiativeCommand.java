package kr.objet.okrproject.domain.initiative.service;

import java.time.LocalDate;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.team.TeamMember;
import lombok.Builder;
import lombok.Getter;

public class InitiativeCommand {

	@Getter
	public static class registerInitiative {

		private final String keyResultToken;

		private final String name;

		private final LocalDate edt;

		private final LocalDate sdt;

		private final String detail;

		@Builder
		public registerInitiative(String keyResultToken, String name, LocalDate edt, LocalDate sdt, String detail) {
			this.keyResultToken = keyResultToken;
			this.name = name;
			this.edt = edt;
			this.sdt = sdt;
			this.detail = detail;
		}

		public Initiative toEntity(KeyResult keyResult, TeamMember teamMember) {

			if (this.edt.isBefore(LocalDate.now())) {
				throw new OkrApplicationException(ErrorCode.INVALID_END_DATE_FOR_INITIATIVE);
			}
			if (this.edt.isBefore(this.sdt)) {
				throw new OkrApplicationException(ErrorCode.INVALID_END_DATE_FOR_INITIATIVE_SDT);
			}

			return Initiative.builder()
				.keyResult(keyResult)
				.teamMember(teamMember)
				.name(this.name)
				.edt(this.edt)
				.sdt(this.sdt)
				.detail(this.detail)
				.done(false)
				.build();
		}
	}

}

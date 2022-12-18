package kr.objet.okrproject.interfaces.initiative;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.objet.okrproject.common.utils.DateValid;
import kr.objet.okrproject.domain.initiative.InitiativeInfo;
import kr.objet.okrproject.domain.initiative.service.InitiativeCommand;
import kr.objet.okrproject.interfaces.user.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class InitiativeDto {

	@Getter
	@NoArgsConstructor
	public static class Save {

		@Valid
		@NotNull(message = "KR token은 필수 값입니다.")
		@Schema(example = "KeyResult token은")
		private String keyResultToken;

		@Valid
		@NotNull(message = "Initiative 이름은 필수 값입니다.")
		@Schema(example = "Initiative 이름")
		@Size(max = 50, message = "Initiative 이름은 50자보다 클 수 없습니다.")
		private String name;

		@Valid
		@DateValid(message = "8자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
		@NotNull(message = "Initiative 마감일은 필수 값입니다.")
		@Schema(example = "Initiative 마감일 2022-09-09")
		private String edt;

		@Valid
		@DateValid(message = "8자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
		@NotNull(message = "Initiative 시작일은 필수 값입니다.")
		@Schema(example = "Initiative 시작일 2022-09-09")
		private String sdt;

		@Valid
		@NotNull(message = "Initiative 상세정보는 필수 값입니다.")
		@Schema(example = "Initiative 상세정보")
		@Size(max = 200, message = "Initiative 상세정보는 200 자보다 클 수 없습니다.")
		private String detail;

		@Builder
		public Save(String keyResultToken, String name, String edt, String sdt, String detail) {
			this.keyResultToken = keyResultToken;
			this.name = name;
			this.edt = edt;
			this.sdt = sdt;
			this.detail = detail;
		}

		public InitiativeCommand.RegisterInitiative toCommand() {
			LocalDate endDt = LocalDate.parse(this.edt, DateTimeFormatter.ISO_DATE);
			LocalDate startDt = LocalDate.parse(this.sdt, DateTimeFormatter.ISO_DATE);

			return InitiativeCommand.RegisterInitiative.builder()
				.keyResultToken(this.keyResultToken)
				.edt(endDt)
				.sdt(startDt)
				.name(this.name)
				.detail(this.detail)
				.build();
		}
	}

	@Getter
	@NoArgsConstructor
	public static class Response {

		@Schema(description = "프로젝트 token", example = "")
		private String projectToken;

		@Schema(description = "프로젝트 명", example = "프로젝트!!!")
		private String projectNm;

		@Schema(description = "kr token", example = "")
		private String keyResultToken;

		@Schema(description = "프로젝트 Initiative token", example = "ini 1")
		private String initiativeToken;

		@Schema(description = "프로젝트 Initiative 명", example = "정교한 와이어프레임 설계")
		private String initiativeName;

		@Schema(description = "프로젝트 Initiative 상세", example = "정교한 와이어프레임 설계를 통해 사용자 행동 분석하고, .......")
		private String initiativeDetail;

		@Schema(description = "프로젝트 Initiative 완료 여부", example = "true")
		private boolean done;

		@Schema(description = "유저명", example = "홍길동(나)")
		private UserDto.UserBrief user;

		@Schema(description = "Initiative D-day", example = "D-12")
		private String dDay;

		@Schema(description = "프로젝트 Initiative 마감일", example = "7월 23일")
		private String endDate;

		@Schema(description = "프로젝트 Initiative 시작일", example = "7월 23일")
		private String startDate;

		@Schema(description = "유저 email", example = "test@gmail.com")
		private String email;

		@Schema(description = "로그인한 유저의 initiative true/false", example = "true")
		private boolean myInitiative;

		public Response(InitiativeInfo.Response response) {
			this.projectToken = response.getProjectToken();
			this.projectNm = response.getProjectNm();
			this.keyResultToken = response.getKeyResultToken();
			this.initiativeToken = response.getInitiativeToken();
			this.initiativeName = response.getInitiativeName();
			this.initiativeDetail = response.getInitiativeDetail();
			this.done = response.isDone();
			this.user = new UserDto.UserBrief(response.getUser());
			this.dDay = response.getDDay();
			this.endDate = response.getEndDate();
			this.startDate = response.getStartDate();
			this.email = response.getEmail();
			this.myInitiative = response.isMyInitiative();
		}
	}

	@NoArgsConstructor
	@Getter
	public static class UpdateRequest {
		@Valid
		@NotNull(message = "Initiative 상세정보는 필수 값입니다.")
		@Schema(example = "Initiative 상세정보")
		@Size(max = 200, message = "Initiative 상세정보는 200 자보다 클 수 없습니다.")
		private String iniDetail;

		@Valid
		@DateValid(message = "8자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
		@NotNull(message = "Initiative 마감일은 필수 값입니다.")
		@Schema(example = "Initiative 마감일 2022-09-09")
		private String edt;

		@Valid
		@DateValid(message = "8자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
		@NotNull(message = "Initiative 시작일은 필수 값입니다.")
		@Schema(example = "Initiative 시작일 2022-09-09")
		private String sdt;


		@Builder
		public UpdateRequest(String iniDetail, String edt, String sdt) {
			this.iniDetail = iniDetail;
			this.edt = edt;
			this.sdt = sdt;
		}

		public InitiativeCommand.UpdateInitiative toCommand() {
			LocalDate endDt = LocalDate.parse(this.edt, DateTimeFormatter.ISO_DATE);
			LocalDate startDt = LocalDate.parse(this.sdt, DateTimeFormatter.ISO_DATE);
			return InitiativeCommand.UpdateInitiative.builder()
					.iniDetail(this.iniDetail)
					.edt(endDt)
					.sdt(startDt)
					.build();

		}

	}
}

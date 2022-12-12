package kr.objet.okrproject.interfaces.initiative;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.objet.okrproject.common.utils.DateValid;
import kr.objet.okrproject.domain.initiative.service.InitiativeCommand;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InitiativeSaveDto {

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
	public InitiativeSaveDto(String keyResultToken, String name, String edt, String sdt, String detail) {
		this.keyResultToken = keyResultToken;
		this.name = name;
		this.edt = edt;
		this.sdt = sdt;
		this.detail = detail;
	}

	public InitiativeCommand.registerInitiative toCommand() {
		LocalDate endDt = LocalDate.parse(this.edt, DateTimeFormatter.ISO_DATE);
		LocalDate startDt = LocalDate.parse(this.sdt, DateTimeFormatter.ISO_DATE);

		return InitiativeCommand.registerInitiative.builder()
			.keyResultToken(this.keyResultToken)
			.edt(endDt)
			.sdt(startDt)
			.name(this.name)
			.detail(this.detail)
			.build();
	}
}

package kr.objet.okrproject.interfaces.keyresult;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.objet.okrproject.domain.keyresult.service.KeyResultCommand;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class KeyResultSaveDto {

    @Valid
    @NotNull(message = "프로젝트 token은 필수 값입니다.")
    @Schema(example = "프로젝트 토큰")
    private String projectToken;

    @Valid
    @NotNull(message = "Key Result는 필수 값입니다.")
    @Schema(example = "Key Result 명")
    @Size(max = 50, message = "Key Result이름은 50자보다 클 수 없습니다.")
    private String name;

    @Builder
    public KeyResultSaveDto(String projectToken, String name) {
        this.projectToken = projectToken;
        this.name = name;
    }

    public KeyResultCommand.RegisterKeyResult toCommand() {

        return new KeyResultCommand.RegisterKeyResult(
                this.name,
                this.projectToken
        );
    }

}

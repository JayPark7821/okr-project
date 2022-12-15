package kr.objet.okrproject.interfaces.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NotificationDto {

    @Getter
    @NoArgsConstructor
    public static class Response {
        @Schema(description = "알림 id", example = "12")
        private Long id;

        @Schema(description = "알림 타입", example = "NEW_TEAM_MATE")
        private String notiType;

        @Schema(description = "알림 메시지", example = "<프로젝트 No.555 other> 프로젝트에 새로운 팀원이 합류했습니다.")
        private String msg;

        @Schema(description = "확인 여부", example = "false")
        private boolean isChecked;

        @Schema(description = "메시지 받은 일자", example = "2022-02-02")
        private String createdDate;

    }

}

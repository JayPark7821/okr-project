package kr.objet.okrproject.domain.notification;

import lombok.Builder;
import lombok.Getter;

public class NotificationInfo {

    @Getter
    public static class Response {
        private final Long id;

        private final String notiType;

        private final String msg;

        private final boolean isChecked;

        private final String createdDate;

        @Builder
        public Response(Long id, String notiType, String msg, boolean isChecked, String createdDate) {
            this.id = id;
            this.notiType = notiType;
            this.msg = msg;
            this.isChecked = isChecked;
            this.createdDate = createdDate;
        }
    }

}

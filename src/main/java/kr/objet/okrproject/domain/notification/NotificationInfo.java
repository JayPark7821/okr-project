package kr.objet.okrproject.domain.notification;

import lombok.Builder;
import lombok.Getter;

public class NotificationInfo {

	@Getter
	public static class Response {
		private final String notiToken;

		private final String notiType;

		private final String msg;

		private final NotificationCheckType status;

		private final String createdDate;

		@Builder
		public Response(Notification entity) {
			this.notiToken = entity.getNotificationToken();
			this.notiType = entity.getType().name();
			this.msg = entity.getMsg();
			this.status = entity.getStatus();
			this.createdDate = entity.getCreatedDate().toString();
		}
	}

}

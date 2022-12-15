package kr.objet.okrproject.domain.notification.service;

import kr.objet.okrproject.domain.notification.Notification;
import kr.objet.okrproject.domain.notification.Notifications;
import kr.objet.okrproject.domain.user.User;
import lombok.Builder;
import lombok.Getter;

public class NotificationCommand {

	@Getter
	public static class send {
		private final User to;
		private final Notifications notifications;
		private final String msg;

		@Builder
		public send(User to, Notifications notifications, String...param) {
			this.to = to;
			this.notifications = notifications;
			this.msg = notifications.getMsg(param);
		}

		public Notification toEntity() {
			return new Notification(this.to, this.notifications, this.msg);
		}
	}

}

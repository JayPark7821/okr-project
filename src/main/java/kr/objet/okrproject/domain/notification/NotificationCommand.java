package kr.objet.okrproject.domain.notification;

import kr.objet.okrproject.domain.user.User;
import lombok.Getter;

public class NotificationCommand {

	@Getter
	public static class send {
		private final User to;
		private final Notifications notifications;
		private final String msg;

		public send(User to, Notifications notifications, String msg) {
			this.to = to;
			this.notifications = notifications;
			this.msg = msg;
		}

		public Notification toEntity() {
			return new Notification(this.to, this.notifications, this.msg);
		}
	}

}

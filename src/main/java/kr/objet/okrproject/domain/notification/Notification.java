package kr.objet.okrproject.domain.notification;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kr.objet.okrproject.common.entity.BaseTimeEntity;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.common.utils.TokenGenerator;
import kr.objet.okrproject.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notification")
public class Notification extends BaseTimeEntity {

	private static final String NOTIFICATION_PREFIX = "noti_";

	@Id
	@Column(name = "notification_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String notificationToken;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq", updatable = false)
	private User user;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private Notifications type;

	@Column(name = "message")
	private String msg;

	@Column(name = "checked")
	@Enumerated(EnumType.STRING)
	private NotificationCheckType status;

	public void checkNotification() {
		if (this.status.equals(NotificationCheckType.NEW)) {
			this.status = NotificationCheckType.CHECKED;
		} else {
			throw new OkrApplicationException(ErrorCode.INVALID_REQUEST);
		}
	}

	public void deleteNotification() {
		if (this.status.equals(NotificationCheckType.CHECKED)) {
			this.status = NotificationCheckType.DELETED;
		} else {
			throw new OkrApplicationException(ErrorCode.INVALID_REQUEST);
		}
	}

	public Notification(User user, Notifications type, String msg) {
		this.notificationToken = TokenGenerator.randomCharacterWithPrefix(NOTIFICATION_PREFIX);
		this.user = user;
		this.type = type;
		this.msg = msg;
		this.status = NotificationCheckType.NEW;
	}
}

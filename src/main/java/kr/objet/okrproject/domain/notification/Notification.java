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
import kr.objet.okrproject.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notification")
public class Notification extends BaseTimeEntity {

	@Id
	@Column(name = "notification_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq", updatable = false)
	private User user;

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private Notifications type;

	@Column(name = "message")
	private String msg;

	@Column(name = "checked")
	private boolean isChecked;

	public void checkNotification() {
		this.isChecked = true;
	}

	public Notification(User user, Notifications type, String... param) {
		this.user = user;
		this.type = type;
		this.msg = type.getMsg(param);
		this.isChecked = false;
	}
}

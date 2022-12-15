package kr.objet.okrproject.domain.notification;

import kr.objet.okrproject.common.entity.BaseTimeEntity;
import kr.objet.okrproject.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

	public Notification(User user, Notifications type, String msg) {
		this.user = user;
		this.type = type;
		this.msg = msg;
		this.isChecked = false;
	}
}

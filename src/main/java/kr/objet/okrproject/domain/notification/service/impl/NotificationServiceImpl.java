package kr.objet.okrproject.domain.notification.service.impl;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.notification.Notification;
import kr.objet.okrproject.domain.notification.NotificationInfo;
import kr.objet.okrproject.domain.notification.Notifications;
import kr.objet.okrproject.domain.notification.service.NotificationCommand;
import kr.objet.okrproject.domain.notification.service.NotificationReader;
import kr.objet.okrproject.domain.notification.service.NotificationService;
import kr.objet.okrproject.domain.notification.service.NotificationStore;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

	private final NotificationStore notificationStore;
	private final NotificationReader notificationReader;

	private static final double QUARTER = 25;
	private static final double HALF = 50;
	private static final double THREE_QUARTERS = 75;
	private static final double FINISHED = 100;


	@Override
	public void pushNotification(List<NotificationCommand.send> commands) {
		commands.forEach(c -> {
			notificationStore.store(c.toEntity());
		});

	}

	@Override
	public List<NotificationInfo.Response> findNotificationsByUser(User user) {
		List<Notification> notifications = notificationReader.findNotificationsByUser(user);
		return notifications.stream()
			.map(NotificationInfo.Response::new)
			.collect(Collectors.toList());
	}

	@Override
	public void updateNotificationStatus(User user, String token) {
		Notification notification = notificationReader.findByUserAndNotificationToken(user, token)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_NOTIFICAION_TOKEN));
		notification.updateStatus();
	}

	@Override
	public void sendIniDoneNoti(List<TeamMember> teamMember, User user, String initiativeName) {
		Notifications notiType = Notifications.INITIATIVE_ACHIEVED;
		teamMember.stream().filter(t->!t.getUser().getEmail().equals(user.getEmail()))
				.forEach(t->notificationStore.store(
						new Notification(
								t.getUser(),
								notiType,
								notiType.getMsg(user.getUsername(),initiativeName)
						)
				));
	}

	@Override
	public void sendProjectProgressNoti(ProjectMaster projectMaster, Double beforeProgress, Double afterProgress) {

		Double progressAchieve = getProgressAchieve(beforeProgress, afterProgress);
		if (progressAchieve != null) {
			if (progressAchieve == QUARTER) {
				sendProjectProgressNoti(projectMaster, Notifications.PROJECT_PROGRESS_ACHIEVED_QUARTER);
			} else if (progressAchieve == HALF) {
				sendProjectProgressNoti(projectMaster, Notifications.PROJECT_PROGRESS_ACHIEVED_HALF);
			} else if (progressAchieve == THREE_QUARTERS) {
				sendProjectProgressNoti(projectMaster, Notifications.PROJECT_PROGRESS_ACHIEVED_THREE_QUARTERS);
			} else {
				sendProjectProgressNoti(projectMaster, Notifications.PROJECT_FINISHED);
			}
		}
		
	}

	private void sendProjectProgressNoti(ProjectMaster projectMaster, Notifications notiType) {
		projectMaster.getTeamMember().forEach(t -> notificationStore.store(new Notification(t.getUser(), notiType, notiType.getMsg(projectMaster.getName()))));
	}

	private static Double getProgressAchieve(double beforeProgress, double afterProgress) {
		if (afterProgress == FINISHED) {
			return FINISHED;
		} else if (beforeProgress >= HALF && afterProgress >= THREE_QUARTERS) {
			return THREE_QUARTERS;
		} else if (beforeProgress >= QUARTER && afterProgress >= HALF) {
			return HALF;
		} else if (beforeProgress < QUARTER && afterProgress >= QUARTER) {
			return QUARTER;
		}
		return null;
	}
	
}

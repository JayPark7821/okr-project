package kr.objet.okrproject.application.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.domain.feedback.FeedbackInfo;
import kr.objet.okrproject.domain.feedback.SearchRange;
import kr.objet.okrproject.domain.feedback.service.FeedbackCommand;
import kr.objet.okrproject.domain.feedback.service.FeedbackService;
import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.initiative.service.InitiativeService;
import kr.objet.okrproject.domain.notification.Notifications;
import kr.objet.okrproject.domain.notification.service.NotificationCommand;
import kr.objet.okrproject.domain.notification.service.NotificationService;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackFacade {

	private final FeedbackService feedbackService;
	private final ProjectMasterService projectMasterService;
	private final InitiativeService initiativeService;
	private final NotificationService notificationService;

	public String registerFeedback(FeedbackCommand.SaveRequest command, User user) {

		ProjectMaster projectMaster = projectMasterService.validateUserWithProjectMasterToken(
			command.getProjectToken(),
			user
		);

		Initiative initiative = initiativeService.findByInitiativeToken(command.getInitiativeToken());

		Feedback feedback = feedbackService.registerFeedback(
			command.toSave(initiative, projectMaster.getTeamMember().get(0))
		);

		notificationService.pushNotification(
			new NotificationCommand.send(
				initiative.getTeamMember().getUser(),
				Notifications.NEW_FEEDBACK,
				user.getEmail(),
				initiative.getName())
			
		);

		return feedback.getFeedbackToken();
	}

	public Page<FeedbackInfo.Response> getAllFeedbackList(String searchRange, User user, Pageable pageable) {
		SearchRange range = SearchRange.of(searchRange);
		return feedbackService.getAllFeedbackList(range, user, pageable)
			.map(FeedbackInfo.Response::new);

	}

	public FeedbackInfo.IniFeedbackResponse getAllFeedbackListForInitiative(String token, User user) {
		Initiative initiative = initiativeService.findByInitiativeToken(token);

		return new FeedbackInfo.IniFeedbackResponse(feedbackService.getAllFeedbackListForInitiative(token), initiative,
			user);
	}

	public Integer getCountForFeedbackToGive(User user) {
		return initiativeService.getCountForFeedbackToGive(user);
	}

	public String setFeedbackChecked(String feedbackToken, User user) {
		return feedbackService.setFeedbackChecked(feedbackToken, user);
	}
}

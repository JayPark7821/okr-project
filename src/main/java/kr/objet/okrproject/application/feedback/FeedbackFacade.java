package kr.objet.okrproject.application.feedback;

import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.domain.feedback.FeedbackInfo;
import kr.objet.okrproject.domain.feedback.SearchRange;
import kr.objet.okrproject.domain.feedback.service.FeedbackCommand;
import kr.objet.okrproject.domain.feedback.service.FeedbackService;
import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.initiative.service.InitiativeService;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackFacade {

	private final FeedbackService feedbackService;
	private final ProjectMasterService projectMasterService;
	private final InitiativeService initiativeService;

	public String registerFeedback(FeedbackCommand.SaveRequest command, User user) {

		ProjectMaster projectMaster = projectMasterService.validateUserWithProjectMasterToken(
			command.getProjectToken(),
			user
		);

		Initiative initiative = initiativeService.validateInitiativeForFeedback(command.getInitiativeToken());

		Feedback feedback = feedbackService.registerFeedback(
			command.toSave(initiative, projectMaster.getTeamMember().get(0))
		);

		return feedback.getFeedbackToken();
	}

	public Page<FeedbackInfo.Response> getAllFeedbackList(String searchRange, User user, Pageable pageable) {
		SearchRange range = SearchRange.of(searchRange);
		return feedbackService.getAllFeedbackList(range, user, pageable)
			.map(FeedbackInfo.Response::new);

	}

	public FeedbackInfo.IniFeedbackResponse getAllFeedbackListForInitiative(String token, User user) {
		Initiative initiative = initiativeService.validateUserWithProjectMasterToken(token, user);
		return new FeedbackInfo.IniFeedbackResponse(feedbackService.getAllFeedbackListForInitiative(token), initiative, user);
	}

	public Integer getCountForFeedbackToGive(User user) {
		return initiativeService.getCountForFeedbackToGive(user);
	}
}

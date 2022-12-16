package kr.objet.okrproject.application.feedback;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.domain.feedback.service.FeedbackCommand;
import kr.objet.okrproject.domain.feedback.service.FeedbackService;
import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.initiative.service.InitiativeService;
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

	public String registerFeedback(FeedbackCommand.SaveRequest command, User user) {

		ProjectMaster projectMaster = projectMasterService.validateProjectMasterWithUser(
			command.getProjectToken(),
			user
		);

		Initiative initiative = initiativeService.validateInitiativeForFeedback(command.getInitiativeToken());

		Feedback feedback = feedbackService.registerFeedback(
			command.toSave(initiative, projectMaster.getTeamMember().get(0))
		);

		return feedback.getFeedbackToken();
	}

}

package kr.objet.okrproject.domain.feedback.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.domain.feedback.SearchRange;
import kr.objet.okrproject.domain.feedback.service.FeedbackCommand;
import kr.objet.okrproject.domain.feedback.service.FeedbackReader;
import kr.objet.okrproject.domain.feedback.service.FeedbackService;
import kr.objet.okrproject.domain.feedback.service.FeedbackStore;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

	private final FeedbackReader feedbackReader;
	private final FeedbackStore feedbackStore;

	@Override
	public Feedback registerFeedback(FeedbackCommand.ToSave command) {
		if (!command.getInitiative().isDone()) {
			throw new OkrApplicationException(ErrorCode.INITIATIVE_IS_NOT_FINISHED);
		}
		if (command.getInitiative()
			.getFeedback()
			.stream()
			.anyMatch(f -> f.getTeamMember().getUser().equals(command.getTeamMember().getUser()))) {
			throw new OkrApplicationException(ErrorCode.CANNOT_FEEDBACK_MORE_THEN_ONCE);
		}
		if (command.getInitiative().getTeamMember().getUser().equals(command.getTeamMember().getUser())) {
			throw new OkrApplicationException(ErrorCode.CANNOT_FEEDBACK_MYSELF);
		}

		return feedbackStore.store(command.toEntity());
	}

	@Override
	public Page<Feedback> getAllFeedbackList(SearchRange range, User user, Pageable pageable) {
		return feedbackReader.getAllFeedbackList(range, user, pageable);
	}

	@Override
	public List<Feedback> getAllFeedbackListForInitiative(String token) {
		return feedbackReader.getAllFeedbackListForInitiative(token);
	}

	@Transactional
	@Override
	public String setFeedbackChecked(String feedbackToken, User user) {
		Feedback feedback = feedbackReader.findByFeedbackTokenAndUser(feedbackToken, user)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_FEEDBACK_TOKEN));
		feedback.checkFeedback();
		return feedback.getFeedbackToken();
	}
}

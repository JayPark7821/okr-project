package kr.objet.okrproject.domain.feedback.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
		return feedbackStore.store(command.toEntity());
	}

	@Override
	public Page<Feedback> getAllFeedbackList(SearchRange range, User user, Pageable pageable) {
		return feedbackReader.getAllFeedbackList(range, user, pageable);
	}
}

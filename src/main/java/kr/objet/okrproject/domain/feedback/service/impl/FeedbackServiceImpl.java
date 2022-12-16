package kr.objet.okrproject.domain.feedback.service.impl;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.domain.feedback.service.FeedbackCommand;
import kr.objet.okrproject.domain.feedback.service.FeedbackReader;
import kr.objet.okrproject.domain.feedback.service.FeedbackService;
import kr.objet.okrproject.domain.feedback.service.FeedbackStore;
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
}

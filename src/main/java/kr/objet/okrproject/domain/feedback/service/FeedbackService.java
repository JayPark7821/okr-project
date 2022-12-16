package kr.objet.okrproject.domain.feedback.service;

import kr.objet.okrproject.domain.feedback.Feedback;

public interface FeedbackService {
	Feedback registerFeedback(FeedbackCommand.ToSave command);
}

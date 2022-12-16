package kr.objet.okrproject.domain.feedback.service;

import kr.objet.okrproject.domain.feedback.Feedback;

public interface FeedbackStore {
	Feedback store(Feedback toEntity);
}

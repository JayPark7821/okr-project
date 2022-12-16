package kr.objet.okrproject.infrastructure.feedback;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.domain.feedback.service.FeedbackStore;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedbackStoreImpl implements FeedbackStore {

	private final FeedbackRepository feedbackRepository;

	@Override
	public Feedback store(Feedback entity) {
		return feedbackRepository.save(entity);
	}
}

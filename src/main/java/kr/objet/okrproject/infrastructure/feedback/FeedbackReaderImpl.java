package kr.objet.okrproject.infrastructure.feedback;

import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.domain.feedback.SearchRange;
import kr.objet.okrproject.domain.feedback.service.FeedbackReader;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FeedbackReaderImpl implements FeedbackReader {

	private final FeedbackRepository feedbackRepository;
	private final FeedbackQueryRepository feedbackQueryRepository;

	@Override
	public Page<Feedback> getAllFeedbackList(SearchRange range, User user, Pageable pageable) {
		return feedbackQueryRepository.getAllFeedbackList(range, user, pageable);
	}

	@Override
	public List<Feedback> getAllFeedbackListForInitiative(String token) {
		return feedbackQueryRepository.getAllFeedbackListForInitiative(token);
	}

	@Override
	public Optional<Feedback> findByFeedbackTokenAndUser(String feedbackToken, User user) {
		return feedbackRepository.findByFeedbackTokenAndUser(feedbackToken, user);
	}
}

package kr.objet.okrproject.domain.feedback.service;

import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.domain.feedback.SearchRange;
import kr.objet.okrproject.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackService {
	Feedback registerFeedback(FeedbackCommand.ToSave command);

	Page<Feedback> getAllFeedbackList(SearchRange range, User user, Pageable pageable);

    List<Feedback> getAllFeedbackListForInitiative(String token);

    String setFeedbackChecked(String feedbackToken, User user);
}

package kr.objet.okrproject.domain.feedback.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.domain.feedback.SearchRange;
import kr.objet.okrproject.domain.user.User;

public interface FeedbackService {
	Feedback registerFeedback(FeedbackCommand.ToSave command);

	Page<Feedback> getAllFeedbackList(SearchRange range, User user, Pageable pageable);

	List<Feedback> getAllFeedbackListForInitiative(String token);

	String setFeedbackChecked(String feedbackToken, User user);
}

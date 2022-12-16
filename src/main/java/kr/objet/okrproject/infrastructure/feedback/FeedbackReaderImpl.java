package kr.objet.okrproject.infrastructure.feedback;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.feedback.service.FeedbackReader;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FeedbackReaderImpl implements FeedbackReader {

	private final FeedbackRepository feedbackRepository;
	
}

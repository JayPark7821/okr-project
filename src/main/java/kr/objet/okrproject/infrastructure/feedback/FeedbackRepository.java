package kr.objet.okrproject.infrastructure.feedback;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.objet.okrproject.domain.feedback.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

	Optional<Feedback> findByFeedbackToken(String token);
}

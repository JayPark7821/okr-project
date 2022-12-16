package kr.objet.okrproject.infrastructure.feedback;

import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

	Optional<Feedback> findByFeedbackToken(String token);

    @Query("select f " +
            "from Feedback f " +
            "join f.initiative i " +
            "join i.teamMember t " +
            "where t.user =:user " +
            "and f.feedbackToken =:feedbackToken ")
    Optional<Feedback> findByFeedbackTokenAndUser(@Param("feedbackToken") String feedbackToken, @Param("user")User user);
}

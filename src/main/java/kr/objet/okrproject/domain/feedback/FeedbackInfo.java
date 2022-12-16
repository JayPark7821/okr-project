package kr.objet.okrproject.domain.feedback;

import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.user.User;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class FeedbackInfo {

	@Getter
	public static class Response {

		private final String projectToken;
		private final String feedbackToken;
		private final String opinion;
		private final FeedbackType grade;
		private final Long writerId;
		private final String writerName;
		private final String writerJob;
		private final String profileImage;

		public Response(Feedback feedback) {
			this.projectToken = feedback.getTeamMember().getProjectMaster().getProjectMasterToken();
			this.feedbackToken = feedback.getFeedbackToken();
			this.opinion = feedback.getOpinion();
			this.grade = feedback.getGrade();
			this.writerId = feedback.getTeamMember().getUser().getUserSeq();
			this.writerName = feedback.getTeamMember().getUser().getUsername();
			this.writerJob = feedback.getTeamMember().getUser().getJobField().getTitle();
			this.profileImage = feedback.getTeamMember().getUser().getProfileImageUrl();
		}
	}

	@Getter
    public static class IniFeedbackResponse {

		private boolean myInitiative;
		private boolean wroteFeedback;
		private List<FeedbackInfo.Response> feedbacks;

		public IniFeedbackResponse(List<Feedback> feedbacks, Initiative initiative, User feedbackRequester) {
			this.feedbacks = feedbacks.stream().map(Response::new).collect(Collectors.toList());
			if (feedbacks.size() > 0) {
				this.myInitiative = initiative.getTeamMember().getUser().equals(feedbackRequester);
				this.wroteFeedback = feedbacks.stream()
						.anyMatch(f -> f.getTeamMember().getUser().equals(feedbackRequester));
			}
		}
	}
}

package kr.objet.okrproject.domain.feedback;

import lombok.Getter;

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

}

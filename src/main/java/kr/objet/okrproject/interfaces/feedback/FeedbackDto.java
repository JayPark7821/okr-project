package kr.objet.okrproject.interfaces.feedback;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.objet.okrproject.domain.feedback.FeedbackInfo;
import kr.objet.okrproject.domain.feedback.FeedbackType;
import kr.objet.okrproject.domain.feedback.service.FeedbackCommand;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FeedbackDto {

	@Getter
	@NoArgsConstructor
	public static class Save {

		@Valid
		@NotNull(message = "피드백은 필수 값입니다.")
		@Size(max = 300, message = "피드백은 300자보다 클 수 없습니다.")
		@Schema(example = "의견")
		private String opinion;

		@Valid
		@NotNull(message = "평가 등급은 필수 값입니다.")
		@Schema(example = "평가 등급 (GOOD_IDEA, BEST_RESULT, BURNING_PASSION, COMMUNI_KING)")
		private String grade;

		@Valid
		@NotNull(message = "프로젝트 token은 필수 값입니다.")
		@Schema(example = "프로젝트 token")
		private String projectToken;

		@Valid
		@NotNull(message = "Initiative token은 필수 값입니다.")
		@Schema(example = "Initiative token")
		private String initiativeToken;

		@Builder
		public Save(String opinion, String grade, String projectToken, String initiativeToken) {

			this.opinion = opinion;
			this.grade = grade;
			this.projectToken = projectToken;
			this.initiativeToken = initiativeToken;
		}

		public FeedbackCommand.SaveRequest toCommand() {
			FeedbackType feedbackType = FeedbackType.of(this.grade);
			return new FeedbackCommand.SaveRequest(this.opinion, feedbackType, this.projectToken, this.initiativeToken);
		}
	}

	@Getter
	@NoArgsConstructor
	public static class Response {

		private String projectToken;
		private String feedbackToken;
		private String opinion;
		private FeedbackType grade;
		private Long writerId;
		private String writerName;
		private String writerJob;
		private String profileImage;

		public Response(FeedbackInfo.Response response) {
			this.projectToken = response.getProjectToken();
			this.feedbackToken = response.getFeedbackToken();
			this.opinion = response.getOpinion();
			this.grade = response.getGrade();
			this.writerId = response.getWriterId();
			this.writerName = response.getWriterName();
			this.writerJob = response.getWriterJob();
			this.profileImage = response.getProfileImage();
		}
	}

}

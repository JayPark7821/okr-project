package kr.objet.okrproject.domain.feedback.service;

import kr.objet.okrproject.domain.feedback.Feedback;
import kr.objet.okrproject.domain.feedback.FeedbackType;
import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.team.TeamMember;
import lombok.Builder;
import lombok.Getter;

public class FeedbackCommand {

	@Getter
	public static class SaveRequest {

		private final String opinion;

		private final FeedbackType grade;

		private final String projectToken;

		private final String initiativeToken;

		@Builder
		public SaveRequest(String opinion, FeedbackType grade, String projectToken, String initiativeToken) {
			this.opinion = opinion;
			this.grade = grade;
			this.projectToken = projectToken;
			this.initiativeToken = initiativeToken;
		}

		public ToSave toSave(Initiative initiative, TeamMember teamMember) {
			return new ToSave(this.opinion, this.grade, initiative, teamMember);
		}

	}

	@Getter
	public static class ToSave {

		private final String opinion;

		private final FeedbackType grade;

		private final Initiative initiative;

		private final TeamMember teamMember;

		@Builder
		public ToSave(String opinion, FeedbackType grade, Initiative initiative, TeamMember teamMember) {
			this.opinion = opinion;
			this.grade = grade;
			this.initiative = initiative;
			this.teamMember = teamMember;
		}

		public Feedback toEntity() {
			return new Feedback(this.initiative, this.teamMember, this.grade, this.opinion);
		}

	}

}

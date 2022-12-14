package kr.objet.okrproject.domain.initiative;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import kr.objet.okrproject.domain.user.User;
import lombok.Getter;

public class InitiativeInfo {

	@Getter
	public static class Response {
		private String projectToken;

		private String projectNm;

		private String keyResultToken;

		private String initiativeToken;

		private String initiativeName;

		private String initiativeDetail;

		private boolean done;

		private User user;

		private String dDay;

		private String endDate;

		private String startDate;

		private String email;

		private boolean myInitiative;

		public Response(Initiative entity, User user) {
			long until = LocalDate.now().until(entity.getEdt(), ChronoUnit.DAYS);
			this.projectNm = entity.getKeyResult().getProjectMaster().getName();
			this.projectToken = entity.getKeyResult().getProjectMaster().getProjectMasterToken();
			this.keyResultToken = entity.getKeyResult().getKeyResultToken();
			this.initiativeToken = entity.getInitiativeToken();
			this.initiativeName = entity.getName();
			this.initiativeDetail = entity.getDetail();
			this.done = entity.isDone();
			this.user = entity.getTeamMember().getUser();
			this.endDate = entity.getEdt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			this.startDate = entity.getSdt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			this.dDay = "D" + (until > 0 ? "-" + until : until * -1);
			this.email = entity.getTeamMember().getUser().getEmail();
			this.myInitiative = entity.getTeamMember().getUser().getEmail().equals(user.getEmail());
		}
	}
}

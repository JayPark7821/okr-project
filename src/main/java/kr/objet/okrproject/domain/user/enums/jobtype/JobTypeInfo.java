package kr.objet.okrproject.domain.user.enums.jobtype;

import lombok.Getter;

@Getter
public class JobTypeInfo {

	@Getter
	public static class Response {
		private final String code;
		private final String title;

		public Response(JobType jobType) {
			this.code = jobType.getCode();
			this.title = jobType.getTitle();
		}
	}

}

package kr.objet.okrproject.interfaces.user;

import kr.objet.okrproject.domain.user.enums.jobtype.JobTypeInfo;
import lombok.Getter;

@Getter
public class JobTypeDto {

	@Getter
	public static class Response {
		private final String code;
		private final String title;

		public Response(JobTypeInfo.Response response) {
			this.code = response.getCode();
			this.title = response.getTitle();
		}
	}

}

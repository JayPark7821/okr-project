package kr.objet.okrproject.domain.user.enums.jobtype;

import lombok.Getter;

@Getter
public class JobTypeDto {
	private String code;
	private String title;

	public JobTypeDto(JobType jobType) {
		this.code = jobType.getCode();
		this.title = jobType.getTitle();
	}

}

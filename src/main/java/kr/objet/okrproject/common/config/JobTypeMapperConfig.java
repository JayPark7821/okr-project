package kr.objet.okrproject.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.user.enums.jobtype.JobField;
import kr.objet.okrproject.domain.user.enums.jobtype.JobTypeMapper;

@Component
public class JobTypeMapperConfig {

	@Bean
	public JobTypeMapper jobMapper() {
		JobTypeMapper jobTypeMapper = new JobTypeMapper();
		jobTypeMapper.put("JobField", JobField.class);
		return jobTypeMapper;
	}
}

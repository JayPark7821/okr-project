package kr.objet.okrproject.domain.user.enums.jobtype;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JobTypeMapper {
	private Map<String, List<JobTypeInfo.Response>> factory = new LinkedHashMap<>();

	public JobTypeMapper() {
	}

	public void put(String key, Class<? extends JobType> e) {
		factory.put(key, toEnumValues(e));
	}

	private List<JobTypeInfo.Response> toEnumValues(Class<? extends JobType> e) {
		return Arrays.stream(e.getEnumConstants())
			.map(JobTypeInfo.Response::new)
			.collect(Collectors.toList());
	}

	public List<JobTypeInfo.Response> get(String key) {
		return factory.get(key);
	}
}

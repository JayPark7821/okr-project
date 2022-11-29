package kr.objet.okrproject.domain.user.jobtype;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JobTypeMapper {
    private Map<String, List<JobTypeDto>> factory = new LinkedHashMap<>();

    public JobTypeMapper() {
    }

    public void put(String key, Class<? extends JobType> e) {
        factory.put(key, toEnumValues(e));
    }

    private List<JobTypeDto> toEnumValues(Class<? extends JobType> e) {
        return Arrays.stream(e.getEnumConstants())
                .map(JobTypeDto::new)
                .collect(Collectors.toList());
    }

    public List<JobTypeDto> get(String key) {
        return factory.get(key);
    }
}

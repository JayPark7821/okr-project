package kr.objet.okrproject.domain.feedback;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchRange {
	WEEK("WEEK"),
	MONTH("MONTH"),
	HALF_YEAR("HALF_YEAR"),
	ALL("ALL");

	private final String code;

	public static SearchRange of(String code) {
		return code == null ? ALL : Arrays.stream(SearchRange.values())
			.filter(r -> r.getCode().equals(code))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_SEARCH_RANGE_TYPE));
	}

	public Map<String, LocalDate> getRange() {
		LocalDate now = LocalDate.now();

		switch (code) {
			case "WEEK":
				return Map.of("startDt", now.minusDays(7), "endDt", now.plusDays(1));
			case "MONTH":
				return Map.of("startDt", now.minusDays(30), "endDt", now.plusDays(1));
			case "HALF_YEAR":
				return Map.of("startDt", now.minusDays(183), "endDt", now.plusDays(1));
			default:
				return null;
		}
	}
}

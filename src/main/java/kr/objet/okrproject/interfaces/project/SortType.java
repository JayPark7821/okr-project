package kr.objet.okrproject.interfaces.project;

import java.util.Arrays;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortType {
	RECENTLY_CREATE("RECENTLY_CREATE"),
	DEADLINE_CLOSE("DEADLINE_CLOSE"),
	PROGRESS_HIGH("PROGRESS_HIGH"),
	PROGRESS_LOW("PROGRESS_LOW");

	private final String code;

	public static SortType of(String code) {
		return code == null ? RECENTLY_CREATE : Arrays.stream(SortType.values())
			.filter(r -> r.getCode().equals(code))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_SORT_TYPE));
	}
}

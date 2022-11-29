package kr.objet.okrproject.domain.user.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProviderType {
	GOOGLE("GOOGLE", "google"),
	APPLE("APPLE", "apple"),
	LOCAL("LOCAL", "LOCAL");

	private final String code;
	private final String displayName;

	public static ProviderType of(String code) {
		return Arrays.stream(ProviderType.values())
			.filter(r -> r.getCode().equals(code))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 타입입니다."));
	}
}

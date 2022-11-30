package kr.objet.okrproject.common.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),
	INVALID_USER_INFO(HttpStatus.BAD_REQUEST, "잘못된 사용자 정보 입니다."),
	UNSUPPORTED_SOCIAL_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 소셜로그인 타입 입니다."),
	MISS_MATCH_PROVIDER(HttpStatus.BAD_REQUEST, "소셜 provider 불일치"),
	INVALID_JOB_DETAIL_FIELD(HttpStatus.BAD_REQUEST, "선택한 직업 정보가 정보가 없습니다."),
	INVALID_JOB_FIELD(HttpStatus.BAD_REQUEST, "직업 카테고리 정보가 없습니다."),
	;

	private HttpStatus status;
	private String message;

}

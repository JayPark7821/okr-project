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
	ALREADY_JOINED_USER(HttpStatus.BAD_REQUEST,"이미 가입된 회원입니다." ),
	INVALID_JOIN_INFO(HttpStatus.BAD_REQUEST, "잘못된 가입 정보 입니다." ),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
	INVALID_FEEDBACK_TYPE(HttpStatus.BAD_REQUEST,"잘못된 피드백 타입 입니다." ),
	INVALID_PROJECT_SDT_EDT(HttpStatus.BAD_REQUEST, "프로젝트 시작 일자는 프로젝트 종료 일자 이전일 수 없습니다."),
	INVALID_PROJECT_END_DATE(HttpStatus.BAD_REQUEST, "종료 일짜가 지난 프로젝트입니다."),
	REQUIRED_DATE_VALUE(HttpStatus.BAD_REQUEST,"날짜는 필수 값 입니다." ),
	CASTING_USER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"Casting to User failed" ),
	INVALID_PROJECT_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 프로젝트 토큰 입니다."),
	USER_IS_NOT_LEADER(HttpStatus.BAD_REQUEST, "해당 프로젝트의 리더만 팀원을 초대할 수 있습니다."),
	NO_USERS_ADDED(HttpStatus.BAD_REQUEST,"신규로 추가된 사용자가 없습니다." );

	private HttpStatus status;
	private String message;

}

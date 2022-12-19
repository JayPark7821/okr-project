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
	ALREADY_JOINED_USER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
	INVALID_JOIN_INFO(HttpStatus.BAD_REQUEST, "잘못된 가입 정보 입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
	INVALID_FEEDBACK_TYPE(HttpStatus.BAD_REQUEST, "잘못된 피드백 타입 입니다."),
	INVALID_PROJECT_SDT_EDT(HttpStatus.BAD_REQUEST, "프로젝트 시작 일자는 프로젝트 종료 일자 이전일 수 없습니다."),
	INVALID_PROJECT_END_DATE(HttpStatus.BAD_REQUEST, "종료 일짜가 지난 프로젝트입니다."),
	REQUIRED_DATE_VALUE(HttpStatus.BAD_REQUEST, "날짜는 필수 값 입니다."),
	CASTING_USER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Casting to User failed"),
	INVALID_PROJECT_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 프로젝트 토큰 입니다."),
	USER_IS_NOT_LEADER(HttpStatus.BAD_REQUEST, "해당 프로젝트의 리더만 팀원을 초대할 수 있습니다."),
	NO_USERS_ADDED(HttpStatus.BAD_REQUEST, "신규로 추가된 사용자가 없습니다."),
	NOT_AVAIL_INVITE_MYSELF(HttpStatus.BAD_REQUEST, "자기 자신은 초대할 수 없습니다."),
	INVALID_USER_EMAIL(HttpStatus.BAD_REQUEST, "등록된 email이 없습니다."),
	USER_ALREADY_PROJECT_MEMBER(HttpStatus.BAD_REQUEST, "이미 해당 프로젝트 팀원 입니다."),
	INVALID_END_DATE_FOR_INITIATIVE(HttpStatus.BAD_REQUEST, "Initiative의 마감일은 오늘 이전일 수 없습니다."),
	INVALID_END_DATE_FOR_INITIATIVE_SDT(HttpStatus.BAD_REQUEST, "Initiative의 마감일이 시작일 이전일 수 없습니다."),
	INVALID_INITIATIVE_END_DATE(HttpStatus.BAD_REQUEST, "Initiative의 마감일은 프로젝트 시작, 종료일 사이어야 합니다."),
	INVALID_KEYRESULT_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 KeyResult token 입니다."),
	INVALID_SORT_TYPE(HttpStatus.BAD_REQUEST, "잘못된 정렬 코드 입니다."),
	INVALID_FINISHED_RPOJECT_YN(HttpStatus.BAD_REQUEST, "종료된 프로젝트 포함여부는 Y 또는 N 만 가능합니다. "),
	INVALID_SEARCH_DATE_FORM(HttpStatus.BAD_REQUEST, "날짜는 8자리의 yyyyMMdd 형식이어야 합니다."),
	INVALID_YEARMONTH_FORMAT(HttpStatus.BAD_REQUEST, "년월 형식(yyyy-MM)에 맞지 않습니다"),
	INVALID_NOTIFICAION_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 Notification token 입니다."),
	INVALID_INITIATIVE_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 Initiative token 입니다."),
	INITIATIVE_IS_NOT_FINISHED(HttpStatus.BAD_REQUEST, "완료되지 않은 Initative에는 피드백을 남길 수 없습니다."),
	INVALID_SEARCH_RANGE_TYPE(HttpStatus.BAD_REQUEST, "잘못된 검색 기간입니다."),
	INVALID_INITIATIVE_INFO(HttpStatus.BAD_REQUEST, "Initiative의 정보 오류 입니다."),
	ALREADY_FINISHED_INITIATIVE(HttpStatus.BAD_REQUEST, "이미 완료된 Initiative입니다."),
	INVALID_FEEDBACK_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 Feedback token 입니다."),
	CANNOT_FEEDBACK_MORE_THEN_ONCE(HttpStatus.BAD_REQUEST, "이미 feedbackd을 남기신 initiative에는 feedback을 남길 수 없습니다."),
	CANNOT_FEEDBACK_MYSELF(HttpStatus.BAD_REQUEST, "자기 자신의 initiative에는 feedback을 남길 수 없습니다."),
	INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	;

	private HttpStatus status;
	private String message;

}

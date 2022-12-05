package kr.objet.okrproject.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import kr.objet.okrproject.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {

	private final int resultCode;
	private final String message;
	private T result;

	public static ResponseEntity<Response<Void>> error(HttpStatus status, String message) {
		return ResponseEntity.status(status)
			.body(new Response<>(status.value(), message,null));
	}

	public static ResponseEntity<Response<Void>> error(ErrorCode errorCode ) {
		return ResponseEntity.status(errorCode.getStatus())
			.body(new Response<>(errorCode.getStatus().value(), errorCode.getMessage(),null));
	}

	public static <T> ResponseEntity<Response<T>> success(HttpStatus status, T result) {
		return ResponseEntity.status(status)
			.body(new Response<>(status.value(), "SUCCESS", result));
	}


}

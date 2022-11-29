package kr.objet.okrproject.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OkrApplicationException extends RuntimeException{

	private final ErrorCode errorCode;

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}

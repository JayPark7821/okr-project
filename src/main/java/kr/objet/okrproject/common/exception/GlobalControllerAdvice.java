package kr.objet.okrproject.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.objet.okrproject.common.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

	@ExceptionHandler(OkrApplicationException.class)
	public ResponseEntity<?> applicationHandler(OkrApplicationException e) {
		log.error("Error occurs {}", e.toString());
		return Response.error(e.getErrorCode().getStatus(), e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> applicationHandler(MethodArgumentNotValidException e) {
		log.error("Error occurs {}", e.toString());
		return Response.error(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> applicationHandler(RuntimeException e) {
		log.error("Error occurs {}", e.toString());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR));
	}

}

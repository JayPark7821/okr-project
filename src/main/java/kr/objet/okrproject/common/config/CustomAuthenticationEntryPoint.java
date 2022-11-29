package kr.objet.okrproject.common.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import kr.objet.okrproject.common.exception.ErrorCode;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		response.setContentType("application/json");
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(ErrorCode.INVALID_TOKEN.getStatus().value());
		response.getWriter().write( "{"
			+ "\"resultCode\":" + "\"" + ErrorCode.INVALID_TOKEN.getStatus().value() + "\","
			+ "\"message\":" + "\"" + ErrorCode.INVALID_TOKEN.getMessage() + "\","
			+ "\"result\":" + null + "}"
		);
	}
}

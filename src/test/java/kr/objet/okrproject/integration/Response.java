package kr.objet.okrproject.integration;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Response {
	private String resultCode;
	private String message;
	private String result;
}

package kr.objet.okrproject.interfaces.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "ApiDocController", description = "API 문서")
public class ApiDocController {

	@GetMapping("/api/docs")
	public String homeRedirectToSwagger() {
		return "/swagger.html";
	}
}

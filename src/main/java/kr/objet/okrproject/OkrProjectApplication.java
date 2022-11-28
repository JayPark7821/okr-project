package kr.objet.okrproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OkrProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(OkrProjectApplication.class, args);
	}

}

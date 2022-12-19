package kr.objet.okrproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OkrProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(OkrProjectApplication.class, args);
	}

}

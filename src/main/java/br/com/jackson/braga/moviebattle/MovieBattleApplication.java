package br.com.jackson.braga.moviebattle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MovieBattleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieBattleApplication.class, args);
	}

}

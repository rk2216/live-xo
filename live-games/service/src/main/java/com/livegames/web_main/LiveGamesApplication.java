package com.livegames.web_main;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ComponentScan(basePackages = "com.livegames")
public class LiveGamesApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiveGamesApplication.class, args);
	}

}

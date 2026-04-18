package com.persona.superation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@ComponentScan(basePackages = {"com.persona.superation",
		"com.persona.superation.controller",
		   "com.persona.superation.dto",
		   "com.persona.superation.repository",
		   "com.persona.superation.service",
		   "com.persona.superation.entity",
		   "com.persona.superation.exception",
		   "com.persona.superation.handlers",
		   "com.persona.superation.util",
		   "com.persona.superation.config",
		   "com.persona.superation.constants",
		   })
@EnableJpaRepositories(basePackages = "com.persona.superation.repository")
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

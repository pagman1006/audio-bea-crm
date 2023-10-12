package com.audiobea.crm.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class AudioBeaCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(AudioBeaCrmApplication.class, args);
	}

}

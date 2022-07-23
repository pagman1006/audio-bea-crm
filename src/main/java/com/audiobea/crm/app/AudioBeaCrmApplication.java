package com.audiobea.crm.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
	FileStorageProperties.class
})
public class AudioBeaCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(AudioBeaCrmApplication.class, args);
	}

}

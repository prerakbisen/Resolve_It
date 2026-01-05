package com.Info_intern.Hgs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HgsApplication {

	public static void main(String[] args) {
		EarlyHealthServer.start();
		SpringApplication.run(HgsApplication.class, args);
	}

}

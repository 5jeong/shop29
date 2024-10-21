package com.toy2.shop29;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ServletComponentScan
@EnableAsync
public class Shop29Application {

	public static void main(String[] args) {

		SpringApplication.run(Shop29Application.class, args);
	}

}

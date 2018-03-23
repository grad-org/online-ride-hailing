package com.gd.orh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class OnlineRideHailingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineRideHailingApplication.class, args);
	}

	@RequestMapping("/")
	public String index() {
		return "hello world";
	}
}

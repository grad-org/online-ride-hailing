package com.gd.orh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.gd.orh.mapper")
public class OnlineRideHailingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineRideHailingApplication.class, args);
	}
}

package com.gd.orh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.gd.orh.mapper")
public class OnlineRideHailingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineRideHailingApplication.class, args);
	}
}

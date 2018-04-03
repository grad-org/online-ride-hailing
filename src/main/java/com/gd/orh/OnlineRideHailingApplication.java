package com.gd.orh;

import com.gd.orh.entity.Role;
import com.gd.orh.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class OnlineRideHailingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineRideHailingApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer () {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedHeaders("*")
						.allowedMethods("*");
			}
		};
	}

	@Bean
	public CommandLineRunner start(RoleRepository roleRepo) {
		return args -> {
			Role admin = new Role();
			admin.setName("ROLE_ADMIN");
			roleRepo.save(admin);

			Role driver = new Role();
			driver.setName("ROLE_DRIVER");
			roleRepo.save(driver);

			Role passenger = new Role();
			passenger.setName("ROLE_PASSENGER");
			roleRepo.save(passenger);
		};
	}
}

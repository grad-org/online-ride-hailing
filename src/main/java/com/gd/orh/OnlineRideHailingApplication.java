package com.gd.orh;

import com.gd.orh.entity.Role;
import com.gd.orh.entity.User;
import com.gd.orh.repository.RoleRepository;
import com.gd.orh.repository.UserRepository;
import com.google.common.collect.Lists;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
	public CommandLineRunner start(RoleRepository roleRepo, UserRepository userRepo) {
		return args -> {
			Role admin = Role.builder().name("ROLE_ADMIN").build();
			Role passenger = Role.builder().name("ROLE_PASSENGER").build();
			Role driver = Role.builder().name("ROLE_DRIVER").build();

			admin = roleRepo.save(admin);
			driver = roleRepo.save(driver);
			passenger = roleRepo.save(passenger);

			User a = User.builder().username("admin").password("admin").roles(Lists.newArrayList(admin)).nickname("系统管理员").build();
			User d1 = User.builder().username("d1").password("d1").roles(Lists.newArrayList(driver)).nickname("车主1").build();
			User d2 = User.builder().username("d2").password("d2").roles(Lists.newArrayList(driver)).nickname("车主2").build();
			User p1 = User.builder().username("p1").password("p1").roles(Lists.newArrayList(passenger)).nickname("乘客1").build();
			User p2 = User.builder().username("p2").password("p2").roles(Lists.newArrayList(passenger)).nickname("乘客2").build();

			userRepo.save(a);
			userRepo.save(d1);
			userRepo.save(d2);
			userRepo.save(p1);
			userRepo.save(p2);
		};
	}
}

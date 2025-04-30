package com.example.jobresearch;

import com.example.jobresearch.domain.models.AppUser;
import com.example.jobresearch.domain.models.Role;
import com.example.jobresearch.infra.repositories.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.jobresearch.infra.repositories")
public class JobresearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobresearchApplication.class, args);
	}

	@Bean
	CommandLineRunner run(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.count() == 0) { // 只在数据库空的时候添加
				AppUser user1 = new AppUser("testuser", passwordEncoder.encode("test123"), Role.USER);
				AppUser user2 = new AppUser("adminuser", passwordEncoder.encode("admin123"), Role.ADMIN);
				userRepository.save(user1);
				userRepository.save(user2);
			}
		};
	}
}

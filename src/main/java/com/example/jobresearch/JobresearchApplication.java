package com.example.jobresearch;

import com.example.jobresearch.domain.models.AppUser;
import com.example.jobresearch.domain.models.Role;
import com.example.jobresearch.repositories.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.jobresearch.repositories")
public class JobresearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobresearchApplication.class, args);
	}

	@Bean
	CommandLineRunner run(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.count() == 0) { // Add only if the database is empty
				String userPassword = System.getenv("TESTUSER_PW");
				String adminPassword = System.getenv("ADMINUSER_PW");

				if (userPassword == null || adminPassword == null) {
					throw new IllegalStateException("❌ TESTUSER_PW or ADMINUSER_PW environment variable is not set!！");
				}

				AppUser user1 = new AppUser("testuser", passwordEncoder.encode(userPassword), Role.USER);
				AppUser user2 = new AppUser("adminuser", passwordEncoder.encode(adminPassword), Role.ADMIN);

				userRepository.save(user1);
				userRepository.save(user2);

				System.out.println("🔐 DB_PASSWORD length: " + System.getenv("DB_PASSWORD").length());


			}
		};
	}
}

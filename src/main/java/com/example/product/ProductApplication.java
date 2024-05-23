package com.example.product;

import com.example.product.model.UserData;
import com.example.product.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}
    //add two users for test data
	@Bean
	CommandLineRunner init (UserRepository userRepository, PasswordEncoder passwordEncoder){
		return args -> {
			userRepository.save(new UserData(null, "user", passwordEncoder.encode("password"), "ROLE_USER"));
			userRepository.save(new UserData(null, "admin", passwordEncoder.encode("admin"), "ROLE_ADMIN"));

		};
	}

}

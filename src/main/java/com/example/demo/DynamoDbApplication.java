package com.example.demo;

		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.boot.SpringApplication;
		import org.springframework.boot.autoconfigure.SpringBootApplication;
		import org.springframework.scheduling.annotation.EnableScheduling;
		import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableScheduling
@Component
public class DynamoDbApplication {

	@Autowired
	UserCrudDao userCrudDao;
	public static void main(final String[] args) {
		SpringApplication.run(DynamoDbApplication.class, args);
	}
}

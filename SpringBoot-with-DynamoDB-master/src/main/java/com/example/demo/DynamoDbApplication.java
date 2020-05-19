package com.example.demo;

		import ch.qos.logback.core.status.StatusListener;
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.boot.SpringApplication;
		import org.springframework.boot.autoconfigure.SpringBootApplication;
		import org.springframework.scheduling.annotation.EnableScheduling;
		import org.springframework.scheduling.annotation.Scheduled;
		import org.springframework.stereotype.Component;

		import java.util.Arrays;
		import java.util.List;
		import java.util.Random;

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

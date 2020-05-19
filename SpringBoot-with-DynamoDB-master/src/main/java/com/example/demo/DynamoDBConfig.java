package com.example.demo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Configuration
public class DynamoDBConfig {

	@Autowired
	private Environment environment;

	@Bean
	public DynamoDBMapper dynamoDBMapper(DynamoDBMapperConfig mapperConfig) {
		return new DynamoDBMapper(amazonDynamoDB(), mapperConfig);
	}

	@Bean
	public DynamoDBMapperConfig mapperConfig(){

		DynamoDBMapperConfig.TableNameOverride tableNameOverride = DynamoDBMapperConfig.TableNameOverride.
				withTableNamePrefix(getActiveProfileName());
		return DynamoDBMapperConfig.builder().withTableNameOverride(tableNameOverride).build();
	}
	@Bean
	public AmazonDynamoDB amazonDynamoDB(){
		 AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials("AKIA4OFF7C7WOD7QOHEM", "E8OGiMLfgxxYORrQUZFn9Ie4ZfoH3ClWOa0PdWDG")))
				.withRegion(Regions.AP_SOUTH_1).build();
		 return client;
	}

	@Bean
	public DynamoDBScanExpression dynamoDBScanExpression(){
		return new DynamoDBScanExpression();
	}
	@Bean
	public String getActiveProfileName(){
		if(Arrays.stream(environment.getActiveProfiles()).anyMatch(
				env -> (env.equalsIgnoreCase("test")
						|| env.equalsIgnoreCase("dev"))))
		{
			return "dev";
		}
//Check if Active profiles contains "prod"
		else if(Arrays.stream(environment.getActiveProfiles()).anyMatch(
				env -> (env.equalsIgnoreCase("prod")) ))
		{
			return "prod";
		}
		return null;
	}
}
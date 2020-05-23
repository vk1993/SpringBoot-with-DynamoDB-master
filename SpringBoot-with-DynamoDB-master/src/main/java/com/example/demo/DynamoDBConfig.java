package com.example.demo;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Configuration
@Slf4j
public class DynamoDBConfig {

	@Autowired
	private Environment environment;

	@Value("${amazon.dynamodb.endpoint}")
	private String amazonDynamoDBEndpoint;

	@Value("${amazon.aws.accesskey}")
	private String amazonAWSAccessKey;

	@Value("${amazon.aws.secretkey}")
	private String amazonAWSSecretKey;

	@Bean
	public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB client, DynamoDBMapperConfig mapperConfig) {
		return new DynamoDBMapper(client, mapperConfig);
	}

	@Bean
	public DynamoDBMapperConfig mapperConfig(){
		DynamoDBMapperConfig.TableNameOverride tableNameOverride = DynamoDBMapperConfig.TableNameOverride.
				withTableNamePrefix(getActiveProfileName());
		return DynamoDBMapperConfig.builder().withTableNameOverride(tableNameOverride).build();
	}

	@Bean
	@Profile({"test","dev"})
	public AmazonDynamoDB amazonDynamoDB(){
		AmazonDynamoDB amazonDynamoDB
				= new AmazonDynamoDBClient(amazonAWSCredentials());

		if (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
			amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
		}

		 return amazonDynamoDB;
	}

	@Bean
	public AWSCredentials amazonAWSCredentials() {
		return new BasicAWSCredentials(
				amazonAWSAccessKey, amazonAWSSecretKey);
	}
	@Bean
	@Profile("prod")
	public AmazonDynamoDB amazonDynamoDBProd(){
		log.info("started prod");
		return AmazonDynamoDBClientBuilder.standard().build();
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
		return "prod";
	}
}
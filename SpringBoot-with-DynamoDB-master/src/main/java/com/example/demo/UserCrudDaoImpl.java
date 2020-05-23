package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserCrudDaoImpl implements UserCrudDao{
	@Autowired private DynamoDBMapper dynamoDBMapper;

	@Autowired private AmazonDynamoDB database;
	@Autowired private DynamoDBScanExpression dynamoDBScanExpression;
	@Autowired private DynamoDBConfig dynamoDBConfig;

	@Override
	public User createUser(User user) {
		dynamoDBMapper.save(user);
		return user;
	}
//31999665458
//	ifsc D visal kumar rao
	@Override
	public User readUser(String userId) {
		return dynamoDBMapper.load(User.class, userId);
	}

	@Override
	public User updateUser(User user) {
		Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<>();
		expectedAttributeValueMap.put("userId",
				new ExpectedAttributeValue(new AttributeValue().withS(user.getUserId())));

		expectedAttributeValueMap.entrySet().forEach(System.out::println);
		DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression().withExpected(expectedAttributeValueMap);
		dynamoDBMapper.save(user, saveExpression);
		return user;
	}

	@Override
	public void deleteUser(String userId) {
		Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<>();
		expectedAttributeValueMap.put("userId", new ExpectedAttributeValue(new AttributeValue().withS(userId)));
		DynamoDBDeleteExpression deleteExpression = new DynamoDBDeleteExpression()
				.withExpected(expectedAttributeValueMap);
		User user = dynamoDBMapper.load(User.class, userId);
		  dynamoDBMapper.delete(user, deleteExpression);
	}

	@Override
	public Status updateStatus(Status status) throws InterruptedException {
		String tablename = dynamoDBConfig.getActiveProfileName()+ "health-check";
		log.info(tablename);
		createTable(tablename);
				if(isEmpty(tablename)){
					System.out.println("---------------->>>>" +status.getESIMhealthCheckStatus());
					dynamoDBMapper.save(status);
				}else {
					System.out.println("-->>>>>>>>>>>>>>>>>>>>>>>>>>" +status.getESIMhealthCheckStatus());
					String healthCheckID = getHealthCheckID();
					Map<String, ExpectedAttributeValue> expectedAttributeValueMap = new HashMap<>();
					expectedAttributeValueMap.put("healthCheckID",
							new ExpectedAttributeValue(new AttributeValue().withS(healthCheckID)));
					expectedAttributeValueMap.entrySet().forEach(System.out::println);
					DynamoDBSaveExpression saveExpression = new DynamoDBSaveExpression().withExpected(expectedAttributeValueMap);
					status.setHealthCheckID(healthCheckID);
					dynamoDBMapper.save(status,saveExpression);
				}
		return status;
	}

	@Override
	public JsonData readStatus() {
		Status response = dynamoDBMapper.load(Status.class, getHealthCheckID());
		JsonData converted = new JsonData();
		converted.setData(Converter.toNode(response));
		//converted.data.put("eSIMhealthCheckStatus",response.getESIMhealthCheckStatus())
		//		.put("TimeStamp",response.getTimeStamp()).put("status", String.valueOf(HttpStatus.OK));
		return converted;
	}

	public Boolean isEmpty(String tableName) {
		ScanRequest scanRequest = new ScanRequest().withTableName(tableName).withLimit(1);
		System.out.println(database.scan(scanRequest).getItems());
		return database.scan(scanRequest).getCount() == 0;
	}
	public String getHealthCheckID(){
		log.info("gethealthcheck");
		PaginatedScanList<Status> scanResult = dynamoDBMapper.scan(Status.class,dynamoDBScanExpression);
		String healthCheckID = scanResult.stream().findFirst().get().getHealthCheckID();
		return healthCheckID;
	}

	public String createTable(String tableName) throws InterruptedException {
		List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
		attributeDefinitions.add(new AttributeDefinition().withAttributeName("healthCheckID").withAttributeType("S"));

		List<KeySchemaElement> keySchemaElements = new ArrayList<>();
		keySchemaElements.add(new KeySchemaElement().withAttributeName("healthCheckID").withKeyType("S"));

		ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput().withReadCapacityUnits(2L).withWriteCapacityUnits(2L);

		CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName).withAttributeDefinitions(attributeDefinitions).withKeySchema(keySchemaElements)
				.withProvisionedThroughput(provisionedThroughput);
		if(!TableUtils.createTableIfNotExists(database,createTableRequest)){
			log.info("Table is already exist Nothing to do",tableName);
		}
		else{
			TableUtils.waitUntilExists(database,tableName);
		}
		return "created";
	}

}

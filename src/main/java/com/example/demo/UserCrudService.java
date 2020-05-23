package com.example.demo;

import java.util.Stack;

public interface UserCrudService {

	User createUser(User user);

	User readUser(String userId);

	JsonData readStatus();

	void deleteUser(String userId);

	User updateUser(User user);

	Status updateStatus(Status status) throws InterruptedException;

}

package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCrudServiceImpl implements UserCrudService{

	 private UserCrudDao userCrudDao;

	    @Autowired
	    public UserCrudServiceImpl(UserCrudDao userCrudDao) {
	        this.userCrudDao = userCrudDao;
	    }

	    @Override
	    public User createUser(User user) {
	        return userCrudDao.createUser(user);
	    }

	    @Override
	    public User readUser(String userId) {
	        return userCrudDao.readUser(userId);
	    }

	    @Override
	    public User updateUser(User user) {
	        return userCrudDao.updateUser(user);
	    }

		@Override
		public Status updateStatus(Status status) throws InterruptedException {
			return userCrudDao.updateStatus(status);
		}

	@Override
	public JsonData readStatus() {
		return userCrudDao.readStatus();
	}

	@Override
	    public void deleteUser(String userId) {
	        userCrudDao.deleteUser(userId);
	    }

}

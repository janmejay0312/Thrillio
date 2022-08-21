package com.janmejay.thrillio.manager;

import java.util.List;

import com.janmejay.thrillio.constants.Gender;
import com.janmejay.thrillio.constants.UserType;
import com.janmejay.thrillio.dao.UserDao;
import com.janmejay.thrillio.entities.User;

public class UserManager {
	private static UserManager instance = new UserManager();
    private static UserDao dao=new UserDao();
	private UserManager() {
	}

	public static UserManager getInstance() {
		return instance;
	}

	public User createUser(long id, String email, String password, String firstName, String lastName, UserType userType,
			Gender gender) {
		User user = new User();
		user.setId(id);
		user.setEmail(email);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUserType(userType);
		user.setGender(gender);
		return user;
	}
public  List<User> getUser() {
	return dao.getUser();
}
}

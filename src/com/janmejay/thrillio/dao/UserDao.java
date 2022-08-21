package com.janmejay.thrillio.dao;

import java.util.List;

import com.janmejay.thrillio.DataStore;
import com.janmejay.thrillio.entities.User;

public class UserDao {
public List<User> getUser() {
	return DataStore.getUser();
}
}

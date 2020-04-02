package com.znsd.dao;

import java.util.List;

import com.znsd.bean.User;

public interface UserMapper {
	List<User> findAll();
	
	User findByUser(Integer uId);
}

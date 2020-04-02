package com.znsd.dao;

import java.util.List;

import com.znsd.bean.Account;

public interface AccountMapper {
	List<Account> findAll();
	
	Account findById(Integer aId);
}

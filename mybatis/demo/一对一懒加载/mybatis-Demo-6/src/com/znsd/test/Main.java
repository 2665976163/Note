package com.znsd.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.znsd.bean.Account;
import com.znsd.bean.User;
import com.znsd.dao.AccountMapper;
import com.znsd.dao.UserMapper;

public class Main {
	public static void main(String[] args) throws IOException {
		InputStream resource = Resources.getResourceAsStream("SqlMapConfig.xml");
		SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resource);
		SqlSession session = build.openSession();
		//accountLazy(session);
		//userAndAccountLazy(session);
	}
	//用户级联账户懒加载
	private static void userAndAccountLazy(SqlSession session) {
		UserMapper mapper = session.getMapper(UserMapper.class);
		List<User> findAll = mapper.findAll();
		System.out.println(findAll);
	}
	//账户级联用户懒加载
	private static void accountAndUserLazy(SqlSession session) {
		AccountMapper mapper = session.getMapper(AccountMapper.class);
		List<Account> findAll = mapper.findAll();
		System.out.println(findAll);
	}
}

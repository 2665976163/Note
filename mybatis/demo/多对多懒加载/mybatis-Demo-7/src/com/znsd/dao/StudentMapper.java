package com.znsd.dao;

import java.util.List;

import com.znsd.bean.MyStudent;

public interface StudentMapper {
	List<MyStudent> findAll();
	
	MyStudent findById();
}

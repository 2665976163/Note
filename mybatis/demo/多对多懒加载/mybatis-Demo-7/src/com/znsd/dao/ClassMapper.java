package com.znsd.dao;

import java.util.List;

import com.znsd.bean.MyClass;

public interface ClassMapper {
	List<MyClass> findAll();
	
	MyClass findById();
}

package com.znsd.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.znsd.bean.MyClass;
import com.znsd.bean.MyStudent;
import com.znsd.dao.ClassMapper;
import com.znsd.dao.StudentMapper;

public class Test {
	/**
	 * 当前多对多存在问题
	 * 若想使用多对多则需要建立中间表的接口与映射
	 */
	public static void main(String[] args) throws Exception {
		InputStream resource = Resources.getResourceAsStream("SqlMapConfig.xml");
		SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resource);
		SqlSession session = build.openSession();
		//curriculumAndStuLazy(session);
		//stuAndCurriculumLazy(session);
	}
	//学生与课程延迟加载
	private static void stuAndCurriculumLazy(SqlSession session) {
		StudentMapper mapper = session.getMapper(StudentMapper.class);
		List<MyStudent> findAll = mapper.findAll();
		System.out.println(findAll);
	}
	//课程与学生延迟加载
	private static void curriculumAndStuLazy(SqlSession session) {
		ClassMapper mapper = session.getMapper(ClassMapper.class);
		List<MyClass> findAll = mapper.findAll();
		System.out.println(findAll);
	}
	
}

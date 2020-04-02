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
	 * ��ǰ��Զ��������
	 * ����ʹ�ö�Զ�����Ҫ�����м��Ľӿ���ӳ��
	 */
	public static void main(String[] args) throws Exception {
		InputStream resource = Resources.getResourceAsStream("SqlMapConfig.xml");
		SqlSessionFactory build = new SqlSessionFactoryBuilder().build(resource);
		SqlSession session = build.openSession();
		//curriculumAndStuLazy(session);
		//stuAndCurriculumLazy(session);
	}
	//ѧ����γ��ӳټ���
	private static void stuAndCurriculumLazy(SqlSession session) {
		StudentMapper mapper = session.getMapper(StudentMapper.class);
		List<MyStudent> findAll = mapper.findAll();
		System.out.println(findAll);
	}
	//�γ���ѧ���ӳټ���
	private static void curriculumAndStuLazy(SqlSession session) {
		ClassMapper mapper = session.getMapper(ClassMapper.class);
		List<MyClass> findAll = mapper.findAll();
		System.out.println(findAll);
	}
	
}

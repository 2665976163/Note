package com.znsd.bean;

import java.util.List;

public class MyClass {
	private Integer cId;
	private String cName;
	private List<MyStudent> stuList;
	public Integer getcId() {
		return cId;
	}
	public void setcId(Integer cId) {
		this.cId = cId;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public List<MyStudent> getStuList() {
		return stuList;
	}
	public void setStuList(List<MyStudent> stuList) {
		this.stuList = stuList;
	}
	
	@Override
	public String toString() {
		return "MyClass [cId=" + cId + ", cName=" + cName + ", stuList=" + stuList + "]\n";
	}
	
}

package com.znsd.bean;

import java.util.List;

public class MyStudent {
	private Integer sId;
	private String sName;
	private List<MyClass> clsList;
	public Integer getsId() {
		return sId;
	}
	public void setsId(Integer sId) {
		this.sId = sId;
	}
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public List<MyClass> getClsList() {
		return clsList;
	}
	public void setClsList(List<MyClass> clsList) {
		this.clsList = clsList;
	}
	@Override
	public String toString() {
		return "MyStudent [sId=" + sId + ", sName=" + sName + ", clsList=" + clsList + "]";
	}
	
}

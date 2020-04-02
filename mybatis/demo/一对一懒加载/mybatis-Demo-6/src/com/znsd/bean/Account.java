package com.znsd.bean;

public class Account {
	private Integer aId;
	private String aName;
	private User user;
	public Integer getaId() {
		return aId;
	}
	public void setaId(Integer aId) {
		this.aId = aId;
	}
	public String getaName() {
		return aName;
	}
	public void setaName(String aName) {
		this.aName = aName;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Account [aId=" + aId + ", aName=" + aName + ", user=" + user + "]";
	}
	
}

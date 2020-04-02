package com.znsd.bean;

public class User {
	private Integer uId;
	private String uName;
	private Account account;
	public Integer getuId() {
		return uId;
	}
	public void setuId(Integer uId) {
		this.uId = uId;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	@Override
	public String toString() {
		return "User [uId=" + uId + ", uName=" + uName + ", account=" + account + "]";
	}
	
}

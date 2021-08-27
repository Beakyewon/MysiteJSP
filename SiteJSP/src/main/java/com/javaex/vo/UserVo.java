package com.javaex.vo;

public class UserVo {
	private long id;
	private String name;
	private String password;
	private String gender;
	private String email;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public UserVo(long id, String name, String password, String gender, String email) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.gender = gender;
		this.email = email;
	}
	public UserVo(String name, String password, String gender, String email) {
		super();
		this.name = name;
		this.password = password;
		this.gender = gender;
		this.email = email;
	}
	public UserVo() {}
	
	public UserVo(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}	
}

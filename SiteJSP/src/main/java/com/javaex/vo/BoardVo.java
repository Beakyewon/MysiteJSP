package com.javaex.vo;

public class BoardVo {
	
	private int 	num;
	private int 	id;
	private String 	title;
	private String 	content;
	private int 	hit;
	private String 	reg_date;
	private long 	user_id;
	private String	user_name;
	private String  filename;
	private String  filename2;
	
	
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getFilename2() {
		return filename2;
	}
	public void setFilename2(String filename2) {
		this.filename2 = filename2;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getUserName() {
		return user_name;
	}
	public void setUserName(String user_name) {
		this.user_name = user_name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	
	@Override
	public String toString() {
		return "BoardVo [num=" + num + ", id=" + id + ", title=" + title + ", content=" + content + ", hit=" + hit
				+ ", reg_date=" + reg_date + ", user_id=" + user_id + ", user_name=" + user_name + ", filename="
				+ filename + ", filename2=" + filename2 + "]";
	}
	public BoardVo(int id, String title, String content) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
	}
	public BoardVo(String title, String content, long user_id) {
		super();
		this.title = title;
		this.content = content;
		this.user_id = user_id;
	}
	public BoardVo(int num, int id, String title, String content, int hit, String reg_date, long user_id, String user_name, String filename, String filename2) {
		super();
		this.num = num;
		this.id = id;
		this.title = title;
		this.content = content;
		this.hit = hit;
		this.reg_date = reg_date;
		this.user_id = user_id;
		this.user_name = user_name;
		this.filename = filename;
		this.filename2 = filename2;
		
	}
	public BoardVo(int id, String title, String content, int hit, String reg_date, long user_id, String user_name, String filename, String filename2) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.hit = hit;
		this.reg_date = reg_date;
		this.user_id = user_id;
		this.user_name = user_name;
		this.filename = filename;
		this.filename2 = filename2;
		
	}

	public BoardVo() {}
	
	
	
	
	
	

}

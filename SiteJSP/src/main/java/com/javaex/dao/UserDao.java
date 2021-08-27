package com.javaex.dao;

import java.util.List;

import com.javaex.vo.UserVo;

public interface UserDao {
	
	public List<UserVo> getList();
	
	public int insert(UserVo vo);
	
	public int update(UserVo vo);

	public UserVo find(String email, String password);
	
	public UserVo getUser(long id);
	
	public boolean EmailCheck(String email);
}

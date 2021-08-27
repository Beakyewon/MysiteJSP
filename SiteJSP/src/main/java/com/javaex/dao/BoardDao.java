package com.javaex.dao;

import java.util.List;

import com.javaex.vo.BoardVo;

public interface BoardDao {

	public List<BoardVo> getList(int start, int end);
	
	public List<BoardVo> getList(String keyfield, String search, int start, int end);
	
	public BoardVo getBoard(int no);
	
	public int insert(BoardVo vo);
	
	public int update(BoardVo vo);
	
	public void delete(int no);
	
	public int updateHit(int id, int hit);
	
	int getTotalCount(String keyField, String search);


	
}

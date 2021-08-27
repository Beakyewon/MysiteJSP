package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDaoImpl implements BoardDao {
  private Connection getConnection() throws SQLException {
    Connection conn = null;
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      String dburl = "jdbc:oracle:thin:@192.168.56.1:1521:xe";
      conn = DriverManager.getConnection(dburl, "c##web_db", "1234");
    } catch (ClassNotFoundException e) {
      System.err.println("JDBC 드라이버 로드 실패!");
    }
    return conn;
  }
  // 전체리스트 가져오기
	public List<BoardVo> getList(int start, int end) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();

		try {
			conn = getConnection();
				String query = "select * from ( select row_number() over (order by b.reg_date) num, b.*, u.name from board b, users u WHERE b.USER_ID = u.ID order by b.reg_date DESC) "
							 + "where num between ? and ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, start);
				pstmt.setInt(2, end);
	
				rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int num = rs.getInt("num");
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String content = rs.getString("content");
				long userId = rs.getInt("user_id");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				String userName = rs.getString("name");
				String filename = rs.getString("filename");
				String filename2 = rs.getString("filename2");


				BoardVo vo = new BoardVo(num, id, title, content, hit, regDate, userId, userName, filename, filename2);
				System.out.println(vo);
				list.add(vo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null)  conn.close();
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}

		return list;
	}
	// 검색 기능
	public List<BoardVo> getList(String keyField, String search, int start, int end) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVo> list = new ArrayList<BoardVo>();
		String query = null;

		try {
			conn = getConnection();
			
			if(keyField.equals("regdate")) {
				query = "select * from ( select row_number() over (order by b.reg_date) num, "
						+ "    					 b.*, u.name from board b, users u WHERE b.USER_ID = u.ID  "
						+ "						and b.reg_date between to_date(?, 'YYYYMMDD') and to_date(?, 'YYYYMMDD')+1 "
						+ "						order by b.reg_date DESC ) "
						+ "where num between ? and ?" ;
						pstmt = conn.prepareStatement(query);
						pstmt.setString(1, search);
						pstmt.setString(2, search);
						pstmt.setInt(3, start);
						pstmt.setInt(4, end);
			}else {
				query = "select * from ( select row_number() over (order by b.reg_date) num, "
						+ "						 b.*, u.name from board b, users u WHERE b.USER_ID = u.ID  "
						+ "						 and "+ keyField +" LIKE ? order by b.reg_date DESC) "
						+ "where num between ? and ?" ;
						pstmt = conn.prepareStatement(query);
						pstmt.setString(1, "%"+search+"%");
						pstmt.setInt(2, start);
						pstmt.setInt(3, end);
			}
			rs = pstmt.executeQuery();
				
			while (rs.next()) {
				int num = rs.getInt("num");
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String content = rs.getString("content");
				long userId = rs.getInt("user_id");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				String userName = rs.getString("name");
				String filename = rs.getString("filename");
				String filename2 = rs.getString("filename2");

				BoardVo vo = new BoardVo(num, id, title, content, hit, regDate, userId, userName, filename, filename2);
				list.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null)  conn.close();
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}
		return list;
	}
	// 페이징 기능
	public int getTotalCount(String keyField, String search) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = null;
		int totalCount = 0;

		try {
			conn = getConnection();
			if (search == null || search == "") {
				query = "select count(id) from board";
				pstmt = conn.prepareStatement(query);
				
			} else {
				if(keyField.equals("regdate")) {
					query = "select count(id) from ( select row_number() over (order by b.reg_date) num, b.*, u.name "
							+ "    					from board b, users u WHERE b.USER_ID = u.ID  "
							+ "						order by b.reg_date DESC ) "
							+ "where reg_date between to_date(?, 'YYYYMMDD') and to_date(?, 'YYYYMMDD')+1 " ;
					
							pstmt = conn.prepareStatement(query);
							pstmt.setString(1, search);
							pstmt.setString(2, search);
				} else {
					query = "select count(id) from ( select row_number() over (order by b.reg_date) num, b.*, u.name "
										+ "				from board b, users u "
										+ "				WHERE b.USER_ID = u.ID  "
										+ "				order by b.reg_date DESC ) "
							+ "where "+keyField+" LIKE ? ";
						  
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, "%" + search + "%");
				}
			}
			rs = pstmt.executeQuery();
			if (rs.next()) {
				totalCount = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (conn != null)  conn.close();
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
		}
		return totalCount;
	}
	
	public int insert(BoardVo vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
			
			conn = getConnection();

			String query = "insert into Board(id, user_id, title, content, hit, reg_date, filename, filename2) "
						 + "values (seq_rboard_no.nextval, ?, ?, ?, 0, sysdate, ?, ?)";
			pstmt = conn.prepareStatement(query);

			pstmt.setLong(1, vo.getUser_id());
			pstmt.setString(2, vo.getTitle());
			pstmt.setString(3, vo.getContent());
			pstmt.setString(4, vo.getFilename());
			pstmt.setString(5, vo.getFilename2());
			
			count = pstmt.executeUpdate();

			System.out.println(count + "건 등록");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
      try {
        if (pstmt != null) pstmt.close();
        if (conn != null)  conn.close();
      } catch (SQLException e) {
        System.out.println("error:" + e);
      }
    }
		return count;
	}
	
	public int update(BoardVo vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
			conn = getConnection();

			String query = "update board set title =?, content =?, filename =?, filename2 = ? where id = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContent());
			pstmt.setString(3, vo.getFilename());
			pstmt.setString(4, vo.getFilename2());
			pstmt.setInt(5, vo.getId());

			count = pstmt.executeUpdate();

			System.out.println(count + "건 수정");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
		  try {
        if (pstmt != null) pstmt.close();
        if (conn != null)  conn.close();
      } catch (SQLException e) {
        System.out.println("error:" + e);
      }
		}
		return count;
	}

	public int updateHit(int id, int hit) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
			conn = getConnection();

			String query = "update board set hit =? where id = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, hit);
			pstmt.setInt(2, id);

			count = pstmt.executeUpdate();

			System.out.println(count + "건 수정");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
		  try {
        if (pstmt != null) pstmt.close();
        if (conn != null)  conn.close();
      } catch (SQLException e) {
        System.out.println("error:" + e);
      }
		}
		return count;
	}
	
	public void delete(int id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
			conn = getConnection();

			String query = "delete from board where id= ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setLong(1, id);

			count = pstmt.executeUpdate();

			System.out.println(count + "건 삭제");

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
		  try {
	        if (pstmt != null) pstmt.close();
	        if (conn != null)  conn.close();
	    } catch (SQLException e) {
	        System.out.println("error:" + e);
	    }
	  }
	}
	
	public BoardVo getBoard(int id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVo vo = new BoardVo();

		try {
			conn = getConnection();

			String query = "select b.title, b.content, b.user_id, b.hit, b.reg_date, b.filename, b.filename2, u.name "
						 + "from board b, users u "
						 + "where b.user_id = u.id "
						 + "and b.id = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, id);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {

				String title = rs.getString("title");
				String content = rs.getString("content");
				long userId = rs.getLong("user_id");
				int hit = rs.getInt("hit");
				String regDate = rs.getString("reg_date");
				String userName = rs.getString("name");
				String filename = rs.getString("filename");
				String filename2 = rs.getString("filename2");
				
				 vo = new BoardVo(id, title, content, hit, regDate, userId, userName, filename, filename2);
			}

			System.out.println(vo);

		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
      try {
        if (pstmt != null) pstmt.close();
        if (conn != null)  conn.close();
      } catch (SQLException e) {
        System.out.println("error:" + e);
      }
    }
		return vo;
	}

}

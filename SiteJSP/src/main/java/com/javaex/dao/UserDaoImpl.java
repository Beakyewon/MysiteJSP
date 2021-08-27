package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.UserVo;

public class UserDaoImpl implements UserDao {
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
  
	public List<UserVo> getList() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<UserVo> list = new ArrayList<UserVo>();

		try {
			conn = getConnection();

			String query = " select id, name, password, gender, email " + 
			               " from users "+
					       " order by id desc ";
			pstmt = conn.prepareStatement(query);

			rs = pstmt.executeQuery();
			// 4.결과처리
			while (rs.next()) {
				long id = rs.getInt("id");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String gender = rs.getString("gender");
				String email = rs.getString("email");

				UserVo vo = new UserVo(id, name, password, gender, email);
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
	
	public int insert(UserVo vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
			
			conn = getConnection();

			String query = "insert into users "
					+ "values (seq_users_no.nextval, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());
			

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
	
	public int update(UserVo vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
			conn = getConnection();
			if(vo.getPassword()=="") {
				String query = "update users set name =?, gender =? where id = ?";
	
				pstmt = conn.prepareStatement(query);
	
				pstmt.setString(1, vo.getName());
				pstmt.setString(2, vo.getGender());
				pstmt.setLong(3, vo.getId());
				
	
				count = pstmt.executeUpdate();
	
				System.out.println(count + "건 수정");
			}else {
				String query = "update users set name =?, password =?, gender =? where id = ?";
				
				pstmt = conn.prepareStatement(query);
	
				pstmt.setString(1, vo.getName());
				pstmt.setString(2, vo.getPassword());
				pstmt.setString(3, vo.getGender());
				pstmt.setLong(4, vo.getId());
				
	
				count = pstmt.executeUpdate();
	
				System.out.println(count + "건 수정");
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
		return count;
	}

	public int delete(UserVo vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int count = 0;

		try {
			conn = getConnection();

			String query = "delete from Users where id= ? and password= ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setLong(1, vo.getId());
			pstmt.setString(2, vo.getPassword());

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
		return count;
	}
	
	public UserVo find(String email, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVo vo = null;

		try {
			conn = getConnection();

			String query = "select id, name "
						 + "from users "
						 + "where email = ? and password = ?";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, email);
			pstmt.setString(2, password);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				long id = rs.getLong("id");
				System.out.println(id);
				String name = rs.getString("name");
				System.out.println(name);
				
				 vo = new UserVo(id, name);
				 
				 /*
				  UserVo vo2 = new UserVo();
				  vo2.setId(id);
				  vo2.setName(name);
				  생성자 선언을 다시할 필요없이 이렇게 할 수도 있다. 
				  */
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
	
	public UserVo getUser(long id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		UserVo vo = null;

		try {
			conn = getConnection();

			String query = "select name, email, gender, password "
						 + "from users "
						 + "where id = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setLong(1, id);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				
				String name = rs.getString("name");
				String email = rs.getString("email");
				String gender = rs.getString("gender");
				String password = rs.getString("password");

				
				 vo = new UserVo(id, name, password, gender, email);
				 
				 /*
				  UserVo vo2 = new UserVo();
				  vo2.setId(id);
				  vo2.setName(name);
				  생성자 선언을 다시할 필요없이 이렇게 할 수도 있다. 
				  */
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

	public boolean EmailCheck(String email) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean x = false;

		try {
			conn = getConnection();

			String query = "select id, name, gender, password "
						 + "from users "
						 + "where email = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, email);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) x = true;
				 
				 /*
				  UserVo vo2 = new UserVo();
				  vo2.setId(id);
				  vo2.setName(name);
				  생성자 선언을 다시할 필요없이 이렇게 할 수도 있다. 
				  */
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
		return x;
	}
}

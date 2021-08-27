package com.javaex.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.dao.UserDaoImpl;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;

@WebServlet("/join")
public class JoinServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String actionName = request.getParameter("a");
		System.out.println("join:" + actionName);
		
		if ("join".equals(actionName)) {
			String name 	= request.getParameter("name");
			String email	= request.getParameter("email");
			String password = request.getParameter("password");
			String gender	= request.getParameter("gender");
			String agree	= request.getParameter("agreeProv");
			System.out.println(name);
			System.out.println(email);
			System.out.println(password);
			System.out.println(gender);
			System.out.println(agree);
			
		  	UserVo vo = new UserVo(name, password, gender, email);
		  	UserDao dao = new UserDaoImpl();
		  
		  	int count = dao.insert(vo);
		  		
			if(count >= 1){
				response.sendRedirect("/SiteJSP/join?a=joinsuccess");
			}

		} else if ("joinform".equals(actionName)) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinform.jsp");
			rd.forward(request, response);

		}else if ("joinsuccess".equals(actionName)) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinsuccess.jsp");
			rd.forward(request, response);
		
		} else if ("loginform".equals(actionName)) {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/loginform.jsp");
			rd.forward(request, response);
			
		}else if ("login".equals(actionName)) {
			String email = request.getParameter("email");
			System.out.println(email);
			String password = request.getParameter("password");
			System.out.println(password);
			
			UserDao dao = new UserDaoImpl();
			
			UserVo vo = dao.find(email, password);

			if( vo != null ){
				HttpSession session = request.getSession(true);
				session.setAttribute("authUser", vo);
				response.sendRedirect("/SiteJSP/main");
		    }else {
		    	System.out.println("실패");
		    	response.sendRedirect("/SiteJSP/join?a=loginform&result=fail");
		    }
		}else if("logout".equals(actionName)) {
			HttpSession session = request.getSession();
			session.removeAttribute("authUser");
			session.invalidate();
			response.sendRedirect("/SiteJSP/main");
			
		}else if("modify".equals(actionName)){	
			
			String name 	= request.getParameter("name");
			String password = request.getParameter("password");
			String gender	= request.getParameter("gender");
			
		  	UserVo vo = new UserVo();
		  	vo.setName(name);
		  	vo.setPassword(password);
		  	vo.setGender(gender);
		  	
		  	HttpSession session = request.getSession();
		  	UserVo authUser = (UserVo)session.getAttribute("authUser");
		  	
		  	long no = authUser.getId();
		  	vo.setId(no);
		  	
		  	UserDao dao = new UserDaoImpl();
		  	dao.update(vo);
		  	
		  	authUser.setName(name);
		  	
		  	RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/main/index.jsp");
			rd.forward(request, response);
			
		}else if("modifyform".equals(actionName)){	
			
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			long no = authUser.getId();
			
			UserDao dao = new UserDaoImpl();
			UserVo userVo = dao.getUser(no);
			System.out.println(userVo.toString());
			
			request.setAttribute("userVo", userVo);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/modifyform.jsp");
			rd.forward(request, response);
		
		} else if ("idcheckform".equals(actionName)) {
			
			String email = request.getParameter("email");
			
			request.setAttribute("email", email);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/idcheckform.jsp");
			rd.forward(request, response);		
			
		}else if("idcheck".equals(actionName) ){
				 
		        String email = request.getParameter("email");
		        System.out.println(email);
		        UserDao dao = new UserDaoImpl();
		        
		        boolean result = dao.EmailCheck(email);
		        System.out.println(result);
		        
		        request.setAttribute("email", email);

				if( result == true ){
					System.out.println("실패");
			    	response.sendRedirect("/SiteJSP/join?a=joinform&result=fail");

			    }else {
			    	System.out.println("성공");
			    	request.setAttribute("email", email);
			    	RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/user/joinform.jsp");
					rd.forward(request, response);

			    }
		    
		}else {
			
			WebUtil.redirect(request, response, "/SiteJSP/main");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}

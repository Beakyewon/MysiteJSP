package com.javaex.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.dao.BoardDaoImpl;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@WebServlet("/board")
public class BoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String actionName = request.getParameter("a");
		System.out.println("board:" + actionName);

		if ("list".equals(actionName)) {
			// 리스트 가져오기
			int numPerPage = 10;
			int nowPage = 1;
			if(request.getParameter("nowPage") != null || request.getParameter("nowPage")=="") {
				nowPage = Integer.parseInt(request.getParameter("nowPage"));
			}

			String keyField = request.getParameter("keyField"); //검색 분류 받아옴
			String search = request.getParameter("kwd"); // 검색 키워드 받아옴
			
			BoardDao dao = new BoardDaoImpl();
			int totalCount = dao.getTotalCount(keyField, search);

			int end = totalCount -(nowPage-1)*(numPerPage);
			int start = 0;
			if(end >10) {
				start = end - numPerPage + 1;
			}else {
				start = 1;
			}
			if(search == null || search == "" || search.equals("")) {
				List<BoardVo> list = dao.getList(start, end);
				
				request.setAttribute("list", list);
				request.setAttribute("totalCount", totalCount);
				System.out.println("totalCount:"+totalCount);
				WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
				
			} else {
				List<BoardVo> list = dao.getList(keyField, search, start, end);
				request.setAttribute("list", list);
				request.setAttribute("totalCount", totalCount);
				
				WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			}
		
		} else if ("read".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			int hit = Integer.parseInt(request.getParameter("hit"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);
			
			dao.updateHit(no, hit);
			
			System.out.println(boardVo.toString());

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
			
		} else if ("modifyform".equals(actionName)) {
			// 게시물 가져오기
			int no = Integer.parseInt(request.getParameter("no"));
			BoardDao dao = new BoardDaoImpl();
			BoardVo boardVo = dao.getBoard(no);

			// 게시물 화면에 보내기
			request.setAttribute("boardVo", boardVo);
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyform.jsp");
			
		} else if ("modify".equals(actionName)) {
			// 게시물 가져오기
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int no = Integer.parseInt(request.getParameter("id"));
			
			BoardVo vo = new BoardVo(no, title, content);
			BoardDao dao = new BoardDaoImpl();
			
			dao.update(vo);
			
			WebUtil.redirect(request, response, "/SiteJSP/board?a=list");
			
		} else if ("writeform".equals(actionName)) {
			// 로그인 여부체크
			UserVo authUser = getAuthUser(request);
			if (authUser != null) { // 로그인했으면 작성페이지로
				WebUtil.forward(request, response, "/WEB-INF/views/board/writeform.jsp");
			} else { // 로그인 안했으면 리스트로
				WebUtil.redirect(request, response, "/SiteJSP/board?a=list");
			}

		} else if ("write".equals(actionName)) {
			UserVo authUser = getAuthUser(request);

			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			long userNo = authUser.getId();
			System.out.println("userNo : ["+userNo+"]");
			System.out.println("title : ["+title+"]");
			System.out.println("content : ["+content+"]");

			BoardVo vo = new BoardVo(title, content, userNo);
			BoardDao dao = new BoardDaoImpl();
			dao.insert(vo);

			WebUtil.redirect(request, response, "/SiteJSP/board?a=list");

		} else if ("delete".equals(actionName)) {
			int no = Integer.parseInt(request.getParameter("no"));

			BoardDao dao = new BoardDaoImpl();
			dao.delete(no);

			WebUtil.redirect(request, response, "/SiteJSP/board?a=list");
			
		} else if ("writeUpload".equals(actionName)) {
			
				String saveFolder = ""; //저장 경로
				saveFolder = "C:\\Users\\LGPC\\javaStudy\\eclipse_workspace\\SiteJSP\\WebContent\\upload";
				int fileSize = 400 * 1024 * 1024; //400MB
				
				UserVo authUser = getAuthUser(request);
				long userNo = authUser.getId();
				BoardVo vo = new BoardVo();
				
				MultipartRequest multi = null; //파일 업로드 처리
				DefaultFileRenamePolicy policy = new DefaultFileRenamePolicy(); //중복 파일 명 방지
				multi = new MultipartRequest(request, saveFolder, fileSize, "utf-8", policy);
				
				vo.setUser_id(userNo);
				vo.setTitle(multi.getParameter("title"));
				vo.setContent(multi.getParameter("content"));
				
				ArrayList saveFiles = new ArrayList();
				ArrayList origFiles = new ArrayList();
				
				Enumeration files = multi.getFileNames();
				while(files.hasMoreElements()){
				   String name = (String)files.nextElement();
				   saveFiles.add(multi.getFilesystemName(name));
				   origFiles.add(multi.getOriginalFileName(name));
				}
				vo.setFilename((String)saveFiles.get(0));
				vo.setFilename2((String)saveFiles.get(1));
				
				
				System.out.println(vo.getFilename());
				System.out.println(vo.getFilename2());
				
				
				BoardDao dao = new BoardDaoImpl();
				dao.insert(vo);
				
				WebUtil.redirect(request, response, "/SiteJSP/board?a=list");
				
		} else if ("updateUpload".equals(actionName)) {
			
			String saveFolder = ""; //저장 경로
			saveFolder = "C:\\Users\\LGPC\\javaStudy\\eclipse_workspace\\SiteJSP\\WebContent\\upload";
			int fileSize = 400 * 1024 * 1024; //400MB
			
			int no = Integer.parseInt(request.getParameter("id"));
			BoardVo vo = new BoardVo();
			
			MultipartRequest multi = null; //파일 업로드 처리
			DefaultFileRenamePolicy policy = new DefaultFileRenamePolicy(); //중복 파일 명 방지
			multi = new MultipartRequest(request, saveFolder, fileSize, "utf-8", policy);
			
			vo.setUser_id(no);
			vo.setTitle(multi.getParameter("title"));
			vo.setContent(multi.getParameter("content"));
			
			ArrayList saveFiles = new ArrayList();
			ArrayList origFiles = new ArrayList();
			
			Enumeration files = multi.getFileNames();
			while(files.hasMoreElements()){
			   String name = (String)files.nextElement();
			   saveFiles.add(multi.getFilesystemName(name));
			   origFiles.add(multi.getOriginalFileName(name));
			}
			
			vo.setFilename((String)saveFiles.get(0));
			vo.setFilename2((String)saveFiles.get(1));
			
			
			System.out.println(vo.getFilename());
			System.out.println(vo.getFilename2());
			
			
			BoardDao dao = new BoardDaoImpl();
			dao.update(vo);
			
			WebUtil.redirect(request, response, "/SiteJSP/board?a=list");


		} else {
			WebUtil.redirect(request, response, "/SiteJSP/board?a=list");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	// 로그인 되어 있는 정보를 가져온다.
	protected UserVo getAuthUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserVo authUser = (UserVo) session.getAttribute("authUser");

		return authUser;
	}

}

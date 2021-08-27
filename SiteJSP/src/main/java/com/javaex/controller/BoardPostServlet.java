package com.javaex.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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


/**
 * Servlet implementation class BoardPostServlet
 */
@WebServlet("/boardpost")
public class BoardPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String  SAVEFOLDER = "C:\\Users\\LGPC\\javaStudy\\eclipse_workspace\\SiteJSP\\WebContent\\upload\\";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String actionName = request.getParameter("a");
		System.out.println("boardpost:" + actionName);

		if ("download".equals(actionName)) {
			// 리스트 가져오기
			try {
				String filename = request.getParameter("filename");
				File file = new File(SAVEFOLDER+filename);
				System.out.println(file);
				byte b[] = new byte[(int) file.length()];
				response.setHeader("Accept-Ranges", "bytes");
				String strClient = request.getHeader("User-Agent");
				
				if (strClient.indexOf("MSIE6.0") != -1) {
					response.setContentType("application/smnet;charset=utf-8");
					response.setHeader("Content-Disposition", "filename=" + filename + ";");
				} else {
					response.setContentType("application/smnet;charset=utf-8");
					response.setHeader("Content-Disposition", "attachment;filename="+ filename + ";");
				}
				
				if (file.isFile()) {
					BufferedInputStream fin = new BufferedInputStream(
							new FileInputStream(file));
					BufferedOutputStream outs = new BufferedOutputStream(
							response.getOutputStream());
					int read = 0;
					while ((read = fin.read(b)) != -1) {
						outs.write(b, 0, read);
					}
					outs.close();
					fin.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if ("fileupload".equals(actionName)) {

			try {
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
				
				Enumeration files = multi.getFileNames();
				while(files.hasMoreElements()){
				   String name = (String)files.nextElement();
				   saveFiles.add(multi.getFilesystemName(name));
				}
				vo.setFilename((String)saveFiles.get(0));
				vo.setFilename2((String)saveFiles.get(1));
				
				
				System.out.println(vo.getFilename());
				System.out.println(vo.getFilename2());
				
				
				BoardDao dao = new BoardDaoImpl();
				dao.insert(vo);
				
				WebUtil.redirect(request, response, "/SiteJSP/board?a=list");

			} catch (Exception e) {
				e.printStackTrace();
			}
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

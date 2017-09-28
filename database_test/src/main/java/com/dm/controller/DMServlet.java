package com.dm.controller;

import com.dm.dao.DMDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


/**
 * Servlet implementation class DbcpServlet
 */

public class DMServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DMServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		DMDao dd = new DMDao();
		String result = null;
		try {
			result = dd.excuteSql();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		response.getWriter().write("<html><head><title></title></head><body><h2>DM7</h2>"+result+" </body></html>");
	}


}

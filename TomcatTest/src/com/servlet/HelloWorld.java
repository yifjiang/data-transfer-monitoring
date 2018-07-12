package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

import com.servlet.server.ServerConfig;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class HelloWorld
 */
@WebServlet("/HelloWorld")
public class HelloWorld extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection con;
	String currentTable;
       
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @see HttpServlet#HttpServlet()
     */
    public HelloWorld() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        super();
        ServerConfig serverConfig = new ServerConfig();
        Class.forName(serverConfig.getSqlClass()).newInstance();
        con = DriverManager.getConnection(serverConfig.getSqlConnectionUrl());
        currentTable = "test1";
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	response.setContentType("text/json");
        	Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery(String.format("SELECT * FROM %s",
			        currentTable));
			JSONArray jsonarray = new JSONArray();  
			JSONObject jsonobj = new JSONObject(); 
			while(rst.next()) {
				long curId = rst.getLong("ID");
				jsonobj.put("ID", curId); 
				jsonarray.add(jsonobj);
			}
			PrintWriter out = response.getWriter();
			out.println(jsonarray);
			out.flush();
			out.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package com.servlet;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

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
        Class.forName(Config.sqlClass).newInstance();
        con = DriverManager.getConnection(Config.sqlConnectionUrl);
        currentTable = "test1";
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	response.setContentType("text/html");
        	Statement stmt = con.createStatement();
			ResultSet rst = stmt.executeQuery(String.format("SELECT * FROM %s",
			        currentTable));
			String word = "";
			while(rst.next()) {
				long curId = rst.getLong("ID");
				word = word + " " + Long.toString(curId);
			}
			PrintWriter out = response.getWriter();
			out.print(word);
			out.flush();
			out.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

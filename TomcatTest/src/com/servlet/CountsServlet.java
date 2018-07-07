package com.servlet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.processors.JsonVerifier;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "CountsServlet")
public class CountsServlet extends HttpServlet {

    Connection con;

//    public CountsServlet() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
//        super();
//    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(ClientConfig.sqlConnectionUrl);
            response.setContentType("text/json");
            Statement stmt = con.createStatement();
            JSONObject jsonObject = new JSONObject();
            for (String tableName:ClientConfig.tableNames){
                System.out.println(tableName);
                ResultSet rst = stmt.executeQuery(
                        String.format(
                                "SELECT COUNT(*) AS count FROM %s WHERE dateAndTime > NOW() - %d",
                                tableName,
                                ClientConfig.timeInterval
                        )
                );
                while (rst.next()){
                    jsonObject.put(tableName, rst.getInt("count"));
                }
            }
            PrintWriter out = response.getWriter();
            out.println(jsonObject);;
            out.flush();
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

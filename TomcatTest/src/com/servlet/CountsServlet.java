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
            JSONObject optionJSON = new JSONObject();
            JSONObject xAxisJSON = new JSONObject();
            JSONObject yAxisJSON = new JSONObject();
            xAxisJSON.put("type", "category");
            yAxisJSON.put("type", "value");
            optionJSON.put("yAxis", yAxisJSON);
            JSONArray xAxisArray = new JSONArray();
            for (int time = ClientConfig.timeInterval; time > 0; time -= ClientConfig.timeStep){
                ResultSet rst = stmt.executeQuery("SELECT NOW() - "+time+" AS theTime");
                while (rst.next()) xAxisArray.add(rst.getString("theTime"));
            }
            xAxisJSON.put("data", xAxisArray);
            optionJSON.put("xAxis", xAxisJSON);
            JSONArray seriesJSON = new JSONArray();
            for (String tableName:ClientConfig.tableNames){
                JSONObject yJSON = new JSONObject();
                JSONArray yArray = new JSONArray();
                System.out.println(tableName);
                for (int time = ClientConfig.timeInterval; time > 0; time -= ClientConfig.timeStep){
                    ResultSet rst = stmt.executeQuery(
                            String.format(
                                    "SELECT COUNT(*) AS count FROM %s WHERE dateAndTime > NOW() - %d AND dateAndTime <= NOW() - %d",
                                    tableName,
                                    time,
                                    time - ClientConfig.timeStep
                            )
                    );
                    while (rst.next()){
                        yArray.add(rst.getInt("count"));
                    }
                    yJSON.put("data", yArray);
                    yJSON.put("type", "line");
                    yJSON.put("smooth", true);
                }
//                optionJSON.put(tableName, yArray);
                seriesJSON.add(yJSON);
            }
            optionJSON.put("series", seriesJSON);
            PrintWriter out = response.getWriter();
            out.println(optionJSON);;
            out.flush();
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

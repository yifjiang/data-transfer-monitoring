package com.servlet.server;

import com.servlet.Counter;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(name = "ServerCountsServlet")
public class ServerCountsServlet extends HttpServlet {

    Connection con;

    ServerCountsServlet() throws ClassNotFoundException, SQLException {
        super();
        ServerConfig serverConfig = new ServerConfig();
        Class.forName(serverConfig.getSqlClass());
        con = DriverManager.getConnection(serverConfig.getSqlConnectionUrl());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ServerConfig serverConfig = new ServerConfig();
            response.setContentType("text/json");
            JSONObject optionJSON = (new Counter(serverConfig, con)).get(null);
            PrintWriter out = response.getWriter();
            out.println(optionJSON);
            out.flush();
            out.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

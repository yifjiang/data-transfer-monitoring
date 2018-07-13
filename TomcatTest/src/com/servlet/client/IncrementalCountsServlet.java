package com.servlet.client;

import com.servlet.Counter;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "IncrementalCountsServlet")
public class IncrementalCountsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ClientConfig clientConfig = new ClientConfig();
            response.setContentType("text/json");
            JSONObject optionJSON = (new Counter(clientConfig)).get(request.getParameter("time"));
            PrintWriter out = response.getWriter();
            out.println(optionJSON);
            out.flush();
            out.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

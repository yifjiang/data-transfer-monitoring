package com.servlet;

import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;

@WebServlet(name = "SystemStatusServlet")
public class SystemStatusServlet extends HttpServlet {

    public SystemStatusServlet(){
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Runtime runtime = Runtime.getRuntime();

        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        response.setContentType("text/json");
        JSONObject outJSON = new JSONObject();
        outJSON.put("free_memory", freeMemory / 1024);
        outJSON.put("allocated_memory", allocatedMemory / 1024);
        outJSON.put("max_memory", maxMemory / 1024);
        outJSON.put("total_free_memory", (freeMemory + (maxMemory - allocatedMemory)) / 1024);
        out.println(outJSON);
        out.flush();
        out.close();
    }
}

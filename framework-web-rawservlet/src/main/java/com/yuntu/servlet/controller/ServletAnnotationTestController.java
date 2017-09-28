package com.yuntu.servlet.controller;


import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by jackdeng on 2017/9/27.
 */
@WebServlet("/ServletAnnotationTest")
public class ServletAnnotationTestController extends HttpServlet {

    String message = "hello servlet";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置响应内容类型
        response.setContentType("text/html");
        ServletInputStream inputStream = request.getInputStream();

        int len = 0;
        byte[] bytes = new byte[10240];
        String sss = "";
        while ((len = inputStream.read(bytes)) != -1) {
            sss = new String(bytes, 0, len);
        }
        // 实际的逻辑是在这里
        PrintWriter out = response.getWriter();
        out.println("<h1>" + message + sss + "</h1>");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 设置响应内容类型
        response.setContentType("text/html");

        String str = request.getParameter("name");
        // 实际的逻辑是在这里
        PrintWriter out = response.getWriter();
        out.println("<h1>" + message + str + "</h1>");
    }
}

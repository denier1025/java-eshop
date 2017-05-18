package com.webprojectpattern.eshop.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ErrorHandlerController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Throwable throwable = (Throwable)req.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer)req.getAttribute("javax.servlet.error.status_code");

        resp.setContentType("text/html; charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<!doctype html public>\n"
                + "<html>\n"
                + "<head><title>Error/Exception Information</title></head>\n"
                + "<body>\n");
        if (throwable == null && statusCode == null) {
            out.println("<h2>Error information is missing</h2>"
                + "Please return to the <a href=\"/\">Home Page</a>.");
        } else if (statusCode != null) {
            out.println("The status code : " + statusCode);
        } else {
            out.println("Exception Type : " + throwable.getClass().getName() + "</br></br>");
            out.println("The exception message: " + throwable.getMessage( ));
        }
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

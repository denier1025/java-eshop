package com.webprojectpattern.eshop.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/signUp", "/account"})
public class SignUpController extends HttpServlet{

    private static final Logger logger = LogManager.getLogger(SignOutController.class);
    private static final String PATH = "/WEB-INF/pages/registerForm.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        logger.debug("Internal redirect to the registerForm.jsp...");
        req.getRequestDispatcher(PATH).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

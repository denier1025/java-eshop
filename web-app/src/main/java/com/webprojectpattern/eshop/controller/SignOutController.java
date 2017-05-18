package com.webprojectpattern.eshop.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/signOut")
public class SignOutController extends HttpServlet{

    private static final Logger logger = LogManager.getLogger(SignOutController.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session;
        session = req.getSession(true);
        logger.debug("Removing the attribute 'userName'...");
        session.removeAttribute("user");

        logger.debug("Internal redirect to the main page...");
        req.getRequestDispatcher("/").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

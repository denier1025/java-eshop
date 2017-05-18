package com.webprojectpattern.eshop.controller;

import com.webprojectpattern.eshop.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/account/*")
public class AccountController extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(AccountController.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession(true);
        if(session.getAttribute("user") != null) {
            if(Integer.parseInt(req.getRequestURI().split("/")[2]) != ((User)(session.getAttribute("user"))).getId()) {
                logger.debug("Internal redirect to the registerForm.jsp...");
                req.getRequestDispatcher("/WEB-INF/pages/registerForm.jsp").forward(req, resp);
            } else {
                logger.debug("Internal redirect to the account.jsp...");
                req.getRequestDispatcher("/WEB-INF/pages/account.jsp").forward(req, resp);
            }
        } else {
            logger.debug("Internal redirect to the registerForm.jsp...");
            req.getRequestDispatcher("/WEB-INF/pages/registerForm.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

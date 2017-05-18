package com.webprojectpattern.eshop.controller;

import com.webprojectpattern.eshop.dao.UserDao;
import com.webprojectpattern.eshop.dao.exception.TheUsernameOrEmailIsAlreadyExist;
import com.webprojectpattern.eshop.dao.impl.UserDaoImpl;
import com.webprojectpattern.eshop.dao.transactionManager.TransactionManager;
import com.webprojectpattern.eshop.dao.transactionManager.TransactionManagerImpl;
import com.webprojectpattern.eshop.entity.User;
import com.webprojectpattern.eshop.listener.AppContext;
import com.webprojectpattern.eshop.system.validator.UserValidator;
import com.webprojectpattern.eshop.system.validator.UserValidatorImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = "/checkReg", initParams = {@WebInitParam(name="UserValidator", value="userValidatorImpl"),
                                                    @WebInitParam(name="User", value="user"),
                                                    @WebInitParam(name="TxManager", value="txManager"),
                                                    @WebInitParam(name="UserDao", value="userDao")})
public class CheckRegisterDataController extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(CheckRegisterDataController.class);

    private ServletConfig config;
    private ApplicationContext apCon;
    private UserValidator userValidator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        apCon = AppContext.getInstance();
        logger.debug("ApplicationContext was initialized");
        userValidator = apCon.getBean(config.getInitParameter("UserValidator") , UserValidatorImpl.class);
        logger.debug("UserValidator was initialized");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session;

        User user = apCon.getBean(config.getInitParameter("User"), User.class);
        logger.debug("User was initialized");
        user.setUsername(req.getParameter("username"));
        user.setPassword(req.getParameter("password"));
        user.setEmail(req.getParameter("email"));
        logger.debug("Trying to get errorMap...");
        Map errorMap = userValidator.validate(user);
        logger.debug("errorMap is done!");

        TransactionManager txManager = apCon.getBean(config.getInitParameter("TxManager"), TransactionManagerImpl.class);
        logger.debug("TransactionManager was initialized");
        UserDao userDao = apCon.getBean(config.getInitParameter("UserDao"), UserDaoImpl.class);
        logger.debug("UserDao was initialized");

        logger.debug("Trying to accept filled data...");
        if(errorMap.get("username").equals(req.getParameter("username"))
                && errorMap.get("password").equals(req.getParameter("password"))
                && errorMap.get("email").equals(req.getParameter("email"))) {
            logger.debug("Filled data were correct");
            logger.debug("Changing the password to md5Hex...");
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            try {
                logger.debug("Trying to register a new user...");
                User sUser = txManager.doInTransaction(() -> {
                    userDao.insert(user);
                    return userDao.selectByUsernameAndPassword(user);
                });
                logger.debug("New user was registered!");
                session = req.getSession(true);
                session.setAttribute("user", sUser);
                logger.debug("Internal redirect to the main page...");
                req.getRequestDispatcher("/").forward(req, resp); //todo: try to not going to the main page
            } catch (Exception e) {
                logger.debug("Something get wrong!");
                if( e instanceof TheUsernameOrEmailIsAlreadyExist) {
                    logger.debug("TheUsernameOrEmailIsAlreadyExist exception");
                    errorMap.put("username", "TheUsernameOrEmailIsAlreadyExist");
                    errorMap.put("email", "TheUsernameOrEmailIsAlreadyExist");
                    req.setAttribute("errorMap", errorMap);
                    logger.debug("Internal redirect to the registerForm.jsp for changing username/email...");
                    req.getRequestDispatcher("/WEB-INF/pages/registerForm.jsp").forward(req, resp);
                } else {
                    logger.warn("Error page!");
                    throw new ServletException(e);
                }
            }
        } else {
            logger.debug("Filled data were not correct");
            req.setAttribute("errorMap", errorMap);
            logger.debug("Internal redirect to the registerForm.jsp for correct processing...");
            req.getRequestDispatcher("/WEB-INF/pages/registerForm.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

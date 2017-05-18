package com.webprojectpattern.eshop.controller;

import com.webprojectpattern.eshop.dao.UserDao;
import com.webprojectpattern.eshop.dao.exception.Banned;
import com.webprojectpattern.eshop.dao.exception.TheUsernameOrPasswordIsIncorrect;
import com.webprojectpattern.eshop.dao.impl.UserDaoImpl;
import com.webprojectpattern.eshop.dao.transactionManager.TransactionManager;
import com.webprojectpattern.eshop.dao.transactionManager.TransactionManagerImpl;
import com.webprojectpattern.eshop.entity.User;
import com.webprojectpattern.eshop.listener.AppContext;
import com.webprojectpattern.eshop.system.validator.UserValidator;
import com.webprojectpattern.eshop.system.validator.UserValidatorForAuthor;
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

@WebServlet(urlPatterns = "/signIn", initParams = {@WebInitParam(name="UserValidator", value="userValidatorForAuthor"),
                                                    @WebInitParam(name="User", value="user"),
                                                    @WebInitParam(name="TxManager", value="txManager"),
                                                    @WebInitParam(name="UserDao", value="userDao")})
public class SignInController extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(SignInController.class);

    private ServletConfig config;
    private ApplicationContext apCon;
    private UserValidator userValidator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        apCon = AppContext.getInstance();
        logger.debug("ApplicationContext was initialized");
        userValidator = apCon.getBean(config.getInitParameter("UserValidator") , UserValidatorForAuthor.class);
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
        logger.debug("Trying to get errorMapForAuthor...");
        Map errorMapForAuthor = userValidator.validate(user);
        logger.debug("errorMapForAuthor is done!");

        TransactionManager txManager = apCon.getBean(config.getInitParameter("TxManager"), TransactionManagerImpl.class);
        logger.debug("TransactionManager was initialized");
        UserDao userDao = apCon.getBean(config.getInitParameter("UserDao"), UserDaoImpl.class);
        logger.debug("UserDao was initialized");

//        String referer = req.getHeader("Referer");

//        String[] mass = referer.split("/");
//        String[] massServletPath = new String[mass.length - 3];
//        System.arraycopy(mass, 3, massServletPath, 0, massServletPath.length);
//        String previousPath = String.join("/", massServletPath);
//        System.out.println(previousPath);

        logger.debug("Trying to accept filled data...");
        if(errorMapForAuthor.get("username").equals(req.getParameter("username"))
                && errorMapForAuthor.get("password").equals(req.getParameter("password"))) {
            logger.debug("Filled data were correct");
            logger.debug("Changing the password to md5Hex...");
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            try {
                logger.debug("Trying to authorize a user...");
                User sUser = txManager.doInTransaction(() -> userDao.selectByUsernameAndPassword(user));
                logger.debug("Authorized!");
                session = req.getSession(true);
                if("yes".equals(req.getParameter("remember"))) {
                    session.setMaxInactiveInterval(60*60);
                }
                session.setAttribute("user", sUser);
                logger.debug("External redirect to the main page...");
                req.getRequestDispatcher("/").forward(req, resp);
            } catch (Exception e) {
                logger.debug("Something get wrong!");
                if(e instanceof TheUsernameOrPasswordIsIncorrect) {
                    logger.debug("TheUsernameOrPasswordIsIncorrect exception");
                    errorMapForAuthor.put("username", "TheUsernameOrPasswordIsIncorrect");
                    errorMapForAuthor.put("password", "TheUsernameOrPasswordIsIncorrect");
                    req.setAttribute("errorMapForAuthor", errorMapForAuthor);
                    logger.debug("Internal redirect to the main page...");
                    req.getRequestDispatcher("/").forward(req, resp); ///todo: try to not going to the main page
                } else if(e instanceof Banned){
                    logger.debug("Banned exception");
                    errorMapForAuthor.put("user", "Banned");
                    req.setAttribute("errorMapForAuthor", errorMapForAuthor);
                    logger.debug("Internal redirect to the main page...");
                    req.getRequestDispatcher("/").forward(req, resp); //todo: try to not going to the main page
                } else {
                    logger.warn("Error page!");
                    throw new ServletException(e);
                }
            }
        } else {
            logger.debug("Filled data were not correct");
            req.setAttribute("errorMapForAuthor", errorMapForAuthor);
            logger.debug("Internal redirect to the main page...");
            req.getRequestDispatcher("/").forward(req, resp); //todo: try to not going to the main page
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

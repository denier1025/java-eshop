package com.webprojectpattern.eshop.listener;

import com.webprojectpattern.eshop.dao.GroupDao;
import com.webprojectpattern.eshop.dao.exception.NoSuchEntitiesException;
import com.webprojectpattern.eshop.dao.impl.GroupDaoImpl;
import com.webprojectpattern.eshop.dao.transactionManager.TransactionManager;
import com.webprojectpattern.eshop.dao.transactionManager.TransactionManagerImpl;
import com.webprojectpattern.eshop.entity.Group;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.List;

@WebListener
public class AppContext implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(AppContext.class);

    private static final String APP_CTX_PATH = "ApplicationContext";
    private static ClassPathXmlApplicationContext appCtx;
    public static ApplicationContext getInstance() {
        return appCtx;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("Getting contextInitParameter for " + APP_CTX_PATH + "...");
        String appCtxPath = sce.getServletContext().getInitParameter(APP_CTX_PATH);
        if(appCtxPath == null) {
            logger.error("Need contextInitParameter " + APP_CTX_PATH + " for Spring in web.xml");
            throw new RuntimeException("Need contextInitParameter " + APP_CTX_PATH + " for Spring in web.xml");
        }
        logger.debug("Creating ApplicationContext...");
        appCtx = new ClassPathXmlApplicationContext(appCtxPath);

        TransactionManager txManager = appCtx.getBean("txManager", TransactionManagerImpl.class);
        logger.debug("TransactionManager was initialized");
        GroupDao groupDao = appCtx.getBean("groupDao", GroupDaoImpl.class);
        logger.debug("GroupDao was initialized");
        logger.debug("Trying to get productGroups...");
        try {
            List<Group> groups = txManager.doInTransaction(() -> groupDao.selectAll());
            logger.debug("productGroups were got!");
            sce.getServletContext().setAttribute("catalogList", groups);
        } catch (Exception e) {
            logger.warn("Runtime exception!");
            throw new RuntimeException(e);
        }

//        PropertiesConfiguration config = null;
//        try {
//            config = new PropertiesConfiguration("catalogList.properties");
//        } catch (ConfigurationException e) {
//            e.printStackTrace();
//        }
//        String[] values = config.getStringArray("list");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.debug("Getting all JDBC drivers for deregister them...");
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (Exception e) {
                logger.debug("Some drivers were not deregistered");
            }
        }
        logger.debug("Closing ApplicationContext...");
        this.appCtx.close();

    }
}

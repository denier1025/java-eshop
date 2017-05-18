package com.webprojectpattern.eshop.controller;

import com.webprojectpattern.eshop.dao.GroupDao;
import com.webprojectpattern.eshop.dao.ProductDao;
import com.webprojectpattern.eshop.dao.exception.NoSuchEntitiesException;
import com.webprojectpattern.eshop.dao.impl.GroupDaoImpl;
import com.webprojectpattern.eshop.dao.impl.ProductDaoImpl;
import com.webprojectpattern.eshop.dao.transactionManager.TransactionManager;
import com.webprojectpattern.eshop.dao.transactionManager.TransactionManagerImpl;
import com.webprojectpattern.eshop.entity.Product;
import com.webprojectpattern.eshop.listener.AppContext;
import com.webprojectpattern.eshop.system.utils.Utils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
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
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@WebServlet(urlPatterns = "/catalog/*", initParams = {@WebInitParam(name="TxManager", value="txManager"),
                                                        @WebInitParam(name="GroupDao", value="groupDao"),
                                                        @WebInitParam(name="ProductDao", value="productDao")})
public class ProductController extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ProductController.class);

    private ServletConfig config;
    private ApplicationContext apCon;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        apCon = AppContext.getInstance();
        logger.debug("ApplicationContext was initialized");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        TransactionManager txManager = apCon.getBean(config.getInitParameter("TxManager"), TransactionManagerImpl.class);
        logger.debug("TransactionManager was initialized");
        String[] splittedPath = req.getRequestURI().split("/");
        req.setAttribute("group", splittedPath[2]);
        if(splittedPath.length > 3 && splittedPath.length < 5){
            ProductDao productDao = apCon.getBean(config.getInitParameter("ProductDao"), ProductDaoImpl.class);
            logger.debug("ProductDao was initialized");
            try {
                logger.debug("Trying to get selected product...");
                Product sProduct = txManager.doInTransaction(() -> productDao.selectByProductName(splittedPath[3]));
                logger.debug("Selected product was got!");

                if(sProduct.getInputStream() != null) {
//                byte[] bytes = IOUtils.toByteArray(sProduct.getInputStream());
                    byte[] bytes = Utils.toByteArray(sProduct.getInputStream());
                    byte[] encodeBase64 = Base64.encodeBase64(bytes);
                    String base64DataString = new String(encodeBase64, Charset.forName("UTF-8"));
                    sProduct.setBase64DataString(base64DataString);
                }

                req.setAttribute("insertedProduct", sProduct);
                req.setAttribute("product", sProduct);
                logger.debug("Internal redirect to the index...");
                req.getRequestDispatcher("/").forward(req, resp);
            } catch(Exception e) {
                logger.warn("System exception!", e);
                throw new ServletException(e);
            }
        } else {
            GroupDao groupDao = apCon.getBean(config.getInitParameter("GroupDao"), GroupDaoImpl.class);
            logger.debug("GroupDao was initialized");
            try {
                groupProducts(txManager, groupDao, req, resp, splittedPath);
            } catch (Exception e) {
                exceptionExtractor(e, req, resp);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    private void groupProducts(TransactionManager txManager, GroupDao groupDao, HttpServletRequest req, HttpServletResponse resp, String[] splittedPath) throws Exception {
        logger.debug("Trying to get products of this group...");
        List<Product> products = txManager.doInTransaction(() -> groupDao.selectByGroupName(splittedPath[2]));
        logger.debug("Products of this group were got!");
        req.setAttribute("listOfProducts", products);
        logger.debug("Internal redirect...");
        req.getRequestDispatcher("/").forward(req, resp);
    }

    private void exceptionExtractor(Exception e, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.debug("Something get wrong!");
        if (e instanceof NoSuchEntitiesException) {
            logger.debug("Internal redirect to the image...");
            req.getRequestDispatcher("/images/noProducts.png").forward(req, resp);
        } else {
            logger.warn("Error page!");
            throw new ServletException(e);
        }
    }
}

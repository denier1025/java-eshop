package com.webprojectpattern.eshop.controller.accountHandlers;

import com.webprojectpattern.eshop.dao.ProductDao;
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
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;

@WebServlet(urlPatterns = "/adminHandler", initParams = {@WebInitParam(name="Product", value="product"),
                                                        @WebInitParam(name="TxManager", value="txManager"),
                                                        @WebInitParam(name="ProductDao", value="productDao")})
@MultipartConfig(maxFileSize = 16177215)
public class AdminHandlerController extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(AdminHandlerController.class);

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
        HttpSession session;

        Product product = apCon.getBean(config.getInitParameter("Product"), Product.class);
        logger.debug("Product was initialized");
        product.setProductName(req.getParameter("productName"));
        product.setCount(Integer.valueOf(req.getParameter("count")));
        product.setColor(req.getParameter("color"));
        product.setSize(req.getParameter("size"));
        product.setPrice(Integer.valueOf(req.getParameter("price")));
        product.setDescription(req.getParameter("description"));
        product.setBlocked(Boolean.valueOf(req.getParameter("blocked")));
        product.setGroupId(Integer.valueOf(req.getParameter("group_id")));
        product.setInputStream(req.getPart("pathToImg").getInputStream());

        TransactionManager txManager = apCon.getBean(config.getInitParameter("TxManager"), TransactionManagerImpl.class);
        logger.debug("TransactionManager was initialized");
        ProductDao productDao = apCon.getBean(config.getInitParameter("ProductDao"), ProductDaoImpl.class);
        logger.debug("UserDao was initialized");
        session = req.getSession(true);
        if(session.getAttribute("user") != null) {
            try {
                logger.debug("Trying to insert a new  product...");
                Product sProduct = txManager.doInTransaction(() -> {
                    productDao.insertProduct(product);
                    return productDao.selectByProductName(product.getProductName());
                });
                logger.debug("New product was added!");

//                byte[] bytes = IOUtils.toByteArray(sProduct.getInputStream());
                byte[] bytes = Utils.toByteArray(sProduct.getInputStream());
                byte[] encodeBase64 = Base64.encodeBase64(bytes);
                String base64DataString = new String(encodeBase64 , "UTF-8");
                sProduct.setBase64DataString(base64DataString);

                req.setAttribute("insertedProduct", sProduct);
                logger.debug("Internal redirect to the account page...");
                req.getRequestDispatcher("/WEB-INF/pages/account.jsp").forward(req, resp);
            } catch(Exception e) {
                logger.warn("Exception from the DB!", e);
                throw new ServletException(e);
            }
        } else {
            req.getRequestDispatcher("/WEB-INF/pages/registerForm.jsp").forward(req, resp);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}

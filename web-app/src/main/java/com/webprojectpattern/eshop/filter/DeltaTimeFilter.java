package com.webprojectpattern.eshop.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeltaTimeFilter extends BaseFilter {

    private static final Logger logger = LogManager.getLogger(DeltaTimeFilter.class);

    @Override
    public void doFilter(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.debug("Get started with 'DeltaTimeFilter'");
        long from = System.nanoTime();
        filterChain.doFilter(servletRequest, servletResponse);
        long to = System.nanoTime();
        logger.debug("DeltaTime one request is: " + (to - from) + " nanoseconds!" );
    }
}

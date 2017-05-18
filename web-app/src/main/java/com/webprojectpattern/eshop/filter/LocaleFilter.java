package com.webprojectpattern.eshop.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

public class LocaleFilter extends BaseFilter {

    private static final Logger logger = LogManager.getLogger(LocaleFilter.class);

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String[] splittedPath = request.getServletPath().split("/");
        logger.debug("SplittedPath is " + Arrays.toString(splittedPath));
        String langPart = splittedPath[1].toLowerCase();
        logger.debug("langPath is " + langPart);

        List<String> accessibleLocales = getAllAcceptLocales();
        request.setAttribute("locales", accessibleLocales);
        logger.debug("Accessible locales: " + Arrays.toString(accessibleLocales.toArray()));

        logger.debug("Going to the facade method...");
        HttpServletRequestWrapper requestWrapper = facade(request, response, accessibleLocales, langPart);
        logger.debug("Out from the facade method...");

        if("true".equals(request.getAttribute("thePartOfTheServletPathMustBeChanged"))) {
            String[] withoutLocaleString = new String[splittedPath.length - 2];
            System.arraycopy(splittedPath, 2, withoutLocaleString, 0, withoutLocaleString.length);
            String str = String.join("/", withoutLocaleString);
            if(request.getQueryString() != null && !(request.getQueryString().equals(""))){
                requestWrapper.getRequestDispatcher("/" + str + "?" + request.getQueryString()).forward(requestWrapper, response);
            } else {
                requestWrapper.getRequestDispatcher("/" + str).forward(requestWrapper, response);
            }
        } else {
            filterChain.doFilter(requestWrapper, response);
        }
    }

    private List<String> getAllAcceptLocales() {
        List<String> accessibleLocales = new ArrayList<>();
        Enumeration<String> initParameterNames = filterConfig.getInitParameterNames();
        while(initParameterNames.hasMoreElements()) {
            String valueParameter = filterConfig.getInitParameter(initParameterNames.nextElement());
            accessibleLocales.add(valueParameter);
        }
        return accessibleLocales;
    }

    private HttpServletRequestWrapper facade(HttpServletRequest request, HttpServletResponse response, List<String> accessibleLocales, String langPart) {
        Cookie[] cookies = request.getCookies();
        HttpServletRequestWrapper requestWrapper;

        String lang = request.getParameter("lang");
        if(lang != null && !("".equals(lang)) && accessibleLocales.contains(lang)) {
            Cookie cookie = new Cookie("locale", lang);
            cookie.setMaxAge(60);
            cookie.setPath("/");
            response.addCookie(cookie);
            final Locale locale = new Locale(lang);
            requestWrapper = new HttpServletRequestWrapper(request) {
                @Override
                public Locale getLocale() {
                    return locale;
                }
            };
            if(langPart != null && !("".equals(langPart)) && langPart.length() == 2 && accessibleLocales.contains(langPart)) {
                request.setAttribute("thePartOfTheServletPathMustBeChanged", "true");
            }
            return requestWrapper;
        }

        if(langPart != null && langPart.length() == 2 && accessibleLocales.contains(langPart)) {
            Cookie cookie = new Cookie("locale", langPart);
            cookie.setMaxAge(60);
            cookie.setPath("/");
            response.addCookie(cookie);
            final Locale locale = new Locale(langPart);
            requestWrapper = new HttpServletRequestWrapper(request) {
                @Override
                public Locale getLocale() {
                        return locale;
                    }
            };
            request.setAttribute("thePartOfTheServletPathMustBeChanged", "true");
            return requestWrapper;
        }

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if("locale".equals(cookie.getName())) {
                    final Locale locale = new Locale(cookie.getValue());
                    requestWrapper = new HttpServletRequestWrapper(request) {
                        @Override
                        public Locale getLocale() {
                            return locale;
                        }
                    };
                    return requestWrapper;
                }
            }
        }

        Locale locale;
        String loc = request.getLocale().toString().substring(0, 1);
        List<String> localeMap = new ArrayList<>(Arrays.asList("ru_RU", "uk_UA", "kk_KZ", "be_BY"));
        if(localeMap.contains(request.getLocale().toString())) {
            if(accessibleLocales.contains(loc)) {
                locale = new Locale(loc);
            } else {
                locale = new Locale("ru");
            }
        } else {
            if(accessibleLocales.contains(loc)) {
                locale = new Locale(loc);
            } else {
                locale = new Locale("en");
            }
        }
        requestWrapper = new HttpServletRequestWrapper(request) {
            @Override
            public Locale getLocale() {
                return locale;
            }
        };
        return requestWrapper;
    }
}

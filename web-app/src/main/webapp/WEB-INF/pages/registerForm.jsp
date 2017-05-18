<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="false" %>

<jsp:include page="jspf/header.jspf"/>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${pageContext.request.locale}" />
<fmt:setBundle basename="entries" />



<html>
<head>
    <title></title>
</head>
<body>
<c:set var="errorMap" value="${errorMap}"/>
<c:choose>
    <c:when test="${errorMap ne null}">
        <c:set var="username" value="${param.username}"/>
        <c:set var="password" value="${param.password}"/>
        <c:set var="email" value="${param.email}"/>
        <form method="POST" action="/checkReg">
        <c:if test="${errorMap['username'] eq username}">
            <fmt:message key="Username" />:<input type="text" name="username" value="${errorMap['username']}" placeholder="" />
        </c:if>
        <c:if test="${errorMap['username'] ne username}">
            <fmt:message key="Username" />:<input type="text" name="username" value="" placeholder="" /><fmt:message key="${errorMap['username']}" />
        </c:if>
        <c:if test="${errorMap['password'] eq password}">
            <fmt:message key="Password" />:<input type="password" name="password" value="${errorMap['password']}" placeholder="" />
        </c:if>
        <c:if test="${errorMap['password'] ne password}">
            <fmt:message key="Password" />:<input type="password" name="password" value="" placeholder="" /><fmt:message key="${errorMap['password']}" />
        </c:if>
        <c:if test="${errorMap['email'] eq email}">
            <fmt:message key="E_mail" />:<input type="text" name="email" value="${errorMap['email']}" placeholder="" />
        </c:if>
        <c:if test="${errorMap['email'] ne email}">
            <fmt:message key="E_mail" />:<input type="text" name="email" value="" placeholder="" /><fmt:message key="${errorMap['email']}" />
        </c:if>
        <input type="submit" value="<fmt:message key="Send" />" />
        </form>
    </c:when>
    <c:otherwise>
        <form method="POST" action="/checkReg">
        <fmt:message key="Username" />:<input type="text" name="username" value="" placeholder="" />
            <fmt:message key="Password" />:<input type="password" name="password" value="" placeholder="" />
            <fmt:message key="E_mail" />:<input type="text" name="email" value="" placeholder="" />
            <input type="submit" value="<fmt:message key="Send" />" />
        </form>
    </c:otherwise>
</c:choose>

</body>
</html>

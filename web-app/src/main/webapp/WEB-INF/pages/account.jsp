<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>

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
<c:set var="user" value="${user}"/>
<c:if test="${user.status eq 'user'}">
    <p>User account</p>
</c:if>
<c:if test="${user.status eq 'admin'}">
    <p>Admin account</p>
    <c:set var="insertedProduct" value="${insertedProduct}"/>
    <c:choose>
        <c:when test="${insertedProduct eq null}">
            <form method="post" action="/adminHandler" enctype="multipart/form-data">
                <p><input type="text" name="productName" value="" placeholder="productName(String)" /></p>
                <p><input type="text" name="count" value="" placeholder="count(int)" /></p>
                <p><input type="text" name="color" value="" placeholder="color(String)" /></p>
                <p><input type="text" name="size" value="" placeholder="size(String)" /></p>
                <p><input type="text" name="price" value="" placeholder="price(int)" /></p>
                <p><input type="text" name="description" value="" placeholder="description(String)" /></p>
                <p><input type="text" name="blocked" value="" placeholder="blocked(bool)" /></p>
                <p><input type="text" name="group_id" value="" placeholder="group_id(int)" /></p>
                <p><input type="file" name="pathToImg" value="" placeholder="img" /></p>
                <input type="submit" value="<fmt:message key="Insert" />" />
            </form>
        </c:when>
        <c:otherwise>
            <c:out value="${insertedProduct.id}" />
            <c:out value="${insertedProduct.productName}" />
            <c:out value="${insertedProduct.count}" />
            <c:out value="${insertedProduct.color}" />
            <c:out value="${insertedProduct.size}" />
            <c:out value="${insertedProduct.price}" />
            <c:out value="${insertedProduct.description}" />
            <c:out value="${insertedProduct.blocked}" />
            <c:out value="${insertedProduct.groupId}" />
            <img src="data:image/png;base64,${insertedProduct.base64DataString}" width="250" height="200" />
        </c:otherwise>
    </c:choose>
</c:if>

</body>
</html>

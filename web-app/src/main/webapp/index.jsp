<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="false" %>

<jsp:include page="WEB-INF/pages/jspf/header.jspf"/>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${pageContext.request.locale}" />
<fmt:setBundle basename="entries" />



<html>
<head>
    <title></title>
</head>
<body>

<c:forEach var="catalog" items="${catalogList}">
    <p><a href="/catalog/${catalog}">${catalog}</a></p>
</c:forEach>

<c:set var="listOfProducts" value="${listOfProducts}" />
<c:if test="${listOfProducts ne null}">
    <c:forEach var="product" items="${listOfProducts}">
        <p><a href="/catalog/${group}/${product.productName}">${product}</a></p>
    </c:forEach>
</c:if>

<c:set var="product" value="${product}" />
<c:if test="${product ne null}">
    <c:out value="${product.productName}" />
    <c:out value="${product.count}" />
    <c:out value="${product.color}" />
    <c:out value="${product.size}" />
    <c:out value="${product.price}" />
    <c:out value="${product.description}" />

    <c:set var="base64DataString" value="${insertedProduct.base64DataString}" />
    <c:if test="${base64DataString ne null}" >
    <img src="data:image/png;base64,${base64DataString}" width="250" height="200" />
    </c:if>

</c:if>

<p><fmt:message key="THIS_IS_THE_MAIN_PAGE!!!" /></p>

</body>
</html>

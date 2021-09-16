<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isErrorPage = "true" %>

<tags:master pageTitle="Page not found">
    <h1>We are sorry</h1>
    <h4 class="error">${pageContext.exception.message}</h4>

    <a class="button-back" href="${pageContext.servletContext.contextPath}/products">Go back</a>
</tags:master>
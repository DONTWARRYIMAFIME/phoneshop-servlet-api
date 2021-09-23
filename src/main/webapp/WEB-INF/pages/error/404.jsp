<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isErrorPage = "true" %>

<tags:master pageTitle="Internal Server Error">
    <h1>We are sorry</h1>

    <h4>Page not found</h4>

    <button class="button" onClick="javascript:window.location='${pageContext.servletContext.contextPath}/products'">Go back</button>
</tags:master>
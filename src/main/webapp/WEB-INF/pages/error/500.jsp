<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isErrorPage = "true" %>

<tags:master pageTitle="Internal Server Error">
    <h1>We are sorry</h1>
    <h4 class="error">${pageContext.exception.message}</h4>

    <h4>Try again later or contact with administrators</h4>

    <button class="button-back" onClick="javascript:history.go(-1)" type="button">Go Back</button>
</tags:master>
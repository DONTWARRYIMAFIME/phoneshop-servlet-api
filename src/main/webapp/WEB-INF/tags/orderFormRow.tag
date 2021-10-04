<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="name" required="true"%>
<%@ attribute name="label" required="true"%>
<%@ attribute name="type" required="true"%>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order"%>
<%@ attribute name="errors" required="true" type="java.util.Map<java.lang.String,java.lang.String>"%>

<tr>
    <td class="required">${label}</td>
    <td>
        <c:set var="error" value="${errors[name]}"/>
        <input type="${type}" name="${name}" value="${not empty error ? param[name] : order[name]}"/>
        <c:if test="${not empty error}">
            <div class="error">
                ${error}
            </div>
        </c:if>
    </td>
</tr>
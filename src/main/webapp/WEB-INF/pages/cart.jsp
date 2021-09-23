<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Product Details">
    <p>
        Cart: ${cart}
    </p>
    <c:if test="${cart.items.size() > 0}">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>Description</td>
                <td class="price">Price</td>
            </tr>
            </thead>
            <c:forEach var="item" items="${cart.items.values()}">
                <tr>
                    <td>
                        <img class="product-tile" src="${item.product.imageUrl}" alt="Cannot load img">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                                ${item.product.description}
                        </a>
                    </td>
                    <td class="price tooltip">
                        <fmt:formatNumber value="${item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${cart.items.size() == 0}">
        <h2>Your cart is empty</h2>
    </c:if>

    <button class="button" onClick="javascript:window.location='${pageContext.servletContext.contextPath}/products'">Go back</button>
</tags:master>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Cart">
    <p>
        Cart: ${cart}
    </p>
    <c:if test="${empty errors and not empty param.message}">
        <p class="success">
            ${param.message}
        </p>
    </c:if>
    <c:if test="${not empty errors}">
        <p class="error">
            Something goes wrong...
        </p>
    </c:if>
    <c:if test="${cart.items.size() > 0}">
        <form method="POST" action="${pageContext.servletContext.contextPath}/cart">
            <table>
                <thead>
                <tr>
                    <td>Image</td>
                    <td>Description</td>
                    <td class="quantity">Quantity</td>
                    <td class="price">Price</td>
                    <td>Action</td>
                </tr>
                </thead>
                <c:forEach var="item" items="${cart.items.values()}" varStatus="status">
                    <tr>
                        <td>
                            <img class="product-tile" src="${item.product.imageUrl}" alt="Cannot load img">
                        </td>
                        <td>
                            <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                                    ${item.product.description}
                            </a>
                        </td>
                        <td class="quantity">
                            <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                            <c:set var="error" value="${errors[item.product.id]}"/>
                            <input class="quantity" name="quantity" value="${not empty error ? paramValues['quantity'][status.index] : quantity}"/>
                            <c:if test="${not empty error}">
                                <div class="error">
                                    ${error}
                                </div>
                            </c:if>
                            <input name="productId" type="hidden" value="${item.product.id}">
                        </td>
                        <td class="price">
                            <fmt:formatNumber value="${item.quantity * item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
                        </td>
                        <td>
                            <button class="button" form="deleteCartItem" formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">
                                Delete
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <tr class="price">
                    <td></td>
                    <td></td>
                    <td class="price">Total price:</td>
                    <td class="price">
                        <fmt:formatNumber value="${cart.totalPrice}" type="currency" currencySymbol="${cart.items.values().iterator().next().product.currency.symbol}"/>
                    </td>
                </tr>
            </table>
            <p>
                <button id="update" name="update" class="button">Update</button>
            </p>
        </form>
        <form id="deleteCartItem" method="POST"/>
    </c:if>
    <c:if test="${cart.items.size() == 0}">
        <h2>Your cart is empty</h2>
    </c:if>
    <hr/>
    <button class="button" type="button" onClick="javascript:window.location='${pageContext.servletContext.contextPath}/products'">Go back</button>
</tags:master>
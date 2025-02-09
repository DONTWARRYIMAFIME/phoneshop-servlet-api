<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order overview">
    <h1>Order overview</h1>
    <p>
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>Description</td>
                <td class="quantity">Quantity</td>
                <td class="price">Price</td>
            </tr>
            </thead>
            <c:forEach var="item" items="${order.items.values()}" varStatus="status">
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
                        ${quantity}
                    </td>
                    <td class="price">
                        <fmt:formatNumber value="${item.quantity * item.product.price}" type="currency" currencySymbol="${item.product.currency.symbol}"/>
                    </td>
                </tr>
            </c:forEach>
            <c:set var="currency" value="${order.items.values().iterator().next().product.currency.symbol}"/>
            <tr class="price">
                <td></td>
                <td></td>
                <td class="price">Subtotal price:</td>
                <td class="price">
                    <fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="${currency}"/>
                </td>
            </tr>
            <tr class="price">
                <td></td>
                <td></td>
                <td class="price">Delivery price:</td>
                <td class="price">
                    <fmt:formatNumber value="${order.deliveryPrice}" type="currency" currencySymbol="${currency}"/>
                </td>
            </tr>
            <tr class="price">
                <td></td>
                <td></td>
                <td class="price">Total price:</td>
                <td class="price">
                    <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="${currency}"/>
                </td>
            </tr>
        </table>

        <h2>Your details</h2>

        <table>
            <tags:orderOverviewRow name="firstName" label="First name" order="${order}"/>
            <tags:orderOverviewRow name="lastName" label="Last name" order="${order}"/>
            <tags:orderOverviewRow name="phone" label="Phone" order="${order}"/>
            <tags:orderOverviewRow name="deliveryDate" label="Delivery date" order="${order}"/>
            <tags:orderOverviewRow name="deliveryAddress" label="Delivery address" order="${order}"/>
            <tags:orderOverviewRow name="paymentMethod" label="Payment method" order="${order}"/>
        </table>
    </p>
    <hr/>
    <button class="button" type="button" onClick="javascript:window.location='${pageContext.servletContext.contextPath}/products'">Go back</button>
</tags:master>
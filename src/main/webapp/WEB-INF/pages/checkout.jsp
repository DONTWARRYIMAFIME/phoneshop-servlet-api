<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">
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
    <p><form method="POST" action="${pageContext.servletContext.contextPath}/checkout">
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
            <tags:orderFormRow name="firstName" label="First name" type="text" order="${order}" errors="${errors}"/>
            <tags:orderFormRow name="lastName" label="Last name" type="text" order="${order}" errors="${errors}"/>
            <tags:orderFormRow name="phone" label="Phone" type="text" order="${order}" errors="${errors}"/>
            <tags:orderFormRow name="deliveryDate" label="Delivery date" type="date" order="${order}" errors="${errors}"/>
            <tags:orderFormRow name="deliveryAddress" label="Delivery address" type="text" order="${order}" errors="${errors}"/>
            <tr>
                <td class="required">Payment method</td>
                <td>
                    <select name="paymentMethod">
                        <option></option>
                        <c:forEach var="paymentMethod" items="${paymentMethods}">
                            <option <c:if test="${param.paymentMethod eq paymentMethod.toString()}">selected</c:if>>
                                    ${paymentMethod}
                            </option>
                        </c:forEach>
                    </select>
                    <c:set var="error" value="${errors['paymentMethod']}"/>
                    <c:if test="${not empty error}">
                        <div class="error">
                            ${error}
                        </div>
                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button class="button">Place order</button>
        </p>
    </form></p>
    <hr/>
    <button class="button" type="button" onClick="javascript:window.location='${pageContext.servletContext.contextPath}/products'">Go back</button>
</tags:master>
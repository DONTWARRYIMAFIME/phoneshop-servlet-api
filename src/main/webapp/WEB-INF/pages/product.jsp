<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details">
    <p>
       Cart: ${cart}
    </p>
    <c:if test="${empty error and not empty param.message}">
        <p class="success">
            ${param.message}
        </p>
    </c:if>
    <c:if test="${not empty error}">
        <p class="error">
            Something goes wrong...
        </p>
    </c:if>
    <p>
        ${product.description}
    </p>
    <form method="POST">
        <table>
            <tr>
                <td>Image</td>
                <td><img src="${product.imageUrl}" alt="Cannot load img"></td>
            </tr>
            <tr>
                <td>Code</td>
                <td>${product.code}</td>
            </tr>
            <tr>
                <td>Stock</td>
                <td>${product.stock}</td>
            </tr>
            <tr>
                <td>Price</td>
                <td class="price"><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/></td>
            </tr>
            <tr>
                <td>Quantity</td>
                <td>
                    <input class="quantity" name="quantity" value="${not empty error ? param.quantity : 1}">
                    <button>Add to cart</button>
                    <c:if test="${not empty error}">
                        <p class="error">
                            ${error}
                        </p>
                    </c:if>
                </td>
            </tr>
        </table>

    </form>

    <button class="button-back" onClick="javascript:history.go(-1)" type="button">Go Back</button>
</tags:master>
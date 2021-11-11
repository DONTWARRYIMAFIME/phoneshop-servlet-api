<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="searchModes" type="java.lang.Object" scope="request"/>
<tags:master pageTitle="Advaned Search">
    <h2>Advanced Search</h2>
    <form>
        <table>
            <tr>
                <td>Search</td>
                <td>
                    <input name="query" value="${not empty param.query ? param.query : ""}">
                    <p>
                        <select name="searchMode">
                            <c:forEach var="searchMode" items="${searchModes}">
                                <option <c:if test="${param.searchMode eq searchMode.toString()}">selected</c:if>>
                                        ${searchMode}
                                </option>
                            </c:forEach>
                        </select>
                            <c:set var="error" value="${errors['searchMode']}"/>
                        <c:if test="${not empty error}">
                    <div class="error">
                            ${error}
                    </div>
                    </c:if>
                    </p>
                </td>
            </tr>
            <tags:searchFormRow label="Code" name="code" errors="${errors}"/>
            <tags:searchFormRow label="Min price" name="minPrice" errors="${errors}"/>
            <tags:searchFormRow label="Max price" name="maxPrice" errors="${errors}"/>
            <tags:searchFormRow label="Min Stock" name="minStock" errors="${errors}"/>
            <tags:searchFormRow label="Max Stock" name="maxStock" errors="${errors}"/>
        </table>
        <p>
            <button class="button">Search</button>
        </p>
    </form>
    <hr />
    <p><button class="button" onClick="javascript:window.location='${pageContext.servletContext.contextPath}/products'">Go back</button><p>
    <c:if test="${not empty products}">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>Description</td>
                <td>Code</td>
                <td class="price">Price</td>
                <td class="price">Stock</td>
            </tr>
            </thead>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>
                        <img class="product-tile" src="${product.imageUrl}" alt="Cannot load img">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                                ${product.description}
                        </a>
                    </td>
                    <td>${product.code}</td>
                    <td class="price tooltip">
                        <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
                        <tags:tooltip description="${product.description}" histories="${product.histories}"/>
                    </td>
                    <td class="price">${product.stock}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${not empty param and empty products}">
        <p>No results were found for your search</p>
    </c:if>
</tags:master>
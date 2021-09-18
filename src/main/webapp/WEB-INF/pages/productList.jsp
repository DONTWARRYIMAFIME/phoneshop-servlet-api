<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
  <form>
    <input name="query" value="${param.query}">
    <button>Search</button>
  </form>
  <c:if test="${products.size() > 0}">
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>
          Description
          <tags:sortLink sort="description" order="asc" symbol="⇑"/>
          <tags:sortLink sort="description" order="desc" symbol="⇓"/>
        </td>
        <td class="price">
          Price
          <tags:sortLink sort="price" order="asc" symbol="⇑"/>
          <tags:sortLink sort="price" order="desc" symbol="⇓"/>
        </td>
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
        <td class="price tooltip">
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          <tags:tooltip description="${product.description}" histories="${product.histories}"/>
        </td>
      </tr>
    </c:forEach>
  </table>
  </c:if>
  <c:if test="${products.size() == 0}">
    <p>No results were found for your search</p>
  </c:if>
  <c:if test="${not empty sessionScope.viewed}">
    <h2>Recently viewed</h2>
    <div class="viewed__row">
      <c:forEach var="product" items="${sessionScope.viewed.products}">
        <div class="viewed__col">
          <div>
            <img class="product-tile" src="${product.imageUrl}">
          </div>
          <div>
            <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
              ${product.description}
            </a>
          </div>
          <div>
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </div>
        </div>
      </c:forEach>
    </div>
  </c:if>
</tags:master>
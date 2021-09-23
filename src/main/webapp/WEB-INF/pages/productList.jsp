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
    <button class="button">Search</button>
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
  <tags:recentlyViewed viewed="${viewed}"/>
</tags:master>
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
    <button type="button" class="button" onClick="javascript:window.location='${pageContext.servletContext.contextPath}/search'">More filters</button>
  </form>
  <c:if test="${empty param.error and not empty param.message}">
    <p class="success">
        ${param.message}
    </p>
  </c:if>
  <c:if test="${not empty param.error}">
    <p class="error">
      Something goes wrong...
    </p>
  </c:if>
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
        <td>Quantity</td>
        <td class="price">
          Price
          <tags:sortLink sort="price" order="asc" symbol="⇑"/>
          <tags:sortLink sort="price" order="desc" symbol="⇓"/>
        </td>
        <td>Action</td>
      </tr>
      </thead>
      <c:forEach var="product" items="${products}">
        <form method="POST" action="${pageContext.servletContext.contextPath}/cart/addCartItem/${product.id}">
          <tr>
            <td>
              <img class="product-tile" src="${product.imageUrl}" alt="Cannot load img">
            </td>
            <td>
              <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                  ${product.description}
              </a>
            </td>
            <td class="quantity">
              <fmt:formatNumber value="1" var="quantity"/>
              <c:set var="error" value="${product.id eq param.productId ? param.error : null}"/>
              <input class="quantity" name="quantity" value="${not empty error ? param.quantity : quantity}"/>
              <input type="hidden" name="query" value="${param.query}">
              <input type="hidden" name="sort" value="${param.sort}">
              <input type="hidden" name="order" value="${param.order}">
              <c:if test="${not empty error}">
                <div class="error">
                    ${error}
                </div>
              </c:if>
            </td>
            <td class="price tooltip">
              <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
              <tags:tooltip description="${product.description}" histories="${product.histories}"/>
            </td>
            <td>
              <button class="button">Add to cart</button>
            </td>
          </tr>
        </form>
      </c:forEach>
    </table>
  </c:if>
  <c:if test="${products.size() == 0}">
    <p>No results were found for your search</p>
  </c:if>
  <tags:recentlyViewed viewed="${viewed}"/>
</tags:master>
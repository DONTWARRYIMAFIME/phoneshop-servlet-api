<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="viewed" required="true" type="java.util.Deque<com.es.phoneshop.model.product.Product>" %>

<c:if test="${not empty viewed}">
    <h2>Recently viewed</h2>
    <div class="viewed__row">
        <c:forEach var="product" items="${viewed}">
            <div class="viewed__col">
                <div>
                    <img class="product-tile" src="${product.imageUrl}" alt="">
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
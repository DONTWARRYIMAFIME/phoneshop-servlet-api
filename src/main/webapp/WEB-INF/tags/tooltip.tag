<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="description" required="true" %>
<%@ attribute name="histories" required="true" type="java.util.Set<com.es.phoneshop.model.product.PriceHistoryEntry>" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="tooltiptext">
    <div class="tooltip__title">Price history</div>
    <div class="tooltip__description">${description}</div>
    <div class="tooltip__row" style="font-weight: bold">
        <div class="tooltip__col">Start date</div>
        <div class="tooltip__col">Price</div>
    </div>
    <c:forEach var="history" items="${histories}" >
        <div class="tooltip__row">
            <div class="tooltip__col">${history.date}</div>
            <div class="tooltip__col">
                <fmt:formatNumber value="${history.price}" type="currency" currencySymbol="${history.currency.symbol}"/>
            </div>
        </div>
    </c:forEach>
</div>
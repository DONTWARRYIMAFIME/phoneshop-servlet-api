<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>
<%@ attribute name="symbol" required="true" %>

<a href="?query=${param.query}&sort=${sort}&order=${order}" style="${sort.equals(param.sort) && order.equals(param.order) ? 'font-weight: 700' : ''}">${symbol}</a>
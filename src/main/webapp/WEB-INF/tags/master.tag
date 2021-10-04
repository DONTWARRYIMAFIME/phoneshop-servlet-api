<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>

<html>
<head>
  <title>${pageTitle}</title>
  <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/style.css">
  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/tooltip.css">
  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/minicart.css">
</head>
<body class="product-list">
  <div class="wrapper">
    <header>
      <a href="${pageContext.servletContext.contextPath}">
        <img src="${pageContext.servletContext.contextPath}/images/logo.svg"/>
        PhoneShop
      </a>
    </header>
    <main>
      <jsp:include page="/cart/minicart"/>
      <jsp:doBody/>
    </main>
    <footer>
      <p>(c) Expert - Soft</p>
    </footer>
  </div>
</body>
</html>
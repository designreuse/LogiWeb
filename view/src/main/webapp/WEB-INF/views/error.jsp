<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title>Error Page</title>
<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/flatly.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/logiweb.css" />">
<body>
<jsp:include page="navbar.jsp"/>
<div class="container col-md-4 col-md-offset-4">
    <h2 class="text-info">Sorry, some error occur due to your actions. Go back and try again.</h2>
    <p><b>Message: </b>${sessionScope.errorMessage}</p>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>

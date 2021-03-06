<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<title>Trucks Page</title>
<link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/flatly.css" />">
<link rel="stylesheet" href="<c:url value="/resources/css/logiweb.css" />">
<body>
<c:import url="../../navbar.jsp">
    <c:param name="activeTab" value="Trucks"/>
</c:import>
<div class="container">
    <c:choose>
        <c:when test="${empty trucks}">
            <p>You are <b>not logged in</b> and not allowed to see list of trucks.</p>
        </c:when>
        <c:otherwise>
            <h2>Trucks</h2>
            <div class="text-right">
                <form method="post" action="${pageContext.request.contextPath}/trucks/add">
                    <button type="submit" class="btn btn-primary btn-success" name="action" value="add">Add Truck</button>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </div>
            <table class="table middleAligned table-bordered table-striped table-hover">
                <tr>
                    <th>ID</th>
                    <th>RegNumber</th>
                    <th>ShiftSize</th>
                    <th>Capacity</th>
                    <th>State</th>
                    <th>CurrentCity</th>
                    <th>Actions</th>
                </tr>
                <c:forEach items="${trucks}" var="truck">
                    <tr>
                        <td>
                            ${truck.getId()}
                        </td>
                        <td>
                            ${truck.getRegNumber()}
                        </td>
                        <td>
                            ${truck.getShiftSize()}
                        </td>
                        <td>
                            ${truck.getCapacity()}
                        </td>
                        <td>
                            ${truck.getState().equals("OK") ? "OK" : "Broken"}
                        </td>
                        <td>
                            ${truck.getCity().getName()}
                        </td>
                        <td class="buttonsCell">
                            <div class="row">
                                <div class="col-md-6 editButton">
                                    <form class="form-inline" method="post" action="${pageContext.request.contextPath}/trucks/edit">
                                        <button type="submit" class="btn btn-primary btn-primary" name="action" value="edit">Edit
                                        </button>
                                        <input type="hidden" name="id" value="${truck.getId()}"/>
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    </form>
                                </div>
                                <div class="col-md-6 removeButton">
                                    <form class="form-inline" method="post" action="${pageContext.request.contextPath}/trucks/delete">
                                        <button type="submit" class="btn btn-primary btn-danger" name="action" value="delete">Remove
                                        </button>
                                        <input type="hidden" name="id" value="${truck.getId()}"/>
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                    </form>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>
</div>
<jsp:include page="../../footer.jsp"/>
</body>
</html>

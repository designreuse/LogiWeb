<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: tims
  Date: 2/13/2016
  Time: 11:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Setup and Load Data to jTable using Servlets and JSP</title>
    <!-- Include one of jTable styles. -->
    <link href="${pageContext.request.contextPath}/css/metro/crimson/jtable.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/css/metro/crimson/myAdditions.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/css/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="<c:url value="/resources/css/bootstrap/flatly.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/logiweb.css" />">
    <!-- Include jTable script file. -->
    <script src="${pageContext.request.contextPath}/js/jquery-1.8.2.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/jquery-ui-1.10.3.custom.js" type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.jtable.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#TrucksTableContainer').jtable({
                title: 'Table of trucks',
                actions: {
                    listAction: '${pageContext.request.contextPath}/TruckJSController?action=list',
                    createAction: '${pageContext.request.contextPath}/TruckJSController?action=create',
                    updateAction: '${pageContext.request.contextPath}/TruckJSController?action=update',
                    deleteAction: '${pageContext.request.contextPath}   /TruckJSController?action=delete'
                },
                fields: {
                    id: {
                        title: 'id',
                        width: '5%',
                        key: true,
                        list: true,
                        create: false
                    },
                    regNumber: {
                        title: 'Reg. number',
                        width: '15%',
                        edit: true,

                    },
                    shiftSize: {
                        title: 'Shift size',
                        width: '20%',
                        edit: true
                    },
                    capacity: {
                        title: 'Capacity',
                        width: '20%',
                        edit: true
                    },
                    state: {
                        title: 'State',
                        width: '20%',
                        edit: true
                    },
                    city: {
                        title: 'Current city',
                        width: '20%',
                        edit: false,
                        create: false
                    }
                }
            });
            $('#TrucksTableContainer').jtable('load');
        });
    </script>
</head>
<body>
<c:import url="/jspf/navbar.jspf">
    <c:param name="activeTab" value="Trucks"/>
</c:import>
<div class="container">
    <div style="width:60%;margin-right:20%;margin-left:20%;text-align:center;">
        <h1>Trucks</h1>
        <div id="TrucksTableContainer"></div>
    </div>
</div>
</body>
<jsp:include page="/jspf/footer.jspf"/>
</html>

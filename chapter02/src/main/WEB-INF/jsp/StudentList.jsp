<%--
  Created by IntelliJ IDEA.
  User: creasypita
  Date: 9/3/2019
  Time: 2:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>student List</title>
</head>
<body>

<div id="global">
    <h1>Book List</h1>
    <table>
        <tr>
            <TH>NAME</TH>
            <TH>BRANCH</TH>
            <TH>PERCENTAGE</TH>
            <TH>PHONE</TH>
            <TH>EMAIL</TH>
        </tr>
        <c:forEach items="${students}" var="student">
            <tr>
                <td>${student.name}</td>
                <td>${student.branch}</td>
                <td>${student.percentage}</td>
                <td>${student.phone}</td>
                <td>${student.email}</td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>


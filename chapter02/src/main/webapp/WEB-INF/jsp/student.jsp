<%--
  Created by IntelliJ IDEA.
  User: creasypita
  Date: 9/3/2019
  Time: 2:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html; charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="BASE" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
    <title>student List</title>
</head>
<body>

<div id="global">
    <h1>student List</h1>
    <a href="${BASE}/student_insert">new student</a>
    <table>
        <tr>
            <TH>NAME</TH>
            <TH>BRANCH</TH>
            <TH>PERCENTAGE</TH>
            <TH>PHONE</TH>
            <TH>EMAIL</TH>
            <th>operation</th>
        </tr>
        <c:forEach items="${students}" var="student">
            <tr>
                <td>${student.name}</td>
                <td>${student.branch}</td>
                <td>${student.percentage}</td>
                <td>${student.phone}</td>
                <td>${student.email}</td>
                <td>
                    <a href="${BASE}/student_view?id=${student.id}">view</a>
                    <a href="${BASE}/student_update?id=${student.id}">edit</a>
                    <a href="${BASE}/student_delete?id=${student.id}">remove</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>


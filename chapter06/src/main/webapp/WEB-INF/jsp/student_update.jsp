<%--
  Created by IntelliJ IDEA.
  User: creasypita
  Date: 9/3/2019
  Time: 2:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html; charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="BASE" value="${pageContext.request.contextPath}"/>
<!DOCTYPE HTML>
<html>
<head>
    <title>student form</title>
</head>
<body>

<div id="global">
    <form id="student_form" action="${BASE}/student_update" method="post">
        <input type="hidden" name="id" value="${student.id}">
        <table>
            <tr>
                <td>NAME：</td>
                <td>
                    <input type="text" name="name" value="${student.name}">
                </td>
            </tr>
            <tr>
                <td>BRANCH：</td>
                <td>
                    <input type="text" name="branch" value="${student.branch}">
                </td>
            </tr>
            <tr>
                <td>PERCENTAGE：</td>
                <td>
                    <input type="text" name="percentage" value="${student.percentage}">
                </td>
            </tr>
            <tr>
                <td>phone：</td>
                <td>
                    <input type="text" name="phone" value="${student.phone}">
                </td>
            </tr>
            <tr>
                <td>email：</td>
                <td>
                    <input type="text" name="email" value="${student.email}">
                </td>
            </tr>
        </table>
        <button type="submit">保存</button>
    </form>
</div>
</body>
</html>


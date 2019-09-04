<%--
  Created by IntelliJ IDEA.
  User: creasypita
  Date: 9/3/2019
  Time: 2:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html; charset=UTF-8" language="java" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>student List</title>
</head>
<body>

<div id="global">
    <form id="student_form" >
        <table>
            <tr>
                <td>NAME：</td>
                <td>
                    ${student.name}
                </td>
            </tr>
            <tr>
                <td>BRANCH：</td>
                <td>
                    ${student.branch}
                </td>
            </tr>
            <tr>
                <td>PERCENTAGE：</td>
                <td>
                    ${student.percentage}
                </td>
            </tr>
            <tr>
                <td>phone：</td>
                <td>
                    ${student.phone}
                </td>
            </tr>
            <tr>
                <td>email：</td>
                <td>
                    ${student.email}
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>


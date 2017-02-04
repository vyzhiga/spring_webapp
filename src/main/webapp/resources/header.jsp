<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>

<table>
    <tr>
        <td><a href="${pageContext.request.contextPath}/books">Книги</a></td>
        <td><a href="${pageContext.request.contextPath}/users">Пользователи</a></td>
        <td><a href="${pageContext.request.contextPath}/usersdebug">Users debug</a></td>
    </tr>
</table>

<div style="margin: 10px 0">
    Debug: Current user is ${empty sesCurUser? 'not set yet': sesCurUser} | ${sesCurOrder} - ${sesOrder}.
</div>
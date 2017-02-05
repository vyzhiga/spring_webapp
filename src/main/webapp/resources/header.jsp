<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>

<table>
    <tr>
        <td><a href="${pageContext.request.contextPath}/books">Книги</a></td>
        <td><a href="${pageContext.request.contextPath}/users">Пользователи</a></td>
        <td><a href="${pageContext.request.contextPath}/usersdebug">Users debug</a></td>
        <td><a href="http://user:user@localhost:8080/spring_webapp/">Logout</a></td>
    </tr>
</table>

<div style="margin: 10px 0">
    Debug: Current user is ${empty sesCurUser? 'not set yet': sesCurUser} | ${sesCurOrder} - ${sesOrder}.
    <p>
        Hello <b><c:out value="${pageContext.request.remoteUser}"/></b>
    </p>

</div>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<table>
    <tr>
        <td><a href="${pageContext.request.contextPath}/books">Книги</a></td>
        <td><a href="${pageContext.request.contextPath}/users">Пользователи</a></td>
    </tr>
</table>

<sec:authentication var="user" property="principal" />

<div style="margin: 10px 0">
    Debug: Current user is '${user.username}' | Sorting order is '${sesCurOrder}' - '${sesOrder}'.
</div>
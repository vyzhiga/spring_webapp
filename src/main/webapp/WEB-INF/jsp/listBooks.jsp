<%--
  Created by IntelliJ IDEA.
  User: duzer
  Date: 19.12.2016
  Time: 1:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<sec:authentication var="user" property="principal" />

<c:forEach items="${listBook}" var="book">
    <tr>
        <td><c:out value="${book.idBook}"></c:out></td>
        <td><c:out value="${book.bookAuthor}"></c:out></td>
        <td><c:out value="${book.nameBook}"></c:out></td>
        <%-- колонка ISBN--%>
        <td>
            <a href="#" onclick="jsGetBookDetails(${book.idBook})"><c:out value="${book.ISBNBook}"></c:out></a>
        </td>
        <%-- колонка "Кем взята" --%>
        <c:choose>
            <%-- вернуть, если пользователи совпадают --%>
            <c:when test="${book.bookTaker==user.username}">
                <td>
                    <input type="button" value="Вернуть" onclick="jsChangeTaker(${book.idBook}, 0, '${user.username}')">
                </td>
            </c:when>
            <%-- взять, если книга никем не взята --%>
            <c:when test="${book.bookTaker==null && not empty user.username}">
                <td>
                    <input type="button" value="Взять" onclick="jsChangeTaker(${book.idBook}, 1, '${user.username}')">
                </td>
            </c:when>
            <%-- вернуть имя пользователя, взявшего книгу --%>
            <c:otherwise>
                <td>
                    ${book.bookTaker}
                </td>
            </c:otherwise>
        </c:choose>
        <!-- кнопка удаления -->
        <td><input type="button" value="Удалить" onclick="jsDeleteBook(${book.idBook})"></td>
    </tr>
</c:forEach>
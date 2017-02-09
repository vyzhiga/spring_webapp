<%--
  Created by IntelliJ IDEA.
  User: duzer
  Date: 21.12.2016
  Time: 0:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<head>
    <title>Книги</title>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

    <%-- Loading jquery-ui --%>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css">
    <script type="text/javascript" src="//code.jquery.com/jquery-1.12.4.js"></script>
    <script type="text/javascript" src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

    <%-- определяем внешний стиль --%>
    <link rel="stylesheet" type="text/css" href="css/hw.css">
</head>

<body>

<%@ include file="/WEB-INF/jsp/header.jsp"%>

<div style="margin: 5px 0">
    <input type="button" id="bookdialog" value="Добавить книгу"/>
</div>

<div id="dialog" title="Добавить книгу">
    ISBN: <input type="text" id="isbn"><br>
    Автор: <input type="text" id="author"><br>
    Название: <input type="text" id="name">
</div>

<table class="booksTbl">
    <thead>
    <tr>
        <th>ID</th>
        <th id="authorth" onclick="jsSetAuthorOrder()">Author</th>
        <th id="nameth" onclick="jsSetNameOrder()">NameBook</th>
        <th>ISBNBook</th>
        <th>Кем взята</th>
        <th>Удалить</th>
    </tr>
    </thead>
    <tbody id="emptytbody" style="display: none;"></tbody>
</table>

<%-- Кнопка "Показать еще" в конце списка--%>
<div style="margin: 5px 0">
    <input id="load" type="button" value="Показать еще"/>
    <input id="recqnt" type="number" min="1" defaultValue="5" value="5"/>
</div>

<script type="text/javascript" language="javascript">

    // pagination списка книг
    var getBooksUrl = "${pageContext.request.contextPath}/getbooks"
    // номер страницы
    var numPage = 1;
    // количество книг на странице
    var recPerPage = 5;
    // текущая сортировка по колнке: автор или наименование книги
    var jsCurOrder = "${sesCurOrder}";
    // текущий порядок сортировки
    var jsOrder = "${sesOrder}";

    $(document).ready(function () {
        recPerPage = $("#recqnt").val();

        // устанавливаем цвета и символ направления сортировки при отображении страницы в зависимости от сортировки
        if (jsCurOrder == "BookAuthor") {
            $("#authorth").css("background", "#ff99ff");
            if (jsOrder == "ASC") {
                $("#authorth").text("Author ↑");
            } else if (jsOrder == "DESC") {
                $("#authorth").text("Author ↓");
            }
        } else if (jsCurOrder == "BookName") {
            $("#nameth").css("background", "#ff99ff");
            if (jsOrder == "ASC") {
                $("#nameth").text("NameBook ↑");
            } else if (jsOrder == "DESC") {
                $("#nameth").text("NameBook ↓");
            }
        }

        $("<tbody></tbody>").insertAfter("tbody:last").load(getBooksUrl + '?page=' + numPage + '&recPerPage=' + recPerPage);
        numPage = numPage + 1;
        // меняется количество книг
        $("#recqnt").click(function () {
            recPerPage = $("#recqnt").val();
        });
        // обработка клика по кнопке "Показать еще"
        $("#load").click(function () {
            $("<tbody></tbody>").insertAfter("tbody:last").load(getBooksUrl + '?page=' + numPage + '&recPerPage=' + recPerPage);
            numPage = numPage + 1;
        });
    });

    function jsSetAuthorOrder() {
        // меняем сортировку при клике по заголовку столбца с автором книги
        numPage = 1;
        $.get("${pageContext.request.contextPath}/setauthororder")
            .done(function() {
                // очищаем таблицу. селектор id таблицы и все элементы tbody внутри этой таблицы
                $("#emptytbody ~ tbody").remove();
                // меняем также цвет при клике по ячейке
                $("#authorth").css("background", "#ff99ff");
                $("#nameth").css("background", "#ccc");
                // меняем символ стрелки сортировки
                if ($("#authorth").text() =="Author ↑") {
                    $("#authorth").text("Author ↓");
                } else if ($("#authorth").text() =="Author ↓") {
                    $("#authorth").text("Author ↑");
                } else if ($("#authorth").text() =="Author") {
                    $("#authorth").text("Author ↑");
                    $("#nameth").text("NameBook");
                }
                // здесь вставляем <tbody></tbody> после заголовка таблицы с книгами. Селектор по id <thead>
                $("<tbody></tbody>").insertAfter("tbody:last").load(getBooksUrl + '?page=' + numPage + '&recPerPage=' + recPerPage);
                // увеличиваем счетчик отображенных страниц
                numPage = numPage + 1;
                }
            )
    }

    function jsSetNameOrder() {
        // меняем сортировку при клике по заголовку столбца с названием книги
        numPage = 1;
        $.get("${pageContext.request.contextPath}/setnameorder")
            .done(function() {
                // очищаем таблицу. селектор id таблицы и все элементы tbody внутри этой таблицы
                $("#emptytbody ~ tbody").remove();
                // меняем также цвет при клике по ячейке
                $("#authorth").css("background", "#ccc");
                $("#nameth").css("background", "#ff99ff");
                // меняем символ стрелки сортировки
                if ($("#nameth").text() =="NameBook ↑") {
                    $("#nameth").text("NameBook ↓");
                } else if ($("#nameth").text() =="NameBook ↓") {
                    $("#nameth").text("NameBook ↑");
                } else if ($("#nameth").text() =="NameBook") {
                    $("#authorth").text("Author");
                    $("#nameth").text("NameBook ↑");
                }
                // здесь вставляем <tbody></tbody> после заголовка таблицы с книгами. Селектор по id <thead>
                $("<tbody></tbody>").insertAfter("tbody:last").load(getBooksUrl + '?page=' + numPage + '&recPerPage=' + recPerPage);
                // увеличиваем счетчик отображенных страниц
                numPage = numPage + 1;
                }
            )
    }

    // удаляем книгу
    // bookid - id книги
    function jsDeleteBook(bookid) {
        var r = confirm("Удалить книгу с id="+bookid +"?");
        if (r == true) {
            $.get("${pageContext.request.contextPath}/delbook?idDelBook="+bookid)
                .done(function() {
                    window.location.href = "${pageContext.request.contextPath}/books"
                })
        }
    }

    // сдаем/берем книгу пользователем
    function jsChangeTaker(bookid, action, username) {
        // bookid - id книги
        // action - действие, 1 - взять, 0 - вернуть
        // username - имя "текущего" пользователя
        console.log("bookid", bookid);
        console.log("action", action);
        console.log("username", username);
        $.get("${pageContext.request.contextPath}/changetaker?bookid="+bookid+"&action="+action+"&username="+username)
            .done(function() {
                window.location.href = "${pageContext.request.contextPath}/books"
            })
    }

    // описание модального диалога добавления/редактирования книги
    $("#dialog").dialog({
        autoOpen: false,
        closeOnEscape: false,
        resizable: false,
        modal: true,
        close: function() {
            //location.reload();
            window.location.href = "${pageContext.request.contextPath}/books";
        }
    });

    function jsGetBookDetails(bookid) {
        //диалог с возможностью редактирования информации о книге
        $.get("${pageContext.request.contextPath}/getbookdetails?bookid="+bookid,
            function(data) {
                if (data.Result == 0) {
                    $("#isbn").prop("disabled", true);
                    $("#isbn").val(data.ISBN);
                    $("#author").val(data.author);
                    $("#name").val(data.name);
                    $("#dialog").dialog("option", {
                            title : "Книга \""+data.name +"\"" ,
                            buttons: {
                                "Сохранить": function() {
                                    var newAuthor = $("#author").val();
                                    var newName = $("#name").val();
                                    var newISBN = $("#isbn").val();
                                    if (newAuthor != "" && newName != "") {
                                        $.get("${pageContext.request.contextPath}/updatebookdetails?bookid="+bookid+"&newISBN="+newISBN+"&newAuthor="+newAuthor+"&newName="+newName,
                                            function(data) {
                                                if (data.Result == 0) {
                                                    alert("В данные книги внесены изменения!");
                                                } else {
                                                    alert("Ошибка при изменении данных книги!");
                                                }
                                            }, "json"
                                        )
                                            .done(function() {
                                                $("#dialog").dialog("close");
                                            })
                                    } else {alert("Автор и название не должны быть пустыми!")}
                                },
                                "Отмена": function() {
                                    $(this).dialog("close")
                                }

                            }
                        }
                    );
                    $("#dialog").dialog("open");
                }
            }, "json"
        )
    }

    // вызов диалога добавления книги
    $( "#bookdialog" ).click(function() {
        $("#isbn").val("");
        $("#author").val("");
        $("#name").val("");
        $("#isbn").prop("disabled", false);
        $("#dialog").dialog("option", {
                //заголовок
                title : "Добавить книгу",
                //кнопки
                buttons: {
                    "Сохранить": function() {
                        var isbn = $("#isbn").val();
                        var author = $("#author").val();
                        var name = $("#name").val();
                        var dialogExit = false;
                        if (isbn != "" && author != "" && name != "") {
                            $.get("${pageContext.request.contextPath}/addbook?newISBN="+isbn+"&newAuthor="+author+"&newName="+name,
                                function(data) {
                                    if (data.Result == 1) {
                                        alert("Книга с ISBN " + isbn + " уже существует! Укажите другой ISBN.");
                                    } else if (data.Result == 0) {
                                        dialogExit = true;
                                    }
                                }, "json"
                            )
                                .done(function() {
                                    if (dialogExit == true) {
                                        $("#dialog").dialog("close");
                                    }
                                })
                        } else {alert("Необходимо заполнить все поля!")};
                    },
                    "Отмена": function() {
                        $(this).dialog("close")
                    }

                }
            }
        );
        $("#dialog").dialog("open");
    });

</script>

</body>
</html>

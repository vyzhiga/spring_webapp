package org.duzer.webapp.book.dao.impl;

import org.duzer.webapp.book.model.Book;
import org.duzer.webapp.user.dao.impl.JdbcUserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by duzer on 03.02.2017.
 */
public class JdbcBookDAO implements BookDAO {


    final static Logger logger = LoggerFactory.getLogger(JdbcUserDAO.class);
    private DataSource dataSource;

    public String addBook(Book book) {
        /**
         * Добавляем книгу. Возвращает 1 - fail, 0 - success.
         */
        // для создания подключения к БД
        Connection con = null;
        PreparedStatement stmt = null;
        // кол-во книг с указанным ISBN
        int numBooks = 0;
        // результат попытки добавления книги, возвращаемый в JSON
        String res = "{\"Result\":1}";
        // строки запросов для preparedStatement
        // для SELECT
        String selectSQL = "SELECT COUNT(ISBN) FROM books WHERE ISBN = ?";
        // для INSERT
        String insertSQL = "INSERT INTO books(ISBN, author, name) VALUES(?, ?, ?)";

        try {
            con = dataSource.getConnection();
            // проверяем количество ниг с добавляемым ISBN. выполняем запрос с параметром
            stmt = con.prepareStatement(selectSQL);
            stmt.setString(1, book.getISBNBook());
            // первый ряд в ResultSet, т.к. COUNT, он должен быть единственный
            ResultSet rs = stmt.executeQuery();
            rs.first();
            numBooks = rs.getInt(1);
            // пишем в лог
            logger.debug("Number of books with ISBN '"+ book.getISBNBook() +"' in DB: "+String.valueOf(numBooks));

            if (numBooks==0) {
                // книги с таким ISBN именем отсутствуют, добавляем
                // готовим запрос INSERT
                stmt = con.prepareStatement(insertSQL);
                // параметры
                stmt.setString(1, book.getISBNBook());
                stmt.setString(2, book.getBookAuthor());
                stmt.setString(3, book.getNameBook());
                // выполняем
                stmt.executeUpdate();
                logger.debug("Added book with ISBN:" + book.getISBNBook() + ", author:" + book.getBookAuthor() + ", name: " + book.getNameBook());
                stmt.close();
                res = "{\"Result\":0}";
            } else {
                //пользователи существуют, пропускаем
                logger.debug("Book ISBN: "+ book.getISBNBook() +" already exists. Skipping.");
            }
        } catch (Exception e) {
            logger.error("!!! Error adding a book", e);
        } finally {
            closeQuiet(stmt);
            closeQuiet(con);
        }

        return res;

    }

    public void deleteBook(int bookId) {

        String deleteSQL = "DELETE FROM books WHERE id = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {

            con = dataSource.getConnection();
            stmt = con.prepareStatement(deleteSQL);
            stmt.setInt(1, bookId);
            stmt.executeQuery();
            logger.debug("Deleted user record with id=" + Integer.toString(bookId));
            stmt.close();

        } catch (Exception e) {

            logger.error("!!! Del users error", e);

        } finally {

            closeQuiet(stmt);
            closeQuiet(con);

        }

    }

    private void closeQuiet(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                logger.error("!!! Close error", e);
            }
        }
    }
}

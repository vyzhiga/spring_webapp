package org.duzer.webapp.book.dao.impl;

import org.duzer.webapp.book.dao.BookDAO;
import org.duzer.webapp.book.model.Book;
import org.duzer.webapp.user.dao.impl.JdbcUserDAO;
import org.duzer.webapp.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by duzer on 03.02.2017.
 */
public class JdbcBookDAO implements BookDAO {

    final static Logger logger = LoggerFactory.getLogger(JdbcBookDAO.class);

    private JdbcTemplate jdbcTemplate;

    public JdbcBookDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void saveOrUpdate(Book book) {

        if (book.getIdBook()>0) {
            // обновляем
            String updateSQL = "UPDATE books SET isbn=?, author=?, name=? WHERE id=?";
            jdbcTemplate.update(updateSQL, book.getISBNBook(), book.getBookAuthor(), book.getNameBook(), book.getIdBook());
        } else {
            // добавляем
            String insertSQL = "INSERT INTO books (isbn, author, name) VALUES (?, ?, ?)";
            jdbcTemplate.update(insertSQL, book.getISBNBook(), book.getBookAuthor(), book.getNameBook());
        }
    }

    @Override
    public void deleteBook(int bookId) {
        String deleteSQL = "DELETE FROM books WHERE id=?";
        jdbcTemplate.update(deleteSQL, bookId);
    }

    @Override
    public Book get(int bookId) {
        String selectSQL = "SELECT * FROM books WHERE id=" + bookId;
        return jdbcTemplate.query(selectSQL, new ResultSetExtractor<Book>() {

            @Override
            public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    Book book = new Book();
                    book.setIdBook(rs.getInt("id"));
                    book.setISBNBook(rs.getString("isbn"));
                    book.setBookAuthor(rs.getString("author"));
                    book.setNameBook(rs.getString("name"));
                    book.setBookTaker(rs.getInt("takerid"));
                    return book;
                }

                return null;
            }

        });
    }

    @Override
    public List<Book> list() {
        String selectSQL = "SELECT * FROM books";
        List<Book> listBook = jdbcTemplate.query(selectSQL, new RowMapper<Book>() {

            @Override
            public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
                Book aBook = new Book();

                aBook.setIdBook(rs.getInt("id"));
                aBook.setISBNBook(rs.getString("isbn"));
                aBook.setBookAuthor(rs.getString("author"));
                aBook.setNameBook(rs.getString("name"));
                aBook.setBookTaker(rs.getInt("takerid"));

                return aBook;
            }
        });

        return listBook;
    }

}

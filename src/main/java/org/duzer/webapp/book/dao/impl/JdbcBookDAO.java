package org.duzer.webapp.book.dao.impl;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.duzer.webapp.book.dao.BookDAO;
import org.duzer.webapp.book.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcBookDAO extends JdbcDaoSupport implements BookDAO {

    final static Logger logger = LoggerFactory.getLogger(JdbcBookDAO.class);

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    private JdbcTemplate jdbcTemplate;

    public JdbcBookDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public String saveOrUpdate(Book book) {
        // возвращает значение: 0 - ок, 1 - фейл
        String res = "{\"Result\":1}";

        logger.debug("JdbcBookDAO.saveOrUpdate book.getBookTaker() - "+book.getBookTaker());

        if (book.getIdBook()>0 && book.getBookTaker()== null) {
            // обновляем
            String updateSQL = "UPDATE books SET isbn=?, author=?, name=? WHERE id=?";
            jdbcTemplate.update(updateSQL, book.getISBNBook(), book.getBookAuthor(), book.getNameBook(), book.getIdBook());
            res = "{\"Result\":0}";
        } else if (book.getIdBook()>0 && book.getBookTaker()!= null) {
            if (book.getBookTaker() == "") {
                String updateSQL = "UPDATE books SET takerid=NULL WHERE id=?";
                jdbcTemplate.update(updateSQL, book.getIdBook());
            } else {
                String updateSQL = "UPDATE books SET takerid=(SELECT id FROM users WHERE name = ?) WHERE id =? ";
                jdbcTemplate.update(updateSQL, book.getBookTaker(), book.getIdBook());
            }
            res = "{\"Result\":0}";
        } else {
                // добавляем
                String selectSQL = "SELECT COUNT(isbn) FROM books WHERE isbn = ?";
                Object[] inputs = new Object[] {book.getISBNBook()};
                Integer num = getJdbcTemplate().queryForObject(selectSQL, inputs, Integer.class);
                if (num == 0) {
                    String insertSQL = "INSERT INTO books (isbn, author, name) VALUES (?, ?, ?)";
                    jdbcTemplate.update(insertSQL, book.getISBNBook(), book.getBookAuthor(), book.getNameBook());
                    res = "{\"Result\":0}";
                } else {
                    logger.debug("Book "+book.getISBNBook()+" already exists. Skipping addition.");
                }
            }

        return res;
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
                    book.setBookTaker(rs.getString("takerid"));
                    return book;
                }
                return null;
            }
        });
    }

    @Override
    public List<Book> list(int offset, int recPerPage, String curOrder, String Order) {

        String selectSQL = "SELECT B.id AS BookID, B.ISBN AS BookISBN, B.author AS BookAuthor, B.name AS BookName, U.name AS UserName FROM books AS B LEFT JOIN users AS U ON B.takerid = U.id ORDER BY " + curOrder +" "+ Order +", BookISBN LIMIT ? OFFSET ?";
        logger.debug(selectSQL);
        Object[] inputs = new Object[] {recPerPage, offset};
        List<Book> listBook = jdbcTemplate.query(selectSQL, inputs, new RowMapper<Book>() {

            @Override
            public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
                Book aBook = new Book();

                aBook.setIdBook(rs.getInt("BookID"));
                aBook.setISBNBook(rs.getString("BookISBN"));
                aBook.setBookAuthor(rs.getString("BookAuthor"));
                aBook.setNameBook(rs.getString("BookName"));
                aBook.setBookTaker(rs.getString("UserName"));

                return aBook;
            }
        });
        return listBook;
    }
}

package org.duzer.webapp.book.dao.impl;

/**
 * Created by duzer on 03.02.2017.
 */

import org.duzer.webapp.book.model.Book;
import java.util.List;

public interface BookDAO {

    public void saveOrUpdate(Book book);

    public void deleteBook(int bookId);

    public Book get(int bookId);

    public List<Book> list();

}

package org.duzer.webapp.dao;

/**
 * Created by duzer on 03.02.2017.
 */

import org.duzer.webapp.book.model.Book;

public interface BookDAO {

    public String addBook(Book user);

    public void deleteBook(int bookId);

}

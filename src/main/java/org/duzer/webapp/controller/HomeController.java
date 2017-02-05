package org.duzer.webapp.controller;

import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.javafx.sg.prism.NGShape;
import org.duzer.webapp.book.dao.BookDAO;
import org.duzer.webapp.book.model.Book;
import org.duzer.webapp.user.dao.UserDAO;
import org.duzer.webapp.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class HomeController {

    final private static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired private UserDAO UserDAO;

    @Autowired private BookDAO BookDAO;

    @Autowired HttpSession session;

    @PostConstruct
    public void init() {
        logger.debug("Run init");
    }

    // страница с пользователями
    @RequestMapping(value={"/", "/users"})
    public ModelAndView listUser(ModelAndView model) throws IOException{
        List<User> listUser = UserDAO.list();
        model.addObject("listUser", listUser);
        model.setViewName("users");
        return model;
    }

    // страница с книгами
    @RequestMapping(value={"/books"})
    public ModelAndView listBook(ModelAndView model) throws IOException{
        //List<Book> listBook = BookDAO.list();
        //model.addObject("listBook", listBook);
        model.setViewName("books");
        return model;
    }

    // отдаем список книг
    @RequestMapping(value={"/getbooks"})
    public ModelAndView getBooks(ModelAndView model, HttpServletRequest request, HttpSession session) {

        int pageNum = Integer.parseInt(request.getParameter("page"));
        int recNum = Integer.parseInt(request.getParameter("recPerPage"));

        String curOrder = (String) session.getAttribute("sesCurOrder");
        String Order = (String) session.getAttribute("sesOrder");

        logger.debug("Controller: calling BookDAO.list("+((pageNum - 1) * recNum)+", "+recNum+", "+curOrder+","+Order+")");

        List<Book> listBook = BookDAO.list((pageNum - 1) * recNum, recNum, curOrder, Order);
        model.addObject("listBook", listBook);
        model.setViewName("listBooks");
        return model;
    }

    // добавляем пользователя
    @RequestMapping(value = "/adduser", method = RequestMethod.GET)
    public void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String newUsername = request.getParameter("addUser");
        String newPassword = request.getParameter("addPass");
        User newUser = new User(newUsername, newPassword);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(UserDAO.saveOrUpdate(newUser));
    }

    // добавляем книгу
    @RequestMapping(value = "/addbook", method = RequestMethod.GET)
    public void addBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String newISBN = request.getParameter("newISBN");
        String newAuthor = request.getParameter("newAuthor");
        String newName = request.getParameter("newName");
        Book newBook = new Book(newAuthor, newName, newISBN, "");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(BookDAO.saveOrUpdate(newBook));
    }

    // удаляем книгу
    @RequestMapping(value = "/delbook", method = RequestMethod.GET)
    public ModelAndView deleteBook(HttpServletRequest request) {
        int bookId = Integer.parseInt(request.getParameter("idDelBook"));
        BookDAO.deleteBook(bookId);
        logger.debug("Deleted book with id="+bookId);
        return new ModelAndView("redirect:/users");
    }

    // удаляем пользователя
    @RequestMapping(value = "/deluser", method = RequestMethod.GET)
    public ModelAndView deleteContact(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("id"));
        UserDAO.deleteUser(userId);
        logger.debug("Deleted user with id="+userId);
        return new ModelAndView("redirect:/");
    }

    // данные о пользователе
    @RequestMapping(value = "/getuserdetails", method = RequestMethod.GET)
    public void getUserDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = Integer.parseInt(request.getParameter("userid"));
        User user = UserDAO.get(userId);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write("{\"user\":\""+user.getUserName()+"\", \"pass\":\""+user.getUserPass()+"\", \"Result\":0}");
    }

    // данные о книге
    @RequestMapping(value = "/getbookdetails", method = RequestMethod.GET)
    public void getBookDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    // обновляем информацию о пользователе
    @RequestMapping(value = "/updateuserpass", method = RequestMethod.GET)
    public void updateUserPass(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User uUser = new User();
        uUser.setUserId(Integer.parseInt(request.getParameter("userid")));
        uUser.setUserName(request.getParameter("username"));
        uUser.setUserPass(request.getParameter("newpass"));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(UserDAO.saveOrUpdate(uUser));
    }

    // обновляем информацию о книге
    @RequestMapping(value = "/updatebookdetails", method = RequestMethod.GET)
    public void updateBookDetails(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

}
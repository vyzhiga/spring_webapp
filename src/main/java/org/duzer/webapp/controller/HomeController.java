package org.duzer.webapp.controller;

import java.io.IOException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.duzer.webapp.book.dao.BookDAO;
import org.duzer.webapp.book.model.Book;
import org.duzer.webapp.user.dao.UserDAO;
import org.duzer.webapp.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    @RequestMapping(value={"/users"})
    public ModelAndView listUser(ModelAndView model) throws IOException {
        List<User> listUser = UserDAO.list();
        model.addObject("listUser", listUser);
        model.setViewName("users");
        return model;
    }

    // страница с книгами
    @RequestMapping(value={"/", "/books"})
    public ModelAndView listBook(ModelAndView model) throws IOException {
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
        return new ModelAndView("redirect:/books");
    }

    // удаляем пользователя
    @RequestMapping(value = "/deluser", method = RequestMethod.GET)
    public ModelAndView deleteContact(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("id"));
        UserDAO.deleteUser(userId);
        logger.debug("Deleted user with id="+userId);
        return new ModelAndView("redirect:/users");
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
        int bookId = Integer.parseInt(request.getParameter("bookid"));
        Book book = BookDAO.get(bookId);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write("{\"ISBN\":\""+book.getISBNBook()+"\", \"author\":\""+book.getBookAuthor()+"\", \"name\":\"" + book.getNameBook() + "\",\"Result\":0}");
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
        Book uBook = new Book();
        uBook.setIdBook(Integer.parseInt(request.getParameter("bookid")));
        uBook.setISBNBook(request.getParameter("newISBN"));
        uBook.setBookAuthor(request.getParameter("newAuthor"));
        uBook.setNameBook(request.getParameter("newName"));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(BookDAO.saveOrUpdate(uBook));
    }

    // взять/вернуть книгу
    @RequestMapping(value = "/changetaker", method = RequestMethod.GET)
    public ModelAndView changeTaker(HttpServletRequest request) {
        // bookid - id книги
        // action - действие, 1 - взять, 0 - вернуть
        // username - имя "текущего" пользователя
        int bookId = Integer.parseInt(request.getParameter("bookid"));
        Book book = BookDAO.get(bookId);
        String userName = request.getParameter("username");
        int action = Integer.parseInt(request.getParameter("action"));
        logger.debug("Controller changeTaker() Action: "+action+", bookid: "+bookId+", username: "+userName);
        if (action == 0) {
            book.setBookTaker("");
        } else if (action == 1) {
            book.setBookTaker(userName);
        }
        logger.debug("Controller changeTaker() book.getBookTaker() - "+book.getBookTaker());
        String res = BookDAO.saveOrUpdate(book);
        logger.debug("Controller changeTaker() result = "+res);
        return new ModelAndView("redirect:/books");
    }

    // сортировка по автору
    @RequestMapping(value = "/setauthororder", method = RequestMethod.GET)
    private void setAuthorOrder(HttpServletResponse response) throws IOException {
        logger.debug("Calling setAuthorOrder()");
        if (((String) session.getAttribute("sesCurOrder")).equals("BookAuthor")) {
            // устанавливаем параметр сессии, содержащий порядок отображения авторов книг
            logger.debug("Sorting column is Author, sesOrder is '" + ((String) session.getAttribute("sesOrder")) + "'");
            switch ((String) session.getAttribute("sesOrder")) {
                // был порядок возрасатющий - делаем убывающий
                case "ASC":
                    session.setAttribute("sesOrder", "DESC");
                    logger.debug("Set column=Author sesOrder to DESC.");
                    break;
                // наоборот
                case "DESC":
                    session.setAttribute("sesOrder", "ASC");
                    logger.debug("Set column=Author sesOrder to ASC.");
                    break;
                // заглушка, на всякий случай
                default:
                    session.setAttribute("sesOrder", "ASC");
                    logger.debug("Dummy has worked! Set column=Author sesAuthorOrder to ASC.");
                    break;
            }
        } else {
            session.setAttribute("sesCurOrder", "BookAuthor");
            session.setAttribute("sesOrder", "ASC");
            logger.debug("Set column=Author sesOrder to ASC.");
        }
        response.setStatus(200);
    }

    // сортировка по названию
    @RequestMapping(value = "/setnameorder", method = RequestMethod.GET)
        private void setNameOrder(HttpServletResponse response) throws IOException {
        logger.debug("Calling setNameOrder()");
        if (((String) session.getAttribute("sesCurOrder")).equals("BookName")) {
            // устанавливаем параметр сессии, содержащий порядок отображения наименований книг
            logger.debug("Sorting column is Name, sesOrder is '" + ((String) session.getAttribute("sesNameOrder")) + "'");
            switch ((String) session.getAttribute("sesOrder")) {
                // был порядок возрасатющий - делаем убывающий
                case "ASC":
                    session.setAttribute("sesOrder", "DESC");
                    logger.debug("Set column=Name sesOrder to DESC.");
                    break;
                // наоборот
                case "DESC":
                    session.setAttribute("sesOrder", "ASC");
                    logger.debug("Set column=Name sesOrder to ASC.");
                    break;
                // заглушка, на всякий случай
                default:
                    session.setAttribute("sesOrder", "ASC");
                    logger.debug("Dummy has worked! Set column=Name sesOrder to ASC.");
                    break;
            }
        } else {
            session.setAttribute("sesCurOrder", "BookName");
            session.setAttribute("sesOrder", "ASC");
            logger.debug("Set column=Name sesOrder to ASC.");
        }
        response.setStatus(200);
    }

    // дебаг пользователей
    @RequestMapping(value = "/usersdebug", method = RequestMethod.GET)
    public ModelAndView showUsersDebug(ModelAndView model) throws IOException {
        model.setViewName("usersdebug");
        return model;
    }

    // устанавливаем пользователя для дебага
    @RequestMapping(value = "/setdebuguser", method = RequestMethod.GET)
    public ModelAndView setDebugUser(ModelAndView model, HttpServletRequest request, HttpSession session) {
        session.setAttribute("sesCurUser", request.getParameter("username"));
        model.setViewName("usersdebug");
        return model;
    }
}
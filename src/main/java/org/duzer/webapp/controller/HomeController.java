package org.duzer.webapp.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    final static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserDAO UserDAO;

    @RequestMapping(value={"/", "/users"})
    public ModelAndView listUser(ModelAndView model) throws IOException{
        List<User> listUser = UserDAO.list();
        model.addObject("listUser", listUser);
        model.setViewName("users");
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

    // удаляем пользователя
    @RequestMapping(value = "/deluser", method = RequestMethod.GET)
    public ModelAndView deleteContact(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getParameter("id"));
        UserDAO.deleteUser(userId);
        logger.debug("Deleted user with id="+userId);
        return new ModelAndView("redirect:/");
    }

/*    @RequestMapping(value = "/newContact", method = RequestMethod.GET)
    public ModelAndView newContact(ModelAndView model) {
        Contact newContact = new Contact();
        model.addObject("contact", newContact);
        model.setViewName("ContactForm");
        return model;
    }

    @RequestMapping(value = "/saveContact", method = RequestMethod.POST)
    public ModelAndView saveContact(@ModelAttribute Contact contact) {
        contactDAO.saveOrUpdate(contact);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/editContact", method = RequestMethod.GET)
    public ModelAndView editContact(HttpServletRequest request) {
        int contactId = Integer.parseInt(request.getParameter("id"));
        Contact contact = contactDAO.get(contactId);
        ModelAndView model = new ModelAndView("ContactForm");
        model.addObject("contact", contact);
        return model;
    } */
}
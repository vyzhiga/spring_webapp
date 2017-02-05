package org.duzer.webapp.user.dao;

import org.duzer.webapp.user.model.User;
import java.util.List;

public interface UserDAO {

    public String saveOrUpdate(User user);

    public void deleteUser(int userId);

    public User get(int userId);

    public User getByName(String userName);

    public List<User> list();
}
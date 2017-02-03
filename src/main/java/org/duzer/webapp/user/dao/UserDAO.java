package org.duzer.webapp.user.dao;

import org.duzer.webapp.user.model.User;
import java.util.List;

public interface UserDAO {

    public void saveOrUpdate(User user);

    public void deleteUser(int userId);

    public User get(int userId);

    public List<User> list();
}
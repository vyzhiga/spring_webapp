package org.duzer.webapp.user.dao;

import org.duzer.webapp.user.model.User;

public interface UserDAO {

    public String addUser(User user);

    public void deleteUser(int userId);

}
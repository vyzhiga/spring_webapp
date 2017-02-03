package org.duzer.webapp.user.dao.impl;

/**
 * Created by duzer on 03.02.2017.
 */

import java.sql.*;
import javax.sql.DataSource;
import org.duzer.webapp.user.dao.UserDAO;
import org.duzer.webapp.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcUserDAO implements UserDAO {

    final static Logger logger = LoggerFactory.getLogger(JdbcUserDAO.class);
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String addUser(User user) {
        /**
         * Добавляем пользователя. Возвращает 1 - fail, 0 - success.
         */
        // строки запросов для preparedStatement
        // для SELECT
        String selectSQL = "SELECT COUNT (name) FROM users WHERE name = ?";
        // для INSERT
        String insertSQL = "INSERT INTO users (name, password) VALUES (?, ?)";

        Connection con = null;
        PreparedStatement stmt = null;

        int numUsers = 0;
        String res = "{\"Result\":1}";

        try {

            con = dataSource.getConnection();
            stmt = con.prepareStatement(insertSQL);
            stmt.setString(1, user.getUserName());
            ResultSet rs = stmt.executeQuery();
            rs.first();
            numUsers = rs.getInt(1);

            logger.debug("Number of users '"+user.getUserName()+"' in DB: "+String.valueOf(numUsers));

            if (numUsers==0) {
                //пользователи с таким именем отсутствуют, добавляем
                // готовим запрос INSERT
                stmt = con.prepareStatement(insertSQL);
                // параметры
                stmt.setString(1, user.getUserName());
                stmt.setString(2, user.getUserPass());
                // выполняем
                stmt.executeUpdate();
                logger.debug("Added user with passwd: " + user.getUserName() + ":" + user.getUserPass());
                stmt.close();
                res = "{\"Result\":0}";
            } else {
                //пользователи существуют, пропускаем
                logger.debug("User "+user.getUserName()+" already exists. Skipping.");
            }

        } catch (Exception e) {
            logger.error("!!! Error adding a user", e);
        } finally {
            closeQuiet(stmt);
            closeQuiet(con);
        }

        return res;

    }

    public void deleteUser(int userId) {

        String deleteSQL = "DELETE FROM users WHERE id = ?";

        Connection con = null;
        PreparedStatement stmt = null;

        try {
            con = dataSource.getConnection();
            stmt = con.prepareStatement(deleteSQL);

            stmt.setInt(1, userId);

            stmt.executeQuery();

            logger.debug("Deleted user record with id=" + Integer.toString(userId));
            stmt.close();
        } catch (Exception e) {
            logger.error("!!! Del users error", e);
        } finally {
            closeQuiet(stmt);
            closeQuiet(con);
        }

    }

    private void closeQuiet(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                logger.error("!!! Close error", e);
            }
        }
    }

}

package org.duzer.webapp.user.dao.impl;

import java.sql.*;
import java.util.List;
import javax.sql.DataSource;
import org.duzer.webapp.user.dao.UserDAO;
import org.duzer.webapp.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class JdbcUserDAO implements UserDAO {

    final static Logger logger = LoggerFactory.getLogger(JdbcUserDAO.class);
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    public JdbcUserDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveOrUpdate(User user) {

        if (user.getUserId()>0) {
            // обновляем
            String updateSQL = "UPDATE users SET name=?, password=? WHERE id=?";
            jdbcTemplate.update(updateSQL, user.getUserName(), user.getUserPass(), user.getUserId());
        } else {
            // добавляем
            String insertSQL = "INSERT INTO users (name, password) VALUES (?, ?)";
            jdbcTemplate.update(insertSQL, user.getUserName(), user.getUserPass());
        }

    }

/*    public String addUser(User user) {
        /**
         * Добавляем пользователя. Возвращает 1 - fail, 0 - success.
         */
/*        // строки запросов для preparedStatement
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

    } */

    public void deleteUser(int userId) {

        String deleteSQL = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(deleteSQL, userId);

/*        Connection con = null;
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
*/
    }

    public User get(int userId) {
        String selectSQL = "SELECT * FROM users WHERE id=" + userId;
        return jdbcTemplate.query(selectSQL, new ResultSetExtractor<User>() {

            @Override
            public User extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("id"));
                    user.setUserName(rs.getString("name"));
                    user.setUserPass(rs.getString("password"));
                    return user;
                }

                return null;
            }

        });
    }

    public List<User> list() {
        String selectSQL = "SELECT * FROM users";
        List<User> listUser = jdbcTemplate.query(selectSQL, new RowMapper<User>() {

            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User aUser = new User();

                aUser.setUserId(rs.getInt("id"));
                aUser.setUserName(rs.getString("name"));
                aUser.setUserPass(rs.getString("password"));

                return aUser;
            }

        });

        return listUser;
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

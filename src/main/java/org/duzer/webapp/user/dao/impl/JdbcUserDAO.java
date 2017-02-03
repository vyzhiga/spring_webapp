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

    @Override
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

    @Override
    public void deleteUser(int userId) {

        String deleteSQL = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(deleteSQL, userId);

    }

    @Override
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

    @Override
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

}

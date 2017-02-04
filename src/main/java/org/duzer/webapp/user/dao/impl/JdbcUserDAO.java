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
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class JdbcUserDAO extends JdbcDaoSupport implements UserDAO { //doesn't work
//public class JdbcUserDAO implements UserDAO { //works

    final static Logger logger = LoggerFactory.getLogger(JdbcUserDAO.class);
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    public JdbcUserDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public String saveOrUpdate(User user) {
        // возвращает значение: 0 - ок, 1 - фейл

        String res = "{\"Result\":1}";

        if (user.getUserId()>0) {
            // обновляем
            String updateSQL = "UPDATE users SET name=?, password=? WHERE id=?";
            jdbcTemplate.update(updateSQL, user.getUserName(), user.getUserPass(), user.getUserId());
            res = "{\"Result\":0}";
        } else {
            // добавляем
            String selectSQL = "SELECT COUNT(name) FROM users WHERE name = ?";
            Object[] inputs = new Object[] {user.getUserName()};
            Integer num = getJdbcTemplate().queryForObject(selectSQL, inputs, Integer.class);
            if (num == 0) {
                String insertSQL = "INSERT INTO users (name, password) VALUES (?, ?)";
                jdbcTemplate.update(insertSQL, user.getUserName(), user.getUserPass());
                logger.debug("Added user username:password - " + user.getUserName() + ":" + user.getUserPass() + " .");
                res = "{\"Result\":0}";
            } else {
                logger.debug("User "+user.getUserName()+" already exists. Skipping.");
            }
        }

        return res;

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

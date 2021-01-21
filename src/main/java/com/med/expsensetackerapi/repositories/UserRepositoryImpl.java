package com.med.expsensetackerapi.repositories;

import com.med.expsensetackerapi.domain.User;
import com.med.expsensetackerapi.exceptions.EtAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final String SQL_CREATE = "INSERT INTO et_users(user_id,first_name,last_name,email,password) VALUES(NEXTVAL('et_users_seq'),?,?,?,?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM et_users WHERE email = ?";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM et_users WHERE user_id = ? ";
    private static final String SQL_FIND_BY_EMAIL = "SELECT * FROM et_users WHERE email = ? ";


    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public Integer create(String fName, String lName, String email, String password) throws EtAuthException {
        String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt(10));
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,fName);
                ps.setString(2,lName);
                ps.setString(3,email);
                ps.setString(4,hashedPassword);
                return ps;
                },keyHolder);
            return (Integer) keyHolder.getKeys().get("user_id");
        }catch (Exception e){
            System.out.println("the problem is +++++++++++ => "+e.getMessage());
            throw new EtAuthException("Invalid details. Failed to create account");
        }

    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws EtAuthException {
        try{
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_EMAIL, userRowMapper , email);
            if (!BCrypt.checkpw(password,user.getPassword()))
                throw new EtAuthException("Invalid email/password");
            return user;
        }catch (Exception e){
            throw new EtAuthException("Invalid email/password");
        }

    }

    @Override
    public Integer getCountByEmail(String email) {

        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL,Integer.class,email);
    }

    @Override
    public User findById(int userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, userRowMapper , userId);
    }

    private final RowMapper<User> userRowMapper = ((rs, rowNum)-> new User(rs.getInt(1),
            rs.getString(2),
            rs.getString(3),
            rs.getString(4),
            rs.getString(5)
            ));
}

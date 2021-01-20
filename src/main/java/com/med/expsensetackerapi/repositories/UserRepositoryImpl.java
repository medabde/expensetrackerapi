package com.med.expsensetackerapi.repositories;

import com.med.expsensetackerapi.domain.User;
import com.med.expsensetackerapi.exceptions.EtAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final String SQL_CREATE = "INSERT INTO ET_USERS(USER_ID,FIRST_NAME,LAST_NAME,EMAIL,PASSWORD) VALUES('ET_USERS_SEQ',?,?,?,?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM ET_USERS WHERE EMAIL = ?";
    private static final String SQL_FIND_BY_ID = "SLECT * FROM ET_USERS WHERE USER_ID = ? ";


    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public int create(String fName, String lName, String email, String password) throws EtAuthException {

        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,fName);
                ps.setString(2,lName);
                ps.setString(3,email);
                ps.setString(4,password);
                return ps;
                },keyHolder);
            return (Integer) keyHolder.getKeys().get("USER_ID");
        }catch (Exception e){
            throw new EtAuthException("Invalid details. Failed to create account");
        }

    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws EtAuthException {
        return null;
    }

    @Override
    public int getCountByEmail(String email) {


        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{email},Integer.class);
    }

    @Override
    public User findById(int userId) {
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL, new Object[]{userId},userRowMapper);
    }

    private RowMapper<User> userRowMapper = ((rs,rowNum)->{
        return new User(rs.getInt("USER_ID"),
                rs.getString("FIRST_NAME"),
                rs.getString("LAST_NAME"),
                rs.getString("EMAIL"),
                rs.getString("PASSWORD")
                );
    });
}

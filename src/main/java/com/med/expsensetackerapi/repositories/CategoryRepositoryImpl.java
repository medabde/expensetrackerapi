package com.med.expsensetackerapi.repositories;

import com.med.expsensetackerapi.domain.Category;
import com.med.expsensetackerapi.exceptions.EtAuthException;
import com.med.expsensetackerapi.exceptions.EtBadRequestException;
import com.med.expsensetackerapi.exceptions.EtResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
    private static final String SQL_CREATE = "INSERT INTO et_categories(category_id,user_id,title,description) VALUES(NEXTVAL('et_categories_seq'),?,?,?)";
    private static final String SQL_FIND_BY_ID = "SELECT c.category_id,c.user_id,c.title,c.description, " +
            "COALESCE(SUM(t.amount),0) total_expense " +
            "FROM et_transactions t RIGHT OUTER JOIN et_categories c ON c.category_id = t.category_id " +
            "WHERE c.user_id = ? AND c.category_id = ? GROUP BY c.category_id";
    private static final String SQL_FIND_ALL = "SELECT c.category_id,c.user_id,c.title,c.description, " +
            "COALESCE(SUM(t.amount),0) total_expense " +
            "FROM et_transactions t RIGHT OUTER JOIN et_categories c ON c.category_id = t.category_id " +
            "WHERE c.user_id = ? GROUP BY c.category_id";
    @Autowired
    JdbcTemplate jdbcTemplate;


    @Override
    public List<Category> findAll(Integer userId) throws EtResourceNotFoundException {
        return jdbcTemplate.query(SQL_FIND_ALL,categoryRowMapper,userId);
    }

    @Override
    public Category findById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        try{
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID,categoryRowMapper, userId,categoryId);
        }catch (Exception e){
            throw new EtResourceNotFoundException("Category not found");
        }
    }

    @Override
    public Integer create(Integer userId, String title, String description) throws EtBadRequestException {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1,userId);
                ps.setString(2,title);
                ps.setString(3,description);
                return ps;
            },keyHolder);
            return (Integer) Objects.requireNonNull(keyHolder.getKeys()).get("category_id");
        }catch (Exception e){
            throw new EtAuthException("Invalid details. Failed to create category");
        }
    }

    @Override
    public void update(Integer userId, Integer categoryId, Category category) throws EtBadRequestException {

    }

    @Override
    public void removeById(Integer userId, Integer categoryId) {

    }

    private final RowMapper<Category> categoryRowMapper = ((resultSet, i) -> new Category(resultSet.getInt("category_id"),
            resultSet.getInt("user_id"),
            resultSet.getString("title"),
            resultSet.getString("description"),
            resultSet.getDouble("total_expense")));


}

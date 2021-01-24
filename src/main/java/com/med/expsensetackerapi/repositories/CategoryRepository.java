package com.med.expsensetackerapi.repositories;

import com.med.expsensetackerapi.domain.Category;
import com.med.expsensetackerapi.exceptions.EtBadRequestException;
import com.med.expsensetackerapi.exceptions.EtResourceNotFoundException;

import java.util.List;

public interface CategoryRepository {
    List<Category> findAll(Integer userId) throws EtResourceNotFoundException;
    Category findById(Integer userId,Integer categoryId) throws EtResourceNotFoundException;
    Integer create(Integer userId,String title, String description) throws EtBadRequestException;
    void update(Integer userId,Integer categoryId, Category category) throws EtBadRequestException;
    void removeById(Integer userId,Integer categoryId);
}

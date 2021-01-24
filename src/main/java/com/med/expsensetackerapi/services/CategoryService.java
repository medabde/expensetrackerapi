package com.med.expsensetackerapi.services;

import com.med.expsensetackerapi.domain.Category;
import com.med.expsensetackerapi.exceptions.EtBadRequestException;
import com.med.expsensetackerapi.exceptions.EtResourceNotFoundException;

import java.util.List;

public interface CategoryService {
    List<Category> fetchAllCategories(Integer userId);
    Category fetchCategoryById(Integer userId,Integer categoryId) throws EtResourceNotFoundException;
    Category addCategory(Integer userId,String title,String description) throws EtBadRequestException;

    void updateCategory(Integer userId,Integer categoryId,Category category) throws EtBadRequestException;
    void removeCategoryWithAllTransactions(Integer userId,Integer categoryId) throws EtResourceNotFoundException;

}

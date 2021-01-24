package com.med.expsensetackerapi.resources;


import com.med.expsensetackerapi.domain.Category;
import com.med.expsensetackerapi.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryResource {

    @Autowired
    CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories(HttpServletRequest request){
        int userId = (Integer) request.getAttribute("userId");
        List<Category> categories = categoryService.fetchAllCategories(userId);

        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryById(HttpServletRequest request,@PathVariable("categoryId") Integer categoryId){
        int userId = (Integer) request.getAttribute("userId");
        Category category = categoryService.fetchCategoryById(userId,categoryId);
        return new ResponseEntity<>(category,HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<Category> addCategory(HttpServletRequest request, @RequestBody Map<String,Object> categoryMap){
        int userId = (Integer) request.getAttribute("userId");
        String title = (String) categoryMap.get("title");
        String description = (String) categoryMap.get("description");

        Category category = categoryService.addCategory(userId,title,description);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

}

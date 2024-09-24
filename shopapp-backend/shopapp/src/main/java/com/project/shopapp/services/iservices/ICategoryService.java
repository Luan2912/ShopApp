package com.project.shopapp.services.iservices;


import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;

import java.util.List;

public interface ICategoryService {
    void createCategory(CategoryDTO category);

    Category getCategoryById(long id);

    List<Category> getAllCategories();

    Category updateCategory(long categoryId, CategoryDTO category);

    void deleteCategory(long id);
}

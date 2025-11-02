package se331.metricbackend.service;

import se331.metricbackend.dto.CategoryDTO;
import se331.metricbackend.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(String id);
    Category createCategory(CategoryDTO categoryDTO);
    Category updateCategory(String id, CategoryDTO categoryDTO);
    void deleteCategory(String id);
}
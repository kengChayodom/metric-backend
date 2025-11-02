package se331.metricbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se331.metricbackend.dao.CategoryDao;
import se331.metricbackend.dto.CategoryDTO;
import se331.metricbackend.entity.Category;
import se331.metricbackend.util.LapMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    final CategoryDao categoryDao;

    @Override
    public List<Category> getAllCategories() {
        return categoryDao.findAll();
    }

    @Override
    public Category getCategoryById(String id) {
        return categoryDao.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = LapMapper.INSTANCE.toCategory(categoryDTO);
        return categoryDao.save(category);
    }

    @Override
    public Category updateCategory(String id, CategoryDTO categoryDTO) {
        Category existingCategory = getCategoryById(id);
        LapMapper.INSTANCE.updateCategoryFromDto(categoryDTO, existingCategory);
        return categoryDao.save(existingCategory);
    }

    @Override
    public void deleteCategory(String id) {
        categoryDao.deleteById(id);
    }
}
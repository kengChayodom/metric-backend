package se331.metricbackend.dao;

import se331.metricbackend.entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryDao {
    List<Category> findAll();
    Optional<Category> findById(String id);
    Category save(Category category);
    void deleteById(String id);
}
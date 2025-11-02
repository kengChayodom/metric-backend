package se331.metricbackend.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se331.metricbackend.entity.Category;
import se331.metricbackend.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryDaoImpl implements CategoryDao {

    final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(String id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(String id) {
        categoryRepository.deleteById(id);
    }
}
package se331.metricbackend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import se331.metricbackend.entity.Category;

// ไม่ต้องทำอะไรซับซ้อน MongoRepository มี findAllById() ให้เราใช้
public interface CategoryRepository extends MongoRepository<Category, String> {
}
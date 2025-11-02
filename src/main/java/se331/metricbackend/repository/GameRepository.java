package se331.metricbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import se331.metricbackend.entity.Game;

import java.util.Optional;

// 1. เปลี่ยนเป็น MongoRepository และ ID เป็น String
public interface GameRepository extends MongoRepository<Game, String> {

    // 2. Spring Data MongoDB จะสร้างคิวรีสำหรับค้นหา title ให้อัตโนมัติ
    // (ค้นหาแบบ case-insensitive และมีคำว่า title อยู่)
    Page<Game> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Game> findByCategories_Id(String categoryId, Pageable pageable);

    Page<Game> findByTitleContainingIgnoreCaseAndCategories_Id(String title, String categoryId, Pageable pageable);
}
package se331.metricbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se331.metricbackend.dto.CategoryDTO;
import se331.metricbackend.entity.Category;
import se331.metricbackend.service.CategoryService;
import se331.metricbackend.util.LapMapper; // ◀️ เพิ่ม Import
import java.util.List; // ◀️ เพิ่ม Import

@RestController
@RequestMapping("/categories") // ◀️ แก้ไข Path
@RequiredArgsConstructor
public class CategoryController {

    final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        // ◀️ แปลง List<Category> -> List<CategoryDTO>
        return ResponseEntity.ok(LapMapper.INSTANCE.toCategoryDTOs(categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") String id) {
        Category category = categoryService.getCategoryById(id);
        // ◀️ แปลง Category -> CategoryDTO
        return ResponseEntity.ok(LapMapper.INSTANCE.toCategoryDTO(category));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO) {
        Category newCategory = categoryService.createCategory(categoryDTO);
        // ◀️ แปลง Category -> CategoryDTO
        return ResponseEntity.ok(LapMapper.INSTANCE.toCategoryDTO(newCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable("id") String id,
            @RequestBody CategoryDTO categoryDTO
    ) {
        Category updatedCategory = categoryService.updateCategory(id, categoryDTO);
        // ◀️ แปลง Category -> CategoryDTO
        return ResponseEntity.ok(LapMapper.INSTANCE.toCategoryDTO(updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category with id " + id + " has been deleted.");
    }
}
package org.capps.news.web.controller;

import org.capps.news.model.Category;
import org.capps.news.service.CategoryService;
import org.capps.news.web.vm.mapper.CategoryVMMapper;
import org.capps.news.web.vm.request.CategoryReqVM;
import org.capps.news.web.vm.response.CategoryResVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryVMMapper categoryVMMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, CategoryVMMapper categoryVMMapper) {
        this.categoryService = categoryService;
        this.categoryVMMapper = categoryVMMapper;
    }

    @GetMapping("/categories")
    public List<CategoryResVM> getAll() {
        return categoryService.getAllCategories().stream()
                .map(categoryVMMapper::categoryToCategoryResVM)
                .collect(Collectors.toList());
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryResVM> getById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(categoryVMMapper::categoryToCategoryResVM)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/category")
    public ResponseEntity<CategoryResVM> create(@RequestBody CategoryReqVM categoryReqVM) {
        Category category = categoryVMMapper.categoryReqVMToCategory(categoryReqVM);
        Category createdCategory = categoryService.createCategory(category);
        CategoryResVM response = categoryVMMapper.categoryToCategoryResVM(createdCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<CategoryResVM> update(@PathVariable Long id, @RequestBody CategoryReqVM categoryReqVM) {
        Category category = categoryVMMapper.categoryReqVMToCategory(categoryReqVM);
        Category updatedCategory = categoryService.updateCategory(id, category);
        CategoryResVM response = categoryVMMapper.categoryToCategoryResVM(updatedCategory);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
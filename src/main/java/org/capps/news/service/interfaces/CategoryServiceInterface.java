package org.capps.news.service.interfaces;

import org.capps.news.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryServiceInterface {

    List<Category> getAllCategories();

    Optional<Category> getCategoryById(Long id);

    Category createCategory(Category category);

    Category updateCategory(Long id, Category categoryDetails);

    void deleteCategory(Long id);
}

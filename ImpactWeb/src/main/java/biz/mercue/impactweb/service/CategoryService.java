package biz.mercue.impactweb.service;

import biz.mercue.impactweb.model.Category;

import java.util.List;

public interface CategoryService {
    Category getById(String id);

    List<Category> getCategoryList();

    void createCategory(Category editCategory);

    void updateCategory(Category editCategory);

    void updateCategoryList(List<Category> categoryList);

	void updateCategoryToDefault(String categoryId);
    
    void deleteCategoryById(String categoryId);
}

package biz.mercue.impactweb.dao;

import biz.mercue.impactweb.model.Category;

import java.util.List;

public interface CategoryDao {
    Category getById(String id);

    List<Category> getCategoryList();

    void createCategory(Category category);

    void deleteCategory(Category category);
}

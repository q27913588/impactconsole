package biz.mercue.impactweb.dao;

import biz.mercue.impactweb.model.Category;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryDaoImpl extends AbstractDao<String, Category> implements CategoryDao {
    @Override
    public Category getById(String id) {
        return getByKey(id);
    }

    @Override
    public List<Category> getCategoryList() {
        Criteria criteria = createEntityCriteria();
        criteria.addOrder(Order.asc("category_order"));
        return criteria.list();
    }

    @Override
    public void createCategory(Category category) {
        persist(category);
    }

    @Override
    public void deleteCategory(Category category) {
        delete(category);
    }
}

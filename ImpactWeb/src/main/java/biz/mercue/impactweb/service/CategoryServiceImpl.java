package biz.mercue.impactweb.service;

import biz.mercue.impactweb.dao.CategoryDao;
import biz.mercue.impactweb.dao.DownloadsDao;
import biz.mercue.impactweb.dao.NewsDao;
import biz.mercue.impactweb.model.Category;
import biz.mercue.impactweb.model.Downloads;
import biz.mercue.impactweb.util.Constants;
import biz.mercue.impactweb.util.CustomException;
import biz.mercue.impactweb.util.KeyGeneratorUtils;
import biz.mercue.impactweb.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CategoryServiceImpl implements CategoryService {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private DownloadsDao downloadsDao;

    @Override
    public Category getById(String id) {
        Category dbCategory = categoryDao.getById(id);

        if (dbCategory != null) {
            dbCategory.getListDownloads().size();
        }

        return dbCategory;
    }

    @Override
    public List<Category> getCategoryList() {
        List<Category> dbCategoryList = categoryDao.getCategoryList();

        if (!dbCategoryList.isEmpty()) {
            for (Category category : dbCategoryList) {
                category.getListDownloads().size();
            }
        }

        return dbCategoryList;
    }

    @Override
    public void createCategory(Category editCategory) {
        if (StringUtils.isNULL(editCategory.getCategory_id())) {
            editCategory.setCategory_id(KeyGeneratorUtils.generateRandomString());
        }
        categoryDao.createCategory(editCategory);
    }

    @Override
    public void updateCategory(Category editCategory) {
        Category dbCategory = categoryDao.getById(editCategory.getCategory_id());
        if (dbCategory == null) {
            throw new CustomException.CanNotFindDataException();
        }
        dbCategory.setCategory_id(editCategory.getCategory_id());
        dbCategory.setCategory_name(editCategory.getCategory_name());
        dbCategory.setListDownloads(editCategory.getListDownloads());
    }

    @Override
    public void updateCategoryList(List<Category> categoryList) {
        for (Category category : categoryList) {
            updateCategory(category);
        }
    }

    @Override
    public void updateCategoryToDefault(String categoryId) {
        Category dbCategory = categoryDao.getById(categoryId);

        if (dbCategory == null) {
            throw new CustomException.CanNotFindDataException();
        }

        List<Downloads> dbDownloadsList = dbCategory.getListDownloads();

        Category defaultCategory = categoryDao.getById(Constants.CATEGORY_DEFAULT_ID);
        for (Downloads dbDownloads : dbDownloadsList) {
            dbDownloads.setCategory(defaultCategory);
        }
    }
    
    @Override
    public void deleteCategoryById(String categoryId) {
        Category category = categoryDao.getById(categoryId);
        categoryDao.deleteCategory(category);
    }
}

package biz.mercue.impactweb.controller;

import biz.mercue.impactweb.model.AdminToken;
import biz.mercue.impactweb.model.Category;
import biz.mercue.impactweb.model.View;
import biz.mercue.impactweb.service.AdminService;
import biz.mercue.impactweb.service.AdminTokenService;
import biz.mercue.impactweb.service.CategoryService;
import biz.mercue.impactweb.util.*;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CategoryController {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminTokenService adminTokenService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/api/getcategory/{id}", produces = Constants.CONTENT_TYPE_JSON)
    public String getCategory(HttpServletRequest request, @PathVariable String id) {
        log.info("/api/getcategory/: " + id);
        BeanResponseBody responseBody = new BeanResponseBody();
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
            return responseBody.getJacksonString(View.Category.class);
        }

        Category category = categoryService.getById(id);
        responseBody.setBean(category);
        responseBody.setCode(Constants.INT_SUCCESS);
        return responseBody.getJacksonString(View.Category.class);
    }

    @GetMapping(value = "/api/getcategorylist", produces = Constants.CONTENT_TYPE_JSON)
    public String getCategoryList(HttpServletRequest request) {
        log.info("/api/getcategorylist");
        ListResponseBody responseBody = new ListResponseBody();
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
            return responseBody.getJacksonString(View.Category.class);
        }

        List<Category> categoryList = categoryService.getCategoryList();
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setList(categoryList);
        return responseBody.getJacksonString(View.Category.class);
    }

    @PostMapping(value = "/api/updatecategory", produces = Constants.CONTENT_TYPE_JSON)
    public String updateCategoryList(HttpServletRequest request, @RequestBody String receiveJSONString) {
        log.info("/api/updatecategory");
        BeanResponseBody responseBody = new BeanResponseBody();
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
            return responseBody.getJacksonString(View.Category.class);
        }

        Category category = (Category) JacksonJSONUtils.readValue(receiveJSONString, Category.class);
        categoryService.updateCategory(category);
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setBean(category);
        return responseBody.getJacksonString(View.Category.class);
    }

    @PostMapping(value = "/api/addcategory", produces = Constants.CONTENT_TYPE_JSON)
    public String addCategory(HttpServletRequest request, @RequestBody String receiveJSONString) {
        BeanResponseBody responseBody = new BeanResponseBody();
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
            return responseBody.getJacksonString(View.Category.class);
        }

        Category category = (Category) JacksonJSONUtils.readValue(receiveJSONString, Category.class);
        categoryService.createCategory(category);
        responseBody.setCode(Constants.INT_SUCCESS);
        responseBody.setBean(category);
        return responseBody.getJacksonString(View.Category.class);
    }

    @PostMapping(value = "/api/deletecategory", produces = Constants.CONTENT_TYPE_JSON)
    public String deleteCategory(HttpServletRequest request, @RequestBody String receiveJSONString) {
        BeanResponseBody responseBody = new BeanResponseBody();
        AdminToken tokenBean = adminTokenService.getById(JWTUtils.getJwtToken(request));
        if (tokenBean == null) {
            responseBody.setCode(Constants.INT_ACCESS_TOKEN_ERROR);
            return responseBody.getJacksonString(View.Category.class);
        }

        JSONObject jsonObject = new JSONObject(receiveJSONString);
        String categoryId = jsonObject.optString("category_id");
        categoryService.updateCategoryToDefault(categoryId);
        categoryService.deleteCategoryById(categoryId);
        responseBody.setCode(Constants.INT_SUCCESS);
        return responseBody.getJacksonString(View.Category.class);
    }
}

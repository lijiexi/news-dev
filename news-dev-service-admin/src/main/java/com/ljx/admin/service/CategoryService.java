package com.ljx.admin.service;

import com.ljx.pojo.AdminUser;
import com.ljx.pojo.Category;
import com.ljx.pojo.bo.NewAdminBO;
import com.ljx.utils.PagedGridResult;

import java.util.List;

public interface CategoryService {
    /**
     * 新增文章分类
     */
    public void createCategory(Category category);

    /**
     * 查询分类是否存在
     */
    public boolean queryCatIsExist(String catName,String oldCatName);
    /**
     * 修改文章分类
     */
    public void modifyCategory(Category category);
    /**
     * 获得文章分类
     * 管理员查询，使用selectall查库
     */
    public List<Category> queryCategoryList();

}

package com.ljx.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ljx.admin.mapper.AdminUserMapper;
import com.ljx.admin.mapper.CategoryMapper;
import com.ljx.admin.service.AdminUserService;
import com.ljx.admin.service.CategoryService;
import com.ljx.api.service.BaseService;
import com.ljx.exception.GraceException;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.AdminUser;
import com.ljx.pojo.Category;
import com.ljx.pojo.bo.NewAdminBO;
import com.ljx.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class  CategoryServiceImpl extends BaseService implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Transactional
    public void createCategory(Category category) {
        int res = categoryMapper.insert(category);
        if(res != 1) {
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        //删除redis数据
        redis.del(REDIS_ALL_CATEGORY);
    }

    @Override
    public boolean queryCatIsExist(String catName, String oldCatName) {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name",catName);
        if(StringUtils.isNotBlank(oldCatName)){
            criteria.andEqualTo("name",oldCatName);
        }
        List<Category> catList = categoryMapper.selectByExample(example);
        boolean isExist = false;
        if(catList != null && !catList.isEmpty() && catList.size() > 0) {
            isExist = true;
        }
        return isExist;
    }

    @Override
    @Transactional
    public void modifyCategory(Category category) {
        int res = categoryMapper.updateByPrimaryKey(category);
        if(res != 1) {
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        //删除redis
        redis.del(REDIS_ALL_CATEGORY);
    }


    @Override
    public List<Category> queryCategoryList() {
        return categoryMapper.selectAll();
    }
}

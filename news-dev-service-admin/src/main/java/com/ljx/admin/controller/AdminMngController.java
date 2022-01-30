package com.ljx.admin.controller;

import com.ljx.admin.service.AdminUserService;
import com.ljx.api.BaseController;
import com.ljx.api.controller.admin.AdminMngControllerApi;
import com.ljx.exception.GraceException;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.AdminUser;
import com.ljx.pojo.bo.AdminLoginBO;
import com.ljx.pojo.bo.NewAdminBO;
import com.ljx.utils.PagedGridResult;
import com.ljx.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
public class AdminMngController extends BaseController implements AdminMngControllerApi {

    final static Logger logger = LoggerFactory.getLogger(AdminMngController.class);
    @Autowired
    private RedisOperator redis;
    @Autowired
    private AdminUserService adminUserService;

    @Override
    public GraceJSONResult adminLogin(AdminLoginBO adminLoginBO,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        //1.获取admin的信息
        AdminUser admin = adminUserService.queryAdminByUsername(adminLoginBO.getUsername());

        //2.判断admin不为空，为空失败
        if(admin == null){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
        //校验密码
        boolean isCorrect = BCrypt.checkpw(adminLoginBO.getPassword(),admin.getPassword());
        if(isCorrect) {
            doLoginSettings(admin,request,response);
            return GraceJSONResult.ok();
        }else{
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
    }
    //设置登录成功后,基本信息设置
    private void doLoginSettings(AdminUser admin,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        //保存token到redis中
        String token = UUID.randomUUID().toString();
        redis.set(REDIS_ADMIN_TOKEN+":"+admin.getId(),token);
        //保存信息到cookie:id和uuid生成到token
        setCookie(request,response,"atoken",token,COOKIE_MONTH);
        setCookie(request,response,"aid",admin.getId(),COOKIE_MONTH);
        setCookie(request,response,"aname",admin.getAdminName(),COOKIE_MONTH);
    }

    @Override
    public GraceJSONResult adminIsExist(String username) {
        checkAdminExist(username);
        return GraceJSONResult.ok();
    }
    private void  checkAdminExist(String username) {
        //1.获取admin的信息
        AdminUser admin = adminUserService.queryAdminByUsername(username);

        if(admin != null) {
            GraceException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }

    }

    @Override
    public GraceJSONResult addNewAdmin(NewAdminBO newAdminBO,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        //1.base64不为空，则人脸入库，否则使用密码账号登录
        if(StringUtils.isBlank(newAdminBO.getImg64())) {
            if(StringUtils.isBlank(newAdminBO.getPassword())||
                    StringUtils.isBlank(newAdminBO.getConfirmPassword())){
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
            }

        }
        //2.password和confirm pwd需要一致
        if(StringUtils.isNotBlank(newAdminBO.getPassword())) {
            if(!newAdminBO.getPassword().equalsIgnoreCase(newAdminBO.getConfirmPassword())){
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }
        //3.校验用户名不为空
        checkAdminExist(newAdminBO.getUsername());
        //4.保存admin信息
        adminUserService.createAdminUser(newAdminBO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getAdminList(Integer page, Integer pageSize) {
        if(page == null) {
            page = COMMON_START_PAGE;
        }
        if(pageSize ==null){
            pageSize = COMMON_PAGESIZE;
        }
        PagedGridResult pagedGridResult = adminUserService.queryAdminList(page,pageSize);
        return GraceJSONResult.ok(pagedGridResult);
    }

    @Override
    public GraceJSONResult adminLogout(String adminId,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        //删除redis会话信息
        redis.del(REDIS_ADMIN_TOKEN+":"+adminId);
        //清除浏览器cookie信息
        deleteCookie(request,response,"atoken");
        deleteCookie(request,response,"aid");
        deleteCookie(request,response,"aname");
        return GraceJSONResult.ok();
    }
}

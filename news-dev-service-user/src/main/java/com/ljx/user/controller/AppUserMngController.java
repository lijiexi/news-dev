package com.ljx.user.controller;

import com.ljx.api.BaseController;
import com.ljx.api.controller.user.AppUserMngControllerApi;
import com.ljx.api.controller.user.HelloControllerApi;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.user.service.AppUserMngService;
import com.ljx.utils.PagedGridResult;
import com.ljx.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 实现admin端，用户相关管理
 */
@RestController
public class AppUserMngController extends BaseController implements AppUserMngControllerApi {

    final static Logger logger = LoggerFactory.getLogger(AppUserMngController.class);
    @Autowired
    private AppUserMngService appUserMngService;
    @Override
    public GraceJSONResult queryAll(String nickname,
                                    Integer status,
                                    Date startDate,
                                    Date endDate,
                                    Integer page,
                                    Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGESIZE;
        }
        PagedGridResult res = appUserMngService.queryAllUserList(nickname,status,startDate,endDate,page,pageSize);
        return GraceJSONResult.ok(res);
    }
}

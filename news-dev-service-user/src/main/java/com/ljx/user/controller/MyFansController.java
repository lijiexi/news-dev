package com.ljx.user.controller;

import com.ljx.api.BaseController;
import com.ljx.api.controller.user.HelloControllerApi;
import com.ljx.api.controller.user.MyFansControllerApi;
import com.ljx.enums.Sex;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.pojo.vo.FansCountsVO;
import com.ljx.pojo.vo.RegionRatioVO;
import com.ljx.user.service.MyFansService;
import com.ljx.utils.JsonUtils;
import com.ljx.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MyFansController extends BaseController implements MyFansControllerApi {

    final static Logger logger = LoggerFactory.getLogger(MyFansController.class);
    @Autowired
    private MyFansService myFansService;

    @Override
    public GraceJSONResult forceUpdateFanInfo(String relationId, String fanId) {
        myFansService.forceUpdateFanInfo(relationId,fanId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult isMeFollowThisWriter(String writerId, String fanId) {
        boolean res = myFansService.isMeFollowThisWriter(writerId,fanId);
        return GraceJSONResult.ok(res);
    }

    @Override
    public GraceJSONResult follow(String writerId, String fanId) {
        myFansService.follow(writerId,fanId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult unfollow(String writerId, String fanId) {
        myFansService.unfollow(writerId,fanId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryAll(String writerId,
                                    Integer page,
                                    Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGESIZE;
        }
        //通过mysql查询用户的粉丝
        return GraceJSONResult.ok(myFansService.queryMyFansList(writerId,page,pageSize));
        //通过ES查询用户的粉丝
        //return GraceJSONResult.ok(myFansService.queryMyFansESList(writerId,page,pageSize));
    }

    @Override
    public GraceJSONResult queryRatio(String writerId) {
        //从mysql中查询男女粉丝数量
        int man = myFansService.queryFansCounts(writerId, Sex.man);
        int women = myFansService.queryFansCounts(writerId,Sex.woman);
        FansCountsVO fansCountsVO = new FansCountsVO();
        fansCountsVO.setManCounts(man);
        fansCountsVO.setWomanCounts(women);
        //从ES中查询男女粉丝数量
        //FansCountsVO fansCountsVO = myFansService.queryFansESCounts(writerId);

        return GraceJSONResult.ok(fansCountsVO);
    }

    @Override
    public GraceJSONResult queryRatioByRegion(String writerId) {
        //使用mysql查询粉丝地域分布
        List<RegionRatioVO> list = myFansService.queryRegionRatioCounts(writerId);
        //使用es查询粉丝地域分布
        //List<RegionRatioVO> list = myFansService.queryRegionRatioESCounts(writerId);
        return GraceJSONResult.ok(list);
    }
}

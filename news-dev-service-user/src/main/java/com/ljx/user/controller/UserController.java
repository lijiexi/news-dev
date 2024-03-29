package com.ljx.user.controller;

import com.ljx.api.BaseController;
import com.ljx.api.controller.user.HelloControllerApi;
import com.ljx.api.controller.user.UserControllerApi;
import com.ljx.grace.result.GraceJSONResult;
import com.ljx.grace.result.ResponseStatusEnum;
import com.ljx.pojo.AppUser;
import com.ljx.pojo.bo.UpdateUserInfoBO;
import com.ljx.pojo.vo.AppUserVO;
import com.ljx.pojo.vo.UserAccountInfoVO;
import com.ljx.user.service.UserService;
import com.ljx.utils.JsonUtils;
import com.ljx.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class UserController extends BaseController implements UserControllerApi {

    final static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @Override
    public GraceJSONResult getAccountInfo(String userId) {
        //1.判断参数不为空
        if(StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        //2.根据userId查询用户信息
        AppUser user = getUser(userId);
        //3.返回用户信息
        UserAccountInfoVO accountInfoVO = new UserAccountInfoVO();
        //将属性信息考到vo
        BeanUtils.copyProperties(user,accountInfoVO);
        return GraceJSONResult.ok(accountInfoVO);
    }
    private AppUser getUser(String userId) {
        String userJson = redis.get(REDIS_USER_INFO+":"+userId);
        AppUser user = null;
        //查询判断redis是否包含用户信息，包含则直接返回
        if(StringUtils.isNotBlank(userJson)) {
            user = JsonUtils.jsonToPojo(userJson,AppUser.class);
        }else{
            //redis不存在用户信息，查询数据库
            user = userService.getUser(userId);
            //用户信息不怎么变动，这类信息可以放入redis
            //将第一次查询后的数据放入redis
            redis.set(REDIS_USER_INFO+":"+userId, JsonUtils.objectToJson(user));
        }
        return user;
    }

    @Override
    public GraceJSONResult updateUserInfo(@Valid UpdateUserInfoBO updateUserInfoBO,
                                          BindingResult result) {
        // 0.判断BindingResult中是否保存了错误的验证信息，如果有，则需要返回
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return GraceJSONResult.errorMap(map);
        }
        //1.执行更新操作
        userService.updateUserInfo(updateUserInfoBO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getUserInfo(String userId) {
        //1.判断参数不为空
        if(StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        //2.根据userId查询用户信息
        AppUser user = getUser(userId);
        //3.返回用户信息
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user,userVO);
        //4.查询Redis中用户关注数和粉丝数到，放入VO中，传给前端
        userVO.setMyFansCounts(getCountsFromRedis(REDIS_WRITER_FANS_COUNTS+":"+userId));
        userVO.setMyFollowCounts(getCountsFromRedis(REDIS_MY_FOLLOW_COUNTS+":"+userId));

        return GraceJSONResult.ok(userVO);
    }

    //远程调用
    @Override
    public GraceJSONResult queryByIds(String userIds) {
        if (StringUtils.isBlank(userIds)) {
            //为空
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }
        List<AppUserVO> publishList = new ArrayList<>();
        //将String转换成用户id的list
        List<String> userIdList = JsonUtils.jsonToList(userIds,String.class);
        for (String userId : userIdList) {
            //获得基本信息
            AppUserVO appUserVO = getBasicUserInfo(userId);
            publishList.add(appUserVO);
        }

        return GraceJSONResult.ok(publishList);
    }

    /**
     *
     * @param userId 根据id在redis、数据库中查user信息
     * @return 返回UserVo
     */
    private AppUserVO getBasicUserInfo (String userId) {
        //1.根据userId查询用户信息
        AppUser user = getUser(userId);
        //2.返回用户信息
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }

}

package com.ljx.user.controller;


import com.ljx.api.BaseController;
import com.ljx.api.controller.user.PassportControllerApi;

import com.ljx.grace.result.GraceJSONResult;
import com.ljx.utils.IPUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PassportController extends BaseController implements PassportControllerApi {

    final static Logger logger = LoggerFactory.getLogger(PassportController.class);



    @Override
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request) {

        // 获得用户ip
        String userIp = IPUtil.getRequestIp(request);

        // 根据用户的ip进行限制，限制用户在60秒内只能获得一次验证码
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);

        // 生成随机验证码并且发送短信
        String random = (int)((Math.random() * 9 + 1) * 100000) + "";
        //smsUtils.sendSMS(mobile, random);

        // 把验证码存入redis，用于后续进行验证
        redis.set(MOBILE_SMSCODE + ":" + mobile, random, 30 * 60);
        logger.info("验证码： "+random);

        return GraceJSONResult.ok();
    }

//    @Override
//    public GraceJSONResult doLogin(@Valid RegistLoginBO registLoginBO,
//                                   BindingResult result,
//                                   HttpServletRequest request,
//                                   HttpServletResponse response) {
//
//        // 0.判断BindingResult中是否保存了错误的验证信息，如果有，则需要返回
//        if (result.hasErrors()) {
//            Map<String, String> map = getErrors(result);
//            return GraceJSONResult.errorMap(map);
//        }
//
//        String mobile = registLoginBO.getMobile();
//        String smsCode = registLoginBO.getSmsCode();
//
//        // 1. 校验验证码是否匹配
//        String redisSMSCode = redis.get(MOBILE_SMSCODE + ":" + mobile);
//        if (StringUtils.isBlank(redisSMSCode) || !redisSMSCode.equalsIgnoreCase(smsCode)) {
//            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
//        }
//
//        // 2. 查询数据库，判断该用户注册
//        AppUser user = userService.queryMobileIsExist(mobile);
//        if (user != null && user.getActiveStatus() == UserStatus.FROZEN.type) {
//            // 如果用户不为空，并且状态为冻结，则直接抛出异常，禁止登录
//            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_FROZEN);
//        } else if (user == null) {
//            // 如果用户没有注册过，则为null，需要注册信息入库
//            user = userService.createUser(mobile);
//        }
//
//        // 3. 保存用户分布式会话的相关操作
//        int userActiveStatus = user.getActiveStatus();
//        if (userActiveStatus != UserStatus.FROZEN.type) {
//            // 保存token到redis
//            String uToken = UUID.randomUUID().toString();
//            redis.set(REDIS_USER_TOKEN + ":" + user.getId(), uToken);
//            redis.set(REDIS_USER_INFO + ":" + user.getId(), JsonUtils.objectToJson(user));
//
//            // 保存用户id和token到cookie中
//            setCookie(request, response, "utoken", uToken, COOKIE_MONTH);
//            setCookie(request, response, "uid", user.getId(), COOKIE_MONTH);
//        }
//
//        // 4. 用户登录或注册成功以后，需要删除redis中的短信验证码，验证码只能使用一次，用过后则作废
//        redis.del(MOBILE_SMSCODE + ":" + mobile);
//
//        // 5. 返回用户状态
//        return GraceJSONResult.ok(userActiveStatus);
//    }
//
//    @Override
//    public GraceJSONResult logout(String userId,
//                                  HttpServletRequest request,
//                                  HttpServletResponse response) {
//
//        redis.del(REDIS_USER_TOKEN + ":" + userId);
//
//        setCookie(request, response, "utoken", "", COOKIE_DELETE);
//        setCookie(request, response, "uid", "", COOKIE_DELETE);
//
//        return GraceJSONResult.ok();
//    }
}

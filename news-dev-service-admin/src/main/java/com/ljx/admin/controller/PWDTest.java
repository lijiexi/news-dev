package com.ljx.admin.controller;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * 对明文密码进行加密，存入数据库
 */
public class PWDTest {
    //加密
    public static void main(String[] args) {
        //放入密码并且加盐
        String res = BCrypt.hashpw("admin",BCrypt.gensalt());
    }
}

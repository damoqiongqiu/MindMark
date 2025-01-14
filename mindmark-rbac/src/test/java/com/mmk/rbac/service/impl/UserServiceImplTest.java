package com.mmk.rbac.service.impl;


import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;

class UserServiceImplTest {

    @Test
    void encryptPassword() {
//        String username :mindmark , String password :mindmark , String salt:123456
        String enPwd = new Md5Hash("mindmark" + "mindmark" + "123456").toHex().toString();
        //3a2cf37b023dbc611fcd9f456ac2158d
        System.out.println(enPwd);
    }
}
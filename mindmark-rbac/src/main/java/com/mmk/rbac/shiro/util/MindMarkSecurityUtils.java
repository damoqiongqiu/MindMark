package com.mmk.rbac.shiro.util;

import com.mmk.rbac.jpa.entity.UserEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

/**
 * 静态工具类，扩展 Shiro 内置的 SecurityUtils 工具类，加上 MindMark 需要的一些工具方法，这些方法用来简化上层的业务代码。
 * @author 大漠穷秋
 */
public class MindMarkSecurityUtils extends SecurityUtils{
    public static UserEntity getUserEntity() {
        UserEntity userEntity = null;
        Object obj = SecurityUtils.getSubject().getPrincipal();
        if (!ObjectUtils.isEmpty(obj)) {
            userEntity = new UserEntity();
            BeanUtils.copyProperties(obj, userEntity);
        }
        return userEntity;
    }

    public static void setUserEntity(UserEntity userEntity) {
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(userEntity, realmName);
        subject.runAs(newPrincipalCollection);
    }

    public static Session getSession(){
        return SecurityUtils.getSubject().getSession();
    }

    public static Integer getUserId() {
        return getUserEntity().getUserId();
    }

    public static String getUserName() {
        return getUserEntity().getUserName();
    }

    public static String getIp() {
        return SecurityUtils.getSubject().getSession().getHost();
    }

    public static String getSessionId() {
        return String.valueOf(SecurityUtils.getSubject().getSession().getId());
    }

    public static String randomSalt() {
        return new SecureRandomNumberGenerator().nextBytes(3).toHex();
    }
}
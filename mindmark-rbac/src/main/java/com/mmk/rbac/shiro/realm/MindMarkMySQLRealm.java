package com.mmk.rbac.shiro.realm;

import com.mmk.rbac.jpa.entity.UserEntity;
import com.mmk.rbac.service.IUserService;
import com.mmk.rbac.shiro.util.MindMarkSecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

/**
 * MindMark 操作 MySQL 的 Realm 。
 * @author 大漠穷秋
 */
public class MindMarkMySQLRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(MindMarkMySQLRealm.class);

    @Autowired
    private IUserService userService;

    /**
     * 认证
     * TODO:这里仅实现了简单的用户名+密码的验证方式，需要扩展其它认证方式，例如：扫描二维码、第三方认证。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = usernamePasswordToken.getUsername();
        String password = usernamePasswordToken.getPassword()!=null?new String(usernamePasswordToken.getPassword()):"";

        UserEntity userEntity;
        try {
            userEntity = userService.checkUser(username, password);
            logger.debug("UserName>"+username);
            logger.debug("Password>"+password);
        }catch (Exception e) {
            logger.error(username + "登录失败{}", e.getMessage());
            throw new AuthenticationException(e.getMessage(), e);
        }

        //用户认证成功，返回验证信息实例。
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userEntity, password, getName());
        return info;
    }

    /**
     * 授权
     * MindMark 采用 Shiro 字符串形式的权限定义，权限不实现成 Java 类。
     * Shiro 权限字符串的匹配模式定义参考 https://shiro.apache.org/java-authorization-guide.html
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Integer userId= MindMarkSecurityUtils.getUserId();

        //TODO:首先尝试从 Session 中获取角色和权限数据，加快授权操作的速度。
        //同时需要自己扩展 SessionListener 来同步 Session 数据。

        Set<String> permStrs=this.userService.getPermStringsByUserId(userId);
        logger.debug(permStrs.toString());

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permStrs);
        return info;
    }
}

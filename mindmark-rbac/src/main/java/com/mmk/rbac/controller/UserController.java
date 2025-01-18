package com.mmk.rbac.controller;

import com.mmk.rbac.i18n.I18nUtil;
import com.mmk.rbac.jpa.entity.UserEntity;
import com.mmk.rbac.service.IUserService;
import com.mmk.rbac.shiro.util.MindMarkSecurityUtils;
import com.mmk.rbac.util.AjaxResult;
import com.mmk.rbac.validation.ValidUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 * @author 大漠穷秋
 */
@RestController
@RequestMapping("/mind-mark/auth/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    protected IUserService userService;

    /**
     * 获取Session中的用户实例。
     * 在前后端分离的模式下，前端可以通过此接口来测试Session中是否还存在已登录的用户。
     * @return
     */
    @GetMapping("/get-session-user")
    @ResponseBody
    public UserEntity getSessionUser(){
        return MindMarkSecurityUtils.getUserEntity();
    }

    @PostMapping("/update")
    @ResponseBody
    public AjaxResult updateUser(@ValidUser @RequestBody UserEntity userEntity) {
        logger.debug(userEntity.toString());
        
        userEntity.setSalt(MindMarkSecurityUtils.randomSalt());
        userEntity.setPassword(userService.encryptPassword(userEntity.getUserName(), userEntity.getPassword(), userEntity.getSalt()));

        return AjaxResult.success(userService.saveUser(userEntity));
    }

    @PostMapping(value = "/update-user-role-relation")
    @ResponseBody
    @RequiresPermissions("sys:manage:user")
    public AjaxResult updateUserRoleRelation(@RequestBody UserEntity userEntity){
        userEntity=this.userService.updateUserRoleRelation(userEntity);
        return AjaxResult.success(userEntity);
    }

    @PostMapping(value = "/list/{page}")
    @ResponseBody
    @RequiresPermissions("sys:manage:user")
    public Page<UserEntity> getUserList(@RequestBody UserEntity userEntity, @PathVariable(value="page",required = true) int page) {
        Pageable pageable= PageRequest.of(page-1,10);
        Page<UserEntity> userList = userService.getUserList(userEntity,pageable);
        return userList;
    }

    @DeleteMapping(value = "/delete/{userId}")
    @ResponseBody
    @RequiresPermissions("sys:manage:user")
    public AjaxResult deleteUser(@PathVariable(value="userId",required = true)Integer userId){
        //TODO:合法性校验，关联表操作校验，业务逻辑校验
        int affected=userService.deleteByUserId(userId);
        if(affected==0){
            return new AjaxResult(false, I18nUtil.getMessage("user.del.failure"));
        }else{
            return new AjaxResult(true,"common.del.success");
        }
    }

    @GetMapping(value = "/detail/{userId}")
    @ResponseBody
    public AjaxResult getUserDetail(@PathVariable(value = "userId",required = true) Integer userId){
        UserEntity userEntity=userService.getUserByUserId(userId);
        AjaxResult ajaxResult =new AjaxResult(true,userEntity);
        return ajaxResult;
    }

    //TODO:重新定义权限，管理员可以重置任意密码，用户自己只能重置自己的密码
    @PostMapping("/reset-pwd")
    @ResponseBody
    public AjaxResult resetPwd(UserEntity userEntity) {
        userEntity.setSalt(MindMarkSecurityUtils.randomSalt());
        userEntity.setPassword(userService.encryptPassword(userEntity.getUserName(), userEntity.getPassword(), userEntity.getSalt()));
        userService.resetPwd(userEntity);
        return AjaxResult.failure();
    }

    @PostMapping("/remove")
    @ResponseBody
    @RequiresPermissions("sys:manage:user")
    public AjaxResult delUser(Integer[] ids) {
        try {
            return AjaxResult.success(userService.deleteByIds(ids));
        } catch (Exception e) {
            return AjaxResult.failure(e.getMessage());
        }
    }

    @PostMapping("/change-status")
    @ResponseBody
    @RequiresPermissions("sys:manage:user")
    public AjaxResult changeStatus(UserEntity userEntity) {
        return AjaxResult.success(userService.setUserStatus(userEntity));
    }

    @PostMapping("/is-user-name-unique")
    @ResponseBody
    public String isUserNameUnique(UserEntity userEntity) {
        return userService.isUserNameUnique(userEntity.getUserName())?"0":"1";
    }

    @PostMapping("/is-phone-unique")
    @ResponseBody
    public String isPhoneUnique(UserEntity userEntity) {
        return userService.isPhoneUnique(userEntity.getCellphone())?"0":"1";
    }

    @PostMapping("/is-email-unique")
    @ResponseBody
    public String isEmailUnique(UserEntity userEntity) {
        return userService.isEmailUnique(userEntity.getEmail())?"0":"1";
    }
}
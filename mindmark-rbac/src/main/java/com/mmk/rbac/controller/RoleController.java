package com.mmk.rbac.controller;

import com.mmk.rbac.i18n.I18nUtil;
import com.mmk.rbac.util.AjaxResult;
import com.mmk.rbac.jpa.entity.ApiPermissionEntity;
import com.mmk.rbac.jpa.entity.ComponentPermissionEntity;
import com.mmk.rbac.jpa.entity.RoleEntity;
import com.mmk.rbac.jpa.entity.UserEntity;
import com.mmk.rbac.service.IApiPermissionService;
import com.mmk.rbac.service.IComponentPermissionService;
import com.mmk.rbac.service.IRoleService;
import com.mmk.rbac.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理
 * @author 大漠穷秋
 */
@RestController
@RequestMapping("/mind-mark/auth/role")
@RequiresPermissions("sys:manage:role")
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    protected IRoleService roleService;

    @Autowired
    protected IUserService userService;

    @Autowired
    protected IApiPermissionService apiPermissionService;

    @Autowired
    protected IComponentPermissionService componentService;

    @PostMapping(value = "/list/{page}")
    @ResponseBody
    public Page<RoleEntity> getRoleList(@RequestBody RoleEntity roleEntity, @PathVariable(value="page",required = true) int page) {
        Pageable pageable= PageRequest.of(page-1,10);
        Page<RoleEntity> roleList = roleService.getRoleListPaging(roleEntity,pageable);
        return roleList;
    }

    /**
     * 服务端 API 角色控制
     * @param apiPermissionId
     * @return
     */
    @GetMapping(value = "/list-by-api-id/{apiPermissionId}")
    @ResponseBody
    public AjaxResult findRoleListByApiId(@PathVariable(value="apiPermissionId",required = true) int apiPermissionId){
        ApiPermissionEntity apiPermissionEntity =apiPermissionService.getApiPermissionById(apiPermissionId);
        List<RoleEntity> roleEntities= apiPermissionEntity.getRoleEntities();
        return AjaxResult.success(roleEntities);
    }

    /**
     * 前端页面组件角色控制
     * @param compPermId
     * @return
     */
    @GetMapping(value = "/list-by-component-id/{compPermId}")
    @ResponseBody
    public AjaxResult findRoleListByComponentId(@PathVariable(value="compPermId",required = true) int compPermId){
        ComponentPermissionEntity componentPermissionEntity =componentService.getComponentPermissionDetail(compPermId);
        List<RoleEntity> roleEntities= componentPermissionEntity.getRoleEntities();
        return AjaxResult.success(roleEntities);
    }

    @GetMapping(value = "/list-by-user-id/{userId}")
    @ResponseBody
    public AjaxResult findRoleListByUserId(@PathVariable(value="userId",required = true) int userId){
        UserEntity userEntity=userService.getUserByUserId(userId);
        List<RoleEntity> roleEntities= userEntity.getRoleEntities();
        return AjaxResult.success(roleEntities);
    }

    @PostMapping(value = "/create")
    @ResponseBody
    public AjaxResult createRole(@RequestBody RoleEntity roleEntity){
        roleEntity=roleService.createRole(roleEntity);
        return AjaxResult.success(roleEntity);
    }

    @PostMapping(value = "/update")
    @ResponseBody
    public AjaxResult updateRole(@RequestBody RoleEntity roleEntity){
        roleEntity=roleService.updateRole(roleEntity);
        return AjaxResult.success(roleEntity);
    }

    @DeleteMapping(value = "/delete/{roleId}")
    @ResponseBody
    public AjaxResult deleteRole(@PathVariable(value="roleId",required = true)Integer roleId){
        int affected= roleService.deleteRole(roleId);
        if(affected==0){
            return AjaxResult.failure(I18nUtil.getMessage("role.del.failure"));
        }else{
            return AjaxResult.success("common.del.success");
        }
    }
    
    @GetMapping(value = "/detail/{roleId}")
    @ResponseBody
    public RoleEntity getRoleDetail(@PathVariable(value = "roleId",required = true) Integer roleId){
        return roleService.getRoleById(roleId);
    }

    @PostMapping(value = "/add-auth-users/{roleId}/{userIds}")
    @ResponseBody
    public AjaxResult addAuthUsers(
            @PathVariable(value = "roleId",required = true) Integer roleId,
            @PathVariable(value = "userIds",required = true) Integer[] userIds
    ){
        int result=roleService.addAuthUsers(roleId,userIds);
        if(result==0){
            return AjaxResult.success();
        }else {
            return AjaxResult.failure();
        }
    }

    @PostMapping(value = "/del-auth-users/{roleId}/{userIds}")
    @ResponseBody
    public AjaxResult delAuthUsers(
            @PathVariable(value = "roleId",required = true) Integer roleId,
            @PathVariable(value = "userIds",required = true) Integer[] userIds
    ){
        int result=roleService.deleteAuthUsers(roleId,userIds);
        if(result==0){
            return AjaxResult.success();
        }else {
            return AjaxResult.failure();
        }
    }
}

package com.mmk.rbac.controller;

import com.mmk.core.utils.AjaxResult;
import com.mmk.rbac.jpa.entity.ComponentPermissionEntity;
import com.mmk.rbac.jpa.entity.RoleEntity;
import com.mmk.rbac.service.IComponentPermissionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

/**
 * 前端页面组件权限管理
 * @author 大漠穷秋
 */
@RestController
@RequestMapping("/mind-mark/auth/component-permission")
@RequiresPermissions("sys:manage:component-permission")
public class ComponentPermissionController {
    private static final Logger logger = LoggerFactory.getLogger(ComponentPermissionController.class);

    @Autowired
    private IComponentPermissionService componentService;

    @PostMapping(value = "/list/{page}")
    @ResponseBody
    public Page<ComponentPermissionEntity> getPermissionList(@RequestBody ComponentPermissionEntity componentPermissionEntity, @PathVariable(value="page",required = true) int page) {
        Pageable pageable= PageRequest.of(page-1,10,Sort.by(Sort.Direction.ASC,"displayOrder"));
        Page<ComponentPermissionEntity> compoPermList = componentService.getComponentPermissionTree(componentPermissionEntity,pageable);
        logger.debug(compoPermList.toString());
        return compoPermList;
    }

    @PostMapping(value = "/list-all-by-role")
    @ResponseBody
    public Iterable<ComponentPermissionEntity> getPermissionListByRole(@RequestBody RoleEntity roleEntity) {
        return this.componentService.getPermListAllByRole(roleEntity);
    }

    @GetMapping(value = "/detail/{compPermId}")
    @ResponseBody
    public ComponentPermissionEntity getCompPermDetail(@PathVariable(value = "compPermId",required = true) Integer compPermId){
        return this.componentService.getComponentPermissionDetail(compPermId);
    }

    @DeleteMapping(value = "/delete/{compPermId}")
    @ResponseBody
    public AjaxResult deleteComponentPermission(@PathVariable(value="compPermId",required = true)Integer compPermId){
        int result=this.componentService.deleteComponentPermission(compPermId);
        if(result==0){
            return AjaxResult.success();
        }else{
            return AjaxResult.failure();
        }
    }

    @PostMapping(value = "/create")
    @ResponseBody
    public AjaxResult createComponentPermission(@RequestBody ComponentPermissionEntity componentPermission){
        componentPermission=this.componentService.createComponentPermission(componentPermission);
        return AjaxResult.success(componentPermission);
    }

    @PostMapping(value = "/update")
    @ResponseBody
    public AjaxResult updateComponentPermission(@RequestBody ComponentPermissionEntity componentPermission){
        componentPermission=this.componentService.updateComponentPermission(componentPermission);
        return AjaxResult.success(componentPermission);
    }
}

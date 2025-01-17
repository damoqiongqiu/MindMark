package com.mmk.rbac.controller;

import com.mmk.rbac.util.AjaxResult;
import com.mmk.rbac.jpa.entity.ApiPermissionEntity;
import com.mmk.rbac.jpa.entity.RoleEntity;
import com.mmk.rbac.service.IApiPermissionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 服务端 API 权限管理
 * @author 大漠穷秋
 */
@RestController
@RequestMapping("/mind-mark/auth/api-permission")
@RequiresPermissions("sys:manage:api-permission")
public class ApiPermissionController {
    private static final Logger logger = LoggerFactory.getLogger(ApiPermissionController.class);

    @Autowired
    protected IApiPermissionService apiPermService;

    @PostMapping(value = "/list/{page}")
    @ResponseBody
    public Page<ApiPermissionEntity> getPermissionList(@RequestBody ApiPermissionEntity apiPermissionEntity, @PathVariable(value="page",required = true) int page) {
        Pageable pageable= PageRequest.of(page-1,10);
        Page<ApiPermissionEntity> permList = apiPermService.getPermListPaging(apiPermissionEntity,pageable);
        return permList;
    }

    @PostMapping(value = "/list-all")
    @ResponseBody
    public Iterable<ApiPermissionEntity> getPermissionListAll(@RequestBody ApiPermissionEntity apiPermissionEntity) {
        return apiPermService.getPermListAll(apiPermissionEntity);
    }

    @PostMapping(value = "/list-all-by-role")
    @ResponseBody
    public Iterable<ApiPermissionEntity> getPermissionListByRole(@RequestBody RoleEntity roleEntity) {
        return apiPermService.getPermListAllByRole(roleEntity);
    }

    @GetMapping(value = "/detail/{apiPermissionId}")
    @ResponseBody
    public AjaxResult getApiPermissionDetail(@PathVariable(value = "apiPermissionId",required = true) Integer apiPermissionId){
        return AjaxResult.success(this.apiPermService.getApiPermissionById(apiPermissionId));
    }

    @PostMapping(value = "/create")
    @ResponseBody
    public AjaxResult createApiPermission(@RequestBody ApiPermissionEntity apiPermissionEntity){
        apiPermissionEntity=this.apiPermService.createApiPermission(apiPermissionEntity);
        return AjaxResult.success(apiPermissionEntity);
    }

    @PostMapping(value = "/update")
    @ResponseBody
    public AjaxResult updatePermission(@RequestBody ApiPermissionEntity apiPermissionEntity){
        apiPermissionEntity=this.apiPermService.updatePermission(apiPermissionEntity);
        return AjaxResult.success(apiPermissionEntity);
    }

    @DeleteMapping(value = "/delete/{apiPermissionId}")
    @ResponseBody
    public AjaxResult deleteByApiId(@PathVariable(value="apiPermissionId",required = true)Integer apiPermissionId){
        //TODO:数据校验，数据关联性测试
        this.apiPermService.deleteByApiId(apiPermissionId);
        return AjaxResult.success();
    }
}

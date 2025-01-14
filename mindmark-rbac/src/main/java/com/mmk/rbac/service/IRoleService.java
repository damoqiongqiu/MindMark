package com.mmk.rbac.service;

import com.mmk.rbac.jpa.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 大漠穷秋
 */
public interface IRoleService {
    RoleEntity createRole(RoleEntity roleEntity);

    int deleteRole(Integer roleId);

    RoleEntity updateRole(RoleEntity newRoleEntity);

    Page<RoleEntity> getRoleListPaging(RoleEntity roleEntity, Pageable pageable);

    RoleEntity getRoleById(Integer roleId);

    int deleteAuthUsers(Integer roleId, Integer[] userIds);

    int addAuthUsers(Integer roleId, Integer[] userIds);
}

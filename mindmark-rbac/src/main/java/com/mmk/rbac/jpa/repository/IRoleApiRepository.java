package com.mmk.rbac.jpa.repository;

import com.mmk.rbac.jpa.entity.RoleApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 大漠穷秋
 */
public interface IRoleApiRepository extends PagingAndSortingRepository<RoleApiEntity, Integer>, JpaSpecificationExecutor, JpaRepository<RoleApiEntity, Integer> {
    @Transactional
    int deleteByRoleIdAndApiPermissionId(Integer roleId,Integer apiPermissionId);

    @Transactional
    int deleteByRoleIdAndApiPermissionIdIsIn(Integer roleId,Integer[] apiIds);

    @Transactional
    int deleteAllByRoleId(Integer roleId);

    int deleteByApiPermissionId(Integer apiPermissionId);

    Iterable<RoleApiEntity> findAllByRoleId(Integer roleId);//TODO:分页？

    Iterable<RoleApiEntity> findAllByApiPermissionId(Integer apiPermissionId);//TODO:分页？
}

package com.mmk.rbac.jpa.repository;

import com.mmk.rbac.jpa.entity.RoleEntity;
import com.mmk.rbac.jpa.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 大漠穷秋
 */
public interface IRoleRepository extends PagingAndSortingRepository<RoleEntity, Integer>, JpaSpecificationExecutor, JpaRepository<RoleEntity, Integer> {
    Iterable<RoleEntity> findAllByRoleId(Integer roleId);

    Page<UserEntity> findUserEntitiesByRoleId(Integer roleId, Pageable pageable);

    RoleEntity findDistinctByRoleName(String roleName);

    RoleEntity findDistinctByRoleId(Integer roleId);

    @Transactional
    void deleteAllByRoleIdIn(Integer[] roleIds);

    @Transactional
    int deleteByRoleId(Integer roleId);
}

package com.mmk.rbac.jpa.repository;

import com.mmk.rbac.jpa.entity.RoleComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 大漠穷秋
 */
public interface IRoleComponentRepository extends PagingAndSortingRepository<RoleComponentEntity, Integer>, JpaSpecificationExecutor, JpaRepository<RoleComponentEntity, Integer> {
    int deleteByRoleIdAndCompPermId(Integer roleId,Integer compPermId);
    int deleteByRoleIdAndCompPermIdIsIn(Integer roleId,Integer[] componentIds);

    @Transactional
    int deleteAllByRoleId(Integer roleId);
}

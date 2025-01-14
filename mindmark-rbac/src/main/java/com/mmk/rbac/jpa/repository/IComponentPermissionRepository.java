package com.mmk.rbac.jpa.repository;

import com.mmk.rbac.jpa.entity.ComponentPermissionEntity;
import com.mmk.rbac.jpa.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author 大漠穷秋
 */
public interface IComponentPermissionRepository extends PagingAndSortingRepository<ComponentPermissionEntity, Integer>, JpaSpecificationExecutor, JpaRepository<ComponentPermissionEntity, Integer> {
    ComponentPermissionEntity findDistinctByCompPermId(Integer compPermId);

    Iterable<ComponentPermissionEntity> findAllByRoleEntitiesIn(List<RoleEntity> roleEntityList);

    Iterable<ComponentPermissionEntity> findDistinctByRoleEntitiesIn(List<RoleEntity> roleEntityList);
}

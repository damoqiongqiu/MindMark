package com.mmk.rbac.jpa.repository;

import com.mmk.rbac.jpa.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 大漠穷秋
 */
public interface IUserRoleRepository extends PagingAndSortingRepository<UserRoleEntity, Integer>, JpaSpecificationExecutor, JpaRepository<UserRoleEntity, Integer> {
    @Transactional
    void deleteByRoleIdAndUserIdIsIn(Integer roleId,Integer[] userIds);

    /**
     * 根据 roleId 删除 user-role 之间的关联关系
     * @param roleId
     * @return
     */
    @Transactional
    void deleteAllByRoleId(Integer roleId);

    /**
     * 根据 userId 删除 user-role 之间的关联关系
     * @param userId
     * @return
     */
    @Transactional
    int deleteByUserId(Integer userId);
}

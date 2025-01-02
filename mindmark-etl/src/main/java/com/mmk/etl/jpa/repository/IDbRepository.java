package com.mmk.etl.jpa.repository;

import com.mmk.etl.jpa.entity.DbEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据库配置仓库，用于对 mind_mark_db_for_process 表进行操作。
 * @author 大漠穷秋
 */

@Repository
public interface IDbRepository extends JpaRepository<DbEntity, Integer> {
    /**
     * 根据用户 ID 查找所有数据库配置。
     * @param userId 用户 ID
     * @return 数据库配置列表
     */
    List<DbEntity> findByUserId(Integer userId);

    /**
     * 根据用户 ID 分页查找数据库配置。
     * @param userId 用户 ID
     * @param pageable 分页参数
     * @return 分页后的数据库配置列表
     */
    Page<DbEntity> findByUserId(Integer userId, Pageable pageable);
}

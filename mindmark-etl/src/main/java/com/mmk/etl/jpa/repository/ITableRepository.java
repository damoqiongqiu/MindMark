package com.mmk.etl.jpa.repository;

import com.mmk.etl.jpa.entity.TableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据表配置仓库，用于对 mind_mark_table_for_process 表进行操作。
 * 提供对表配置的基础操作以及常用查询方法。
 * @author 大漠穷秋
 */

@Repository
public interface ITableRepository extends JpaRepository<TableEntity, Integer> {
    /**
     * 根据 Schema ID 查找所有需要处理的表。
     * @param schemaId Schema ID
     * @return 表配置列表
     */
    List<TableEntity> findBySchemaId(Integer schemaId);

    /**
     * 根据 Schema ID 和表名称查找特定的表配置。
     * @param schemaId Schema ID
     * @param tableName 表名称
     * @return 表配置
     */
    TableEntity findBySchemaIdAndTableName(Integer schemaId, String tableName);

    Page<TableEntity> findBySchemaId(Integer schemaId, Pageable pageable);
}

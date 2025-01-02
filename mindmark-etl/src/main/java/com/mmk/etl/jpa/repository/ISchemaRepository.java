package com.mmk.etl.jpa.repository;

import com.mmk.etl.jpa.entity.SchemaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据库 Schema 配置仓库，用于对 mind_mark_schema_for_process 表进行操作。
 * 提供对 Schema 配置的基础操作以及常用查询方法。
 * @author 大漠穷秋
 */

@Repository
public interface ISchemaRepository extends JpaRepository<SchemaEntity, Integer> {
    /**
     * 根据数据库 ID 查找所有 Schema。
     * @param dbId 数据库 ID
     * @return Schema 配置列表
     */
    List<SchemaEntity> findByDbId(Integer dbId);

    /**
     * 根据数据库 ID 和 Schema 名称查找特定的 Schema 配置。
     * @param dbId 数据库 ID
     * @param schemaName Schema 名称
     * @return Schema 配置
     */
    SchemaEntity findByDbIdAndSchemaName(Integer dbId, String schemaName);

    Page<SchemaEntity> findByDbId(Integer dbId, Pageable pageable);
}

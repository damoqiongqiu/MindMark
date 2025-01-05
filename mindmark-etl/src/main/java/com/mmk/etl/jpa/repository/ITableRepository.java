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

    List<TableEntity> findByDbId(Integer dbId);

    Page<TableEntity> findByDbId(Integer dbId, Pageable pageable);
}

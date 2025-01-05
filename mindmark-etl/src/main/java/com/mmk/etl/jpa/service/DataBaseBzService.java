package com.mmk.etl.jpa.service;

import com.mmk.etl.jpa.entity.*;
import com.mmk.etl.jpa.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 对数据库中的数据进行嵌入操作相关的服务
 * @author 大漠穷秋
 */
@Slf4j
@Service
@AllArgsConstructor
public class DataBaseBzService {

    private IUserRepository userRepository;
    private IDbRepository dbRepository;
    private ITableRepository tableRepository;
    private IEmbeddingLogRepository embeddingLogRepository;

    /**
     * 按照每页的条数获取用户总页数
     * @param pageSize 每页条数
     * @return 总页数
     */
    public Integer getAllUserPageCount(int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("每页条数必须大于0");
        }
        long totalUsers = userRepository.count();
        return (int) Math.ceil((double) totalUsers / pageSize);
    }

    /**
     * 分页查询所有用户
     * @param page 当前页码（从0开始）
     * @param size 每页记录数
     * @return 包含用户信息的分页对象
     */
    public Page<UserEntity> getAllUserPageable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }

    /**
     * 获取给定用户配置的所有 dbEntity ，不分页
     * @param userId 用户 ID
     * @return 数据库配置列表
     */
    public List<DbEntity> getDbEntityListByUserId(Integer userId) {
        return this.dbRepository.findByUserId(userId);
    }

    /**
     * 获取指定 dbId 中的所有 tableEntityList ，不分页
     * @param schemaId schema ID
     * @return table 配置列表
     */
    public List<TableEntity> getTableEntityListByDbId(Integer schemaId) {
        return this.tableRepository.findByDbId(schemaId);
    }

    /**
     * 获取数据库向量化处理记录
     * @param dbId 数据库 ID
     * @param tableId 表 ID
     * @return 向量化处理记录
     */
    public EmbeddingLogEntity getDbEmbeddingLog(Integer dbId, Integer tableId) {
        return this.embeddingLogRepository.findByDbIdAndTableId(dbId, tableId);
    }

    /**
     * 保存数据库向量化操作的记录
     * @param dbId 数据库 ID
     * @param tableId 表 ID
     * @param startId 起始 ID
     * @return 向量化处理记录
     */
    public EmbeddingLogEntity saveDbEmbeddingLog(Integer dbId, Integer tableId, Integer startId) {
        EmbeddingLogEntity embeddingLogEntity = this.embeddingLogRepository.findByDbIdAndTableId(dbId, tableId);
        if (embeddingLogEntity == null) {
            embeddingLogEntity = new EmbeddingLogEntity();
            log.info("No existing EmbeddingLogEntity found, creating a new one.");
        }
        embeddingLogEntity.setDbId(dbId);
        embeddingLogEntity.setTableId(tableId);
        embeddingLogEntity.setStartId(startId);
        return this.embeddingLogRepository.save(embeddingLogEntity);
    }

    /**
     * 分页查询指定 dbId 中的 table 配置，带分页
     * @param dbId DB Id
     * @param page 当前页码
     * @param size 每页记录数
     * @return 包含 table 配置的分页对象
     */
    public Page<TableEntity> getTableEntityListByDbIdPageable(Integer dbId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return tableRepository.findByDbId(dbId, pageable);
    }

    /**
     * 分页查询给定用户配置的所有 dbEntity ，带分页
     * @param userId 用户 ID
     * @param page 当前页码
     * @param size 每页记录数
     * @return 包含数据库配置的分页对象
     */
    public Page<DbEntity> getDbEntityListByUserIdPageable(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return dbRepository.findByUserId(userId, pageable);
    }

    /**
     * 根据 ID 删除数据库配置
     * @param id 数据库配置 ID
     */
    public void deleteDatabase(Integer id) {
        dbRepository.deleteById(id);
    }

    /**
     * 更新数据库配置
     * @param dbEntity 数据库配置对象
     * @return 更新后的数据库配置对象
     */
    public DbEntity saveDatabase(DbEntity dbEntity) {
        return dbRepository.save(dbEntity);
    }

    /**
     * 根据用户 ID 删除数据库配置
     * @param userId 用户 ID
     */
    public void deleteDatabaseByUserId(Integer userId) {
        List<DbEntity> databases = dbRepository.findByUserId(userId);
        dbRepository.deleteAll(databases);
    }

    /**
     * 根据ID获取数据库配置详情
     * @param id 数据库配置ID
     * @return 数据库配置详情
     */
    public DbEntity getDatabaseById(Integer id) {
        return dbRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("未找到ID为 " + id + " 的数据库配置"));
    }

    /**
     * 根据ID获取表配置详情
     * @param id 表配置ID
     * @return 表配置详情
     */
    public TableEntity getTableById(Integer id) {
        return tableRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("未找到ID为 " + id + " 的表配置"));
    }

    /**
     * 保存表配置
     * @param tableEntity 表配置对象
     * @return 保存后的表配置对象
     */
    public TableEntity saveTable(TableEntity tableEntity) {
        return tableRepository.save(tableEntity);
    }

    /**
     * 根据ID删除表配置
     * @param id 表配置ID
     */
    public void deleteTable(Integer id) {
        tableRepository.deleteById(id);
    }

}

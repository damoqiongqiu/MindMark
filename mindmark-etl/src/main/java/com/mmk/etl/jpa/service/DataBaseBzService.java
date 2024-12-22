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

    private ISchemaRepository schemaRepository;

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
     *
     * @param page 当前页码（从0开始）
     * @param size 每页记录数
     * @return 包含用户信息的分页对象
     */
    public Page<UserEntity> getAllUserPageable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }
    
    /**
     * 获取给定用户配置的所有 dbEntity
     * @param userId
     * @return
     */
    public List<DbEntity> getDbEntityListByUserId(Integer userId){
        return this.dbRepository.findByUserId(userId);
    }

    /**
     * 获取指定 dbId 中的所有 schemaEntityList
     * @param dbId
     * @return
     */
    public List<SchemaEntity> getSchemaEntityListDbId(Integer dbId){
        return this.schemaRepository.findByDbId(dbId);
    }

    /**
     * 获取指定 schemaId 中的所有 tableEntityList
     * @param schemaId
     * @return
     */
    public List<TableEntity> getTableEntityListDbId(Integer schemaId){
        return this.tableRepository.findBySchemaId(schemaId);
    }

    /**
     * 获取数据库向量化处理记录
     * @param dbId
     * @param tableId
     * @return
     */
    public EmbeddingLogEntity getDbEmbeddingLog(Integer dbId, Integer tableId){
        return this.embeddingLogRepository.findByDbIdAndTableId(dbId, tableId);
    }

    /**
     * 保存数据库向量化操作的记录
     * @param dbId
     * @param tableId
     * @param startId
     * @return
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
}

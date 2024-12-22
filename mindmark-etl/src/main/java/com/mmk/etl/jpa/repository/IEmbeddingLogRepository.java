package com.mmk.etl.jpa.repository;

import com.mmk.etl.jpa.entity.EmbeddingLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 向量化记录仓库接口
 * 提供对 mind_mark_embedding_log 表的操作
 * @author 大漠穷秋
 */
@Repository
public interface IEmbeddingLogRepository extends JpaRepository<EmbeddingLogEntity, Integer> {
    /**
     * 根据 dbId 和 tableId 查找数据库向量化记录
     * @param dbId 数据库 ID
     * @param tableId 表 ID
     * @return 向量化记录
     */
    EmbeddingLogEntity findByDbIdAndTableId(Integer dbId, Integer tableId);

    /**
     * 根据 fileId 查找文件向量化记录
     * @param fileId
     * @return
     */
    EmbeddingLogEntity findByFileId(Integer fileId);
}

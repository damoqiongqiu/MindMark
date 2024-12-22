package com.mmk.etl.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

/**
 * 向量化记录
 * @author 大漠穷秋
 */

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@Table(name = "mind_mark_embedding_log")
public class EmbeddingLogEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;
    
    @Column(name = "db_id")
    private Integer dbId;

    @Column(name = "table_id")
    private Integer tableId;

    @Column(name = "start_id")
    private Integer startId;

    @Column(name = "file_id")
    private Integer fileId;

    @Column(name = "index_id")
    private Integer indexId;
}
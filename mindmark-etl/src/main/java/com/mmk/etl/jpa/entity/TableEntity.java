package com.mmk.etl.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

/**
 * 维护需要进行向量化的表，用户可以在 UI 界面上进行配置。
 * @author 大漠穷秋
 */

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@Table(name = "mind_mark_table_for_process")
public class TableEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "db_id")
    private Integer dbId;

    @Column(name = "schema_name", length = 128)
    private String schemaName;

    @Column(name = "table_name", length = 128)
    private String tableName;

    @Column(name = "id_column", length = 128)
    private String idColumn;
}
package com.mmk.etl.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

/**
 * 维护用户在 ElasticSearch 中创建的索引
 * @author 大漠穷秋
 */

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@Table(name = "mind_mark_user_index")
public class UserIndexEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "index_name", length = 256)
    private String indexName;

    @Column(name = "remark", length = 1024)
    private String remark;
}
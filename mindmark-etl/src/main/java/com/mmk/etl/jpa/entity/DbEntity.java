package com.mmk.etl.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

/**
 * 需要进行处理的数据库，让用户自己配置。
 * @author 大漠穷秋
 */

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@Table(name = "mind_mark_db_for_process")
public class DbEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "db_type", length = 64)
    private String dbType;

    @Column(name = "ip", length = 64)
    private String ip;

    @Column(name = "port")
    private Integer port;

    @Column(name = "charset", length = 64)
    private String charset;

    @Column(name = "user_name", length = 64)
    private String userName;

    @Column(name = "password", length = 256)
    private String password;

    @Column(name = "user_id")
    private Integer userId;
}
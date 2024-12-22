package com.mmk.etl.jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 大漠穷秋
 */

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@Table(name = "mind_mark_rbac_user")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Integer userId;

    @Column(name="user_name",nullable = false,updatable = false)
    private String userName;

    @Column(name="nick_name",nullable = false)
    private String nickName;

    @Column(name="password",nullable = false)
    private String password;

    @Column(name="email")
    private String email;

    @Column(name="cellphone")
    private String cellphone;

    @Column(name="gender",columnDefinition = "int default 0")
    private Integer gender=0;

    @Column(name="city")
    private String city;

    @Column(name="education")
    private String education;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_time",updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name="avatar_url")
    private String avatarURL;

    @Column(name="salt")
    private String salt;

    @Column(name="status",columnDefinition = "int default 0")
    private Integer status=0;

    @Column(name="remark")
    private String remark;
}
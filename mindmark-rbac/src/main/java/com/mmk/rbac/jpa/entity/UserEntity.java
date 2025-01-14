package com.mmk.rbac.jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "RbacUserEntity")
@DynamicInsert
@DynamicUpdate
@Table(name = "mindmark_rbac_user")
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", updatable = false)
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

    @JoinTable(
        name="mindmark_rbac_user_role",
        joinColumns={@JoinColumn(name="user_id",referencedColumnName="user_id")},
        inverseJoinColumns={@JoinColumn(name="role_id",referencedColumnName="role_id")}
    )
    @ManyToMany(fetch = FetchType.LAZY)
    private List<RoleEntity> roleEntities;

    public List<RoleEntity> getRoleEntities() {
        return roleEntities;
    }

    @JsonProperty
    public void setRoleEntities(List<RoleEntity> roleEntities) {
        this.roleEntities = roleEntities;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }
}
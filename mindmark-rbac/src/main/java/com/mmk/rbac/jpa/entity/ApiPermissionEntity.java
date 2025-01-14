package com.mmk.rbac.jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mmk.rbac.jpautils.RoleListSerializer;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 在 MindMark 中，权限控制整体上分2个层次：前端权限和后端权限。
 * - 前端权限是指在前端页面上的权限控制，比如菜单、按钮、甚至可以细致到一个 HTML 元素。
 * - 后端权限是指在后端 API 接口上的权限控制，比如一个 API 接口是否需要登录才能访问，是否需要某个角色才能访问，是否需要某个权限才能访问等。
 * 
 * ApiPermissionEntity 用来定义服务端 API 接口的权限，对应数据库中的 mindmark_rbac_api 表。
 * 需要被保护的服务端 API 接口使用此 Entity 进行权限定义，不需要保护的接口由业务代码自行处理。
 * 
 * @see ComponentPermissionEntity
 * @author 大漠穷秋
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "mindmark_rbac_api")
public class ApiPermissionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="api_id")
    private Integer apiPermissionId;

    @Column(name="api_name")
    private String apiName;
    
    @Column(name="url")
    private String url;

    @Column(name="permission")
    private String permission;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_time",updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime=new Date();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="update_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime=new Date();

    @Column(name="remark")
    private String remark;

    @JoinTable(
        name="mindmark_rbac_role_api",
        joinColumns={@JoinColumn(name="api_id",referencedColumnName="api_id")},
        inverseJoinColumns={@JoinColumn(name="role_id",referencedColumnName="role_id")}
    )
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonSerialize(using = RoleListSerializer.class)
    private List<RoleEntity> roleEntities;

    public List<RoleEntity> getRoleEntities() {
        return roleEntities;
    }

    public void setRoleEntities(List<RoleEntity> roleEntities) {
        this.roleEntities = roleEntities;
    }

    public Integer getApiPermissionId() {
        return apiPermissionId;
    }

    public void setApiPermissionId(Integer apiPermissionId) {
        this.apiPermissionId = apiPermissionId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

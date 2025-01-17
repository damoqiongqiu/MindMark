package com.mmk.rbac.jpa.entity;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "mind_mark_rbac_role")
public class RoleEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="role_id")
    private Integer roleId;
    
    @Column(name="role_name")
    private String roleName;
    
    @Column(name="status",columnDefinition = "int default 0",nullable = false)
    private Integer status;
    
    @Column(name="remark")
    private String remark;
    
    @ManyToMany(mappedBy = "roleEntities",fetch = FetchType.LAZY)
    private List<UserEntity> userEntities;

    @ManyToMany(mappedBy = "roleEntities",fetch = FetchType.LAZY)
    private List<ApiPermissionEntity> apiEntities;

    @ManyToMany(mappedBy = "roleEntities",fetch = FetchType.LAZY)
    private List<ComponentPermissionEntity> componentEntities;

    //每个角色都可能被大量用户关联，因此 JSON 序列化时忽略 userEntities
    //TODO:用户数量很大时查询会有问题？？？需要用测试数据 check
    @JsonIgnore
    public List<UserEntity> getUserEntities() {
        return userEntities;
    }

    public void setUserEntities(List<UserEntity> userEntities) {
        this.userEntities = userEntities;
    }

    public List<ApiPermissionEntity> getApiEntities() {
        return apiEntities;
    }

    public void setApiEntities(List<ApiPermissionEntity> apiEntities) {
        this.apiEntities = apiEntities;
    }

    public List<ComponentPermissionEntity> getComponentEntities() {
        return componentEntities;
    }

    public void setComponentEntities(List<ComponentPermissionEntity> componentEntities) {
        this.componentEntities = componentEntities;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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

}
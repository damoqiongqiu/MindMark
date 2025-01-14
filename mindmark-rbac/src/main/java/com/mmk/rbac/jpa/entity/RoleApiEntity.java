package com.mmk.rbac.jpa.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@DynamicInsert
@DynamicUpdate
@IdClass(RoleApiEntity.class)
@Table(name = "mindmark_rbac_role_api")
public class RoleApiEntity implements Serializable {
    @Id
    @Column(name="role_id")
    private Integer roleId;

    @Id
    @Column(name="api_id")
    private Integer apiPermissionId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getApiPermissionId() {
        return apiPermissionId;
    }

    public void setApiPermissionId(Integer apiPermissionId) {
        this.apiPermissionId = apiPermissionId;
    }
}

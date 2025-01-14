package com.mmk.rbac.jpa.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@DynamicInsert
@DynamicUpdate
@IdClass(RoleComponentEntity.class)
@Table(name = "mindmark_rbac_role_component")
public class RoleComponentEntity implements Serializable {
    @Id
    @Column(name="role_id")
    private Integer roleId;

    @Id
    @Column(name="component_id")
    private Integer compPermId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getComponentId() {
        return compPermId;
    }

    public void setComponentId(Integer compPermId) {
        this.compPermId = compPermId;
    }
}

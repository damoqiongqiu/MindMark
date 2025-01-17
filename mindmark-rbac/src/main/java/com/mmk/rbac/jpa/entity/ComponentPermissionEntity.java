package com.mmk.rbac.jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mmk.rbac.jpautils.RoleListSerializer;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 在 MindMark 中，权限控制整体上分2个层次：前端权限和后端权限。
 * - 前端权限是指在前端页面上的权限控制，比如菜单、按钮、甚至可以细致到一个 HTML 元素。
 * - 后端权限是指在后端 API 接口上的权限控制，比如一个 API 接口是否需要登录才能访问，是否需要某个角色才能访问，是否需要某个权限才能访问等。
 * 
 * ComponentPermissionEntity 用来定义前端页面的权限，对应数据库中的 mind_mark_rbac_component 表。
 * 需要被保护的前端组件使用此 Entity 进行权限定义，不需要保护的组件由前端自行管理。
 * 
 * @see ApiPermissionEntity
 * @author: 大漠穷秋
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "mind_mark_rbac_component")
public class ComponentPermissionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="component_id")
    private Integer compPermId;

    @Column(name="component_name")
    private String componentName;

    @Column(name="icon")
    private String icon;

    @Column(name="url")
    private String url;

    @Column(name="display_order ")
    private Integer displayOrder;

    @Column(name="permission")
    private String permission;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_time",updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="update_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Column(name="visiable")
    private Integer visiable=1;

    @Column(name="remark")
    private String remark;

    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="compPermId")
    @ManyToOne
    @JoinColumn(name="p_id")
    private ComponentPermissionEntity parentEntity;

    @OneToMany(cascade = {CascadeType.REMOVE,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name="p_id")
    @OrderBy("displayOrder asc ")
    private List<ComponentPermissionEntity> children=new ArrayList<>();

    @JoinTable(
            name="mind_mark_rbac_role_component",
            joinColumns={@JoinColumn(name="component_id",referencedColumnName="component_id")},
            inverseJoinColumns={@JoinColumn(name="role_id",referencedColumnName="role_id")}
    )
    @ManyToMany
    @JsonSerialize(using = RoleListSerializer.class)
    private List<RoleEntity> roleEntities;

    public List<RoleEntity> getRoleEntities() {
        return roleEntities;
    }

    public void setRoleEntities(List<RoleEntity> roleEntities) {
        this.roleEntities = roleEntities;
    }

    public Integer getCompPermId() {
        return compPermId;
    }

    public void setCompPermId(Integer compPermId) {
        this.compPermId = compPermId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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

    public Integer getVisiable() {
        return visiable;
    }

    public void setVisiable(Integer visiable) {
        this.visiable = visiable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ComponentPermissionEntity> getChildren() {
        return children;
    }

    public void setChildren(List<ComponentPermissionEntity> children) {
        this.children = children;
    }

    public ComponentPermissionEntity getParentEntity() {
        return parentEntity;
    }

    public void setParentEntity(ComponentPermissionEntity parentEntity) {
        this.parentEntity = parentEntity;
    }
}
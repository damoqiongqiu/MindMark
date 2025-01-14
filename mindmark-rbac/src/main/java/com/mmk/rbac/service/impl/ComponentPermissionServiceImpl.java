package com.mmk.rbac.service.impl;


import com.mmk.rbac.jpa.entity.ComponentPermissionEntity;
import com.mmk.rbac.jpa.entity.RoleEntity;
import com.mmk.rbac.jpa.entity.UserEntity;
import com.mmk.rbac.jpa.repository.IComponentPermissionRepository;
import com.mmk.rbac.jpa.repository.IUserRepository;
import com.mmk.rbac.service.IComponentPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 大漠穷秋
 */
@Service
public class ComponentPermissionServiceImpl implements IComponentPermissionService {
    private static final Logger logger = LoggerFactory.getLogger(ComponentPermissionServiceImpl.class);

    @Autowired
    private IComponentPermissionRepository componentPermissionRepository;

    @Autowired
    private IUserRepository userRepository;

    /**
     * 此方法从根节点开始，包含所有层级上的子节点，带分页
     */
    @Override
    public Page<ComponentPermissionEntity> getComponentPermissionTree(ComponentPermissionEntity componentPermissionEntity,Pageable pageable) {
        Page<ComponentPermissionEntity> page= this.componentPermissionRepository.findAll(new Specification<ComponentPermissionEntity>() {
            @Override
            public Predicate toPredicate(Root<ComponentPermissionEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates=new ArrayList<>();
                predicates.add(criteriaBuilder.isNull(root.get("parentEntity")));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        },pageable);
        return page;
    }

    @Override
    public Iterable<ComponentPermissionEntity> getPermListAllByRole(RoleEntity roleEntity) {
        List<RoleEntity> list=new ArrayList<>();
        list.add(roleEntity);
        Iterable<ComponentPermissionEntity> compPermList=this.componentPermissionRepository.findAllByRoleEntitiesIn(list);
        return compPermList;
    }

    @Override
    public ComponentPermissionEntity getComponentPermissionDetail(Integer compPermId) {
        return this.componentPermissionRepository.findDistinctByCompPermId(compPermId);
    }

    /**
     * 创建组件权限，创建时可以带有父层节点的 id 作为参数，但是不处理子节点，即使传递 children 参数，也会被忽略。
     * @param componentPermissionEntity
     * @return
     */
    @Override
    public ComponentPermissionEntity createComponentPermission(ComponentPermissionEntity componentPermissionEntity) {
        if(!ObjectUtils.isEmpty(componentPermissionEntity.getParentEntity())){
            Integer pId=componentPermissionEntity.getParentEntity().getCompPermId();
            ComponentPermissionEntity parentEntity=this.componentPermissionRepository.findDistinctByCompPermId(pId);
            componentPermissionEntity.setParentEntity(parentEntity);
        }
        componentPermissionEntity.setChildren(new ArrayList<>());
        componentPermissionEntity=this.componentPermissionRepository.save(componentPermissionEntity);
        return componentPermissionEntity;
    }

    /**
     * 编辑组件权限，编辑时只修改当前组件节点上的属性，父层和子层都不修改。
     * 即使接收到了 parentEntity 和 children 参数 ，也全部忽略，仍然使用原来的值。
     * @param componentPermissionEntity
     * @return
     */
    @Override
    public ComponentPermissionEntity updateComponentPermission(ComponentPermissionEntity componentPermissionEntity) {
        //TODO:数据校验
        ComponentPermissionEntity oldEntity=this.componentPermissionRepository.findDistinctByCompPermId(componentPermissionEntity.getCompPermId());
        if(!ObjectUtils.isEmpty(oldEntity)){
            componentPermissionEntity.setParentEntity(oldEntity.getParentEntity());
            componentPermissionEntity.setChildren(oldEntity.getChildren());
            componentPermissionEntity.setRoleEntities(oldEntity.getRoleEntities());
        }
        this.componentPermissionRepository.save(componentPermissionEntity);
        return componentPermissionEntity;
    }

    @Override
    public int deleteComponentPermission(Integer compPermId) {
        this.componentPermissionRepository.deleteById(compPermId);
        return 0;
    }

    @Override
    public Iterable<ComponentPermissionEntity> getComponentPermissionsByUserId(Integer userId) {
        UserEntity userEntity=this.userRepository.findDistinctByUserId(userId);
        List<RoleEntity> roleEntities=userEntity.getRoleEntities();
        Iterable<ComponentPermissionEntity> componentPermissionEntities=this.componentPermissionRepository.findDistinctByRoleEntitiesIn(roleEntities);
        return componentPermissionEntities;
    }
}

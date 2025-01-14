package com.mmk.rbac.service.impl;

import com.mmk.rbac.jpa.entity.*;
import com.mmk.rbac.jpa.repository.*;
import com.mmk.rbac.jpa.entity.*;
import com.mmk.rbac.jpa.repository.*;
import com.mmk.rbac.service.IRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
public class RoleServiceImpl implements IRoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IUserRoleRepository userRoleRepository;

    @Autowired
    private IRoleComponentRepository roleComponentRepository;

    @Autowired
    private IRoleApiRepository roleApiRepository;

    @Autowired
    private IApiPermissionRepository apiPermissionRepository;

    @Autowired
    private IComponentPermissionRepository componentPermissionRepository;

    private void saveRoleApiPermissions(RoleEntity newRoleEntity){
        List<RoleApiEntity> newRoleApiPermList=new ArrayList<>();
        List<ApiPermissionEntity> newPermApiEntities=newRoleEntity.getApiEntities();
        newPermApiEntities.forEach(entity->{
            RoleApiEntity newRoleApiEntity=new RoleApiEntity();
            newRoleApiEntity.setRoleId(newRoleEntity.getRoleId());
            newRoleApiEntity.setApiPermissionId(entity.getApiPermissionId());
            newRoleApiPermList.add(newRoleApiEntity);
        });
        logger.debug(newRoleApiPermList.toString());
        this.roleApiRepository.saveAll(newRoleApiPermList);
    }

    private void saveRoleComponentPermissions(RoleEntity newRoleEntity){
        List<RoleComponentEntity> newRoleCompPermList=new ArrayList<>();
        List<ComponentPermissionEntity> componentEntities=newRoleEntity.getComponentEntities();
        componentEntities.forEach(entity->{
            RoleComponentEntity newRoleCompEntity=new RoleComponentEntity();
            newRoleCompEntity.setRoleId(newRoleEntity.getRoleId());
            newRoleCompEntity.setComponentId(entity.getCompPermId());
            newRoleCompPermList.add(newRoleCompEntity);
        });
        logger.debug(newRoleCompPermList.toString());
        this.roleComponentRepository.saveAll(newRoleCompPermList);
    }

    @Override
    @Transactional
    public RoleEntity createRole(RoleEntity newRoleEntity) {
        //TODO:数据校验
        this.saveRoleApiPermissions(newRoleEntity);
        this.saveRoleComponentPermissions(newRoleEntity);
        newRoleEntity=this.roleRepository.save(newRoleEntity);
        return newRoleEntity;
    }

    /**
     * 创建或更新角色。
     * TODO:自动级联更新关联表？
     * @param newRoleEntity
     * @return
     */
    @Override
    @Transactional
    public RoleEntity updateRole(RoleEntity newRoleEntity) {
        //TODO:数据校验

        RoleEntity oldEntity=this.roleRepository.findDistinctByRoleId(newRoleEntity.getRoleId());

        //处理服务端 API 权限关联关系，先删掉现有的 API 权限关联关系
        this.roleApiRepository.deleteAllByRoleId(newRoleEntity.getRoleId());
        this.saveRoleApiPermissions(newRoleEntity);

        //处理前端组件权限关联关系，先删掉现有的组件权限关联关系
        this.roleComponentRepository.deleteAllByRoleId(newRoleEntity.getRoleId());
        this.saveRoleComponentPermissions(newRoleEntity);

        //TODO:处理用户角色关联关系
        return this.roleRepository.save(newRoleEntity);
    }

    /**
     * 删除角色，同时删除此角色上的关联关系。
     *
     * 角色 status 定义：
     * - -1 系统内置角色，不能删除
     * - 0正常
     * - 1停用
     * - 2删除
     * status =-1 的角色为系统内置角色，删除和更新操作都无效，数据库触发器自动保护。
     *
     * TODO:自动级联更新？
     * @param roleId
     * @return
     */
    @Override
    @Transactional
    public int deleteRole(Integer roleId) {
        this.roleApiRepository.deleteAllByRoleId(roleId);
        this.roleComponentRepository.deleteAllByRoleId(roleId);
        this.userRoleRepository.deleteAllByRoleId(roleId);
        return this.roleRepository.deleteByRoleId(roleId);
    }

    @Override
    public Page<RoleEntity> getRoleListPaging(RoleEntity roleEntity, Pageable pageable) {
        return this.roleRepository.findAll(new Specification<RoleEntity>() {
            @Override
            public Predicate toPredicate(Root<RoleEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates=new ArrayList<>();
                if(!StringUtils.isEmpty(roleEntity.getRoleName())){
                    predicates.add(criteriaBuilder.like(root.get("roleName"),"%"+roleEntity.getRoleName()+"%"));
                }
                if(!StringUtils.isEmpty(roleEntity.getStatus())){
                    predicates.add(criteriaBuilder.equal(root.get("status"),roleEntity.getStatus()));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        },pageable);
    }

    @Override
    public RoleEntity getRoleById(Integer roleId) {
        return this.roleRepository.findDistinctByRoleId(roleId);
    }

    @Override
    @Transactional
    public int deleteAuthUsers(Integer roleId, Integer[] userIds) {
        this.userRoleRepository.deleteByRoleIdAndUserIdIsIn(roleId,userIds);
        return 0;
    }

    @Override
    @Transactional
    public int addAuthUsers(Integer roleId, Integer[] userIds) {
        List<UserRoleEntity> list=new ArrayList<>();
        for(int i=0;i<userIds.length;i++){
            UserRoleEntity userRoleEntity=new UserRoleEntity();
            userRoleEntity.setRoleId(roleId);
            userRoleEntity.setUserId(userIds[i]);
            list.add(userRoleEntity);
        }
        this.userRoleRepository.saveAll(list);
        return 0;
    }
}

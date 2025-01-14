package com.mmk.rbac.service.impl;


import com.mmk.rbac.jpa.entity.ApiPermissionEntity;
import com.mmk.rbac.jpa.entity.RoleEntity;
import com.mmk.rbac.jpa.repository.IApiPermissionRepository;
import com.mmk.rbac.service.IApiPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
public class ApiPermissionServiceImpl implements IApiPermissionService {
    private static final Logger logger = LoggerFactory.getLogger(ApiPermissionServiceImpl.class);

    @Autowired
    private IApiPermissionRepository apiPermRepository;

    @Override
    public Page<ApiPermissionEntity> getPermListPaging(ApiPermissionEntity permEntity, Pageable pageable) {
        return this.apiPermRepository.findAll(new Specification<ApiPermissionEntity>() {
            @Override
            public Predicate toPredicate(Root<ApiPermissionEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates=new ArrayList<>();
                if(!StringUtils.isEmpty(permEntity.getApiName())){
                    predicates.add(criteriaBuilder.like(root.get("apiName"),"%"+permEntity.getApiName()+"%"));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        },pageable);
    }

    @Override
    public Iterable<ApiPermissionEntity> getPermListAll(ApiPermissionEntity apiPermissionEntity) {
        return this.apiPermRepository.findAll();
    }

    @Override
    public Iterable<ApiPermissionEntity> getPermListAllByRole(RoleEntity roleEntity) {
        List<RoleEntity> list=new ArrayList<>();
        list.add(roleEntity);
        Iterable<ApiPermissionEntity> apiPermissionEntities=this.apiPermRepository.findAllByRoleEntitiesIn(list);
        return apiPermissionEntities;
    }

    @Override
    public ApiPermissionEntity createApiPermission(ApiPermissionEntity permEntity) {
        return this.apiPermRepository.save(permEntity);
    }

    @Override
    public ApiPermissionEntity updatePermission(ApiPermissionEntity permEntity) {
        //TODO:数据校验
        ApiPermissionEntity permEntityDB=this.apiPermRepository.findDistinctByApiPermissionId(permEntity.getApiPermissionId());
        BeanUtils.copyProperties(permEntity,permEntityDB);
        this.apiPermRepository.save(permEntity);
        return permEntity;
    }

    @Override
    public ApiPermissionEntity getApiPermissionById(Integer apiPermissionId) {
        return this.apiPermRepository.findDistinctByApiPermissionId(apiPermissionId);
    }

    @Override
    @Transactional
    public int deleteByApiId(Integer apiPermissionId) {
        return this.apiPermRepository.deleteByApiPermissionId(apiPermissionId);
    }
}

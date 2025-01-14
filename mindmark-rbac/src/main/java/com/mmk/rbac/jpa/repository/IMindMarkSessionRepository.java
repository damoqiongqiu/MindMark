package com.mmk.rbac.jpa.repository;

import com.mmk.rbac.jpa.entity.MindMarkSessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 大漠穷秋
 */
public interface IMindMarkSessionRepository extends PagingAndSortingRepository<MindMarkSessionEntity, Integer>, JpaSpecificationExecutor, JpaRepository<MindMarkSessionEntity, Integer> {
    MindMarkSessionEntity findDistinctBySessionId(String sessionId);
    
    Page<MindMarkSessionEntity> findNiceFishSessionEntitiesByUserId(Integer userId, Pageable pageable);

    @Transactional
    int deleteDistinctBySessionId(String sessionId);
}

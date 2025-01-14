package com.mmk.rbac.service.impl;

import com.mmk.rbac.jpa.entity.MindMarkSessionEntity;
import com.mmk.rbac.jpa.repository.IMindMarkSessionRepository;
import com.mmk.rbac.service.IMindMarkSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
public class MindMarkSessionServiceImpl implements IMindMarkSessionService {
    private static final Logger logger = LoggerFactory.getLogger(MindMarkSessionServiceImpl.class);

    @Autowired
    private IMindMarkSessionRepository sessionRepository;

    @Override
    public MindMarkSessionEntity findDistinctBySessionId(String sessionId) {
        return this.sessionRepository.findDistinctBySessionId(sessionId);
    }

    @Override
    public Page<MindMarkSessionEntity> findNiceFishSessionEntitiesByUserId(Integer userId, Pageable pageable) {
        return this.sessionRepository.findAll(new Specification<MindMarkSessionEntity>() {
            @Override
            public Predicate toPredicate(Root<MindMarkSessionEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates=new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("userId"),userId));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        },pageable);
    }

    @Override
    public int deleteDistinctBySessionId(String sessionId) {
        return this.sessionRepository.deleteDistinctBySessionId(sessionId);
    }

    @Override
    public MindMarkSessionEntity saveSession(MindMarkSessionEntity session) {
        return this.sessionRepository.save(session);
    }
}

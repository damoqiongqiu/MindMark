package com.mmk.rbac.service;

import com.mmk.rbac.jpa.entity.MindMarkSessionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMindMarkSessionService {
    MindMarkSessionEntity findDistinctBySessionId(String sessionId);

    Page<MindMarkSessionEntity> findNiceFishSessionEntitiesByUserId(Integer userId, Pageable pageable);

    int deleteDistinctBySessionId(String sessionId);

    MindMarkSessionEntity saveSession(MindMarkSessionEntity session);
}

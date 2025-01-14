package com.mmk.rbac.jpa.repository;

import com.mmk.rbac.jpa.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @see <a href="https://docs.spring.io/spring-data/jpa/docs/2.1.10.RELEASE/reference/html/">JPA DOC</a>
 * @author 大漠穷秋
 */
@Repository("rbacUserRepository")
public interface IUserRepository extends PagingAndSortingRepository<UserEntity, Integer>, JpaSpecificationExecutor, JpaRepository<UserEntity, Integer> {

    UserEntity findDistinctByUserId(Integer userId);

    UserEntity findDistinctByUserName(String userName);

    UserEntity findDistinctByEmail(String email);

    UserEntity findDistinctTopByCellphone(String cellphone);

    UserEntity findDistinctByUserNameOrEmailOrCellphone(String userName,String email,String cellphone);

    Page<UserEntity> findAllByNickName(String nickName,Pageable pageable);

    Page<UserEntity> findAllByUserNameOrNickNameOrEmailOrCellphone(String userName,String nickName,String email,String cellphone, Pageable pageable);

    Page<UserEntity> findAll(Pageable pageable);

    @Transactional
    int deleteByUserId(Integer userId);

    @Transactional
    int deleteByUserIdIn(Integer[] userIds);

    @Transactional
    int deleteAllByUserId(Integer[] ids);
}
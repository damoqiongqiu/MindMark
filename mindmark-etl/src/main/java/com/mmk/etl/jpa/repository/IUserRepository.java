package com.mmk.etl.jpa.repository;


import com.mmk.etl.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 大漠穷秋
 */
@Repository
public interface IUserRepository extends PagingAndSortingRepository<UserEntity, Integer>, JpaSpecificationExecutor, JpaRepository<UserEntity, Integer> {
}
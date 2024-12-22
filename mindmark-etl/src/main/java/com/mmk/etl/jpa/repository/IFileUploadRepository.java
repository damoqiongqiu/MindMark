package com.mmk.etl.jpa.repository;


import com.mmk.etl.jpa.entity.FileUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 大漠穷秋
 */

@Repository
public interface IFileUploadRepository extends PagingAndSortingRepository<FileUploadEntity, Integer>, JpaRepository<FileUploadEntity, Integer> {
    FileUploadEntity findDistinctById(Integer id);

    FileUploadEntity findDistinctByFileName(String fileName);
}

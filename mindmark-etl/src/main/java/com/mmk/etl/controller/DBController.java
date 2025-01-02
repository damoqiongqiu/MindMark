package com.mmk.etl.controller;

import com.mmk.etl.jpa.entity.DbEntity;
import com.mmk.etl.jpa.entity.SchemaEntity;
import com.mmk.etl.jpa.entity.TableEntity;
import com.mmk.etl.jpa.service.DataBaseBzService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 数据库相关操作的控制器
 * 
 * @author 大漠穷秋
 */
@RestController
@Slf4j
@RequestMapping("/mind-mark/db")
public class DBController {
    public DataBaseBzService dataBaseBzService;

    @Value("${application.page-size}")
    private Integer pageSize;

    // 添加显式构造函数
    public DBController(DataBaseBzService dataBaseBzService) {
        this.dataBaseBzService = dataBaseBzService;
    }

    /**
     * 分页查询所有数据库配置
     * 
     * @param page 当前页码
     * @return 包含数据库配置的分页对象
     */
    @GetMapping("/list/{page}")
    public Page<DbEntity> getAllDatabasesPageable(@PathVariable Integer page) {
        return dataBaseBzService.getDbEntityListByUserIdPageable(1, page, pageSize);
    }

    /**
     * 根据用户 ID 分页查询数据库配置
     * 
     * @param userId 用户 ID
     * @param page   当前页码
     * @return 包含数据库配置的分页对象
     */
    @GetMapping("/user/{userId}/{page}")
    public ResponseEntity<Page<DbEntity>> getDatabasesByUserId(@PathVariable Integer userId, @PathVariable Integer page) {
        try {
            Page<DbEntity> dbEntities = dataBaseBzService.getDbEntityListByUserIdPageable(userId, page, pageSize);
            return ResponseEntity.ok(dbEntities);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 根据数据库 ID 分页查询 schema 配置
     * 
     * @param dbId 数据库 ID
     * @param page 当前页码
     * @return 包含 schema 配置的分页对象
     */
    @GetMapping("/schemas/{dbId}/{page}")
    public ResponseEntity<Page<SchemaEntity>> getSchemasByDbId(@PathVariable Integer dbId, @PathVariable Integer page) {
        try {
            Page<SchemaEntity> schemaEntities = dataBaseBzService.getSchemaEntityListDbIdPageable(dbId, page, pageSize);
            return ResponseEntity.ok(schemaEntities);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 根据 schema ID 分页查询 table 配置
     * 
     * @param schemaId schema ID
     * @param page     当前页码
     * @return 包含 table 配置的分页对象
     */
    @GetMapping("/tables/{schemaId}/{page}")
    public ResponseEntity<Page<TableEntity>> getTablesBySchemaId(@PathVariable Integer schemaId, @PathVariable Integer page) {
        try {
            Page<TableEntity> tableEntities = dataBaseBzService.getTableEntityListSchemaIdPageable(schemaId, page, pageSize);
            return ResponseEntity.ok(tableEntities);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 创建新的数据库配置
     * 
     * @param dbEntity 数据库配置对象
     * @return 创建的数据库配置对象
     */
    @PostMapping("/create")
    public ResponseEntity<DbEntity> createDatabase(@RequestBody DbEntity dbEntity) {
        try {
            DbEntity createdDbEntity = dataBaseBzService.createDatabase(dbEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDbEntity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 根据 ID 删除数据库配置
     * 
     * @param id 数据库配置 ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDatabase(@PathVariable Integer id) {
        try {
            dataBaseBzService.deleteDatabase(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

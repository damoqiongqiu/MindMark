package com.mmk.etl.controller;

import com.mmk.etl.jpa.entity.DbEntity;
import com.mmk.etl.jpa.entity.SchemaEntity;
import com.mmk.etl.jpa.entity.TableEntity;
import com.mmk.etl.jpa.service.DataBaseBzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @param userId 用户ID
     */
    @GetMapping("/list/{userId}")
    public List<DbEntity> getAllDatabasesPageable(@PathVariable Integer userId) {
        return dataBaseBzService.getDbEntityListByUserId(userId);
    }

    /**
     * 根据用户 ID 分页查询数据库配置
     * 
     * @param userId 用户 ID
     * @param page   当前页码
     * @return 包含数据库配置的分页对象
     */
    @GetMapping("/user/{userId}/{page}")
    public Page<DbEntity> getDatabasesByUserId(@PathVariable Integer userId, @PathVariable Integer page) {
        return dataBaseBzService.getDbEntityListByUserIdPageable(userId, page, pageSize);
    }

    /**
     * 根据数据库 ID 分页查询 schema 配置
     * 
     * @param dbId 数据库 ID
     * @param page 当前页码
     * @return 包含 schema 配置的分页对象
     */
    @GetMapping("/schemas/{dbId}/{page}")
    public Page<SchemaEntity> getSchemasByDbId(@PathVariable Integer dbId, @PathVariable Integer page) {
        return dataBaseBzService.getSchemaEntityListByDbIdPageable(dbId, page, pageSize);
    }

    /**
     * 查询指定数据库下的所有 schema 配置（不分页）
     * 
     * @param dbId 数据库 ID
     * @return schema 配置列表
     */
    @GetMapping("/schemas/all/{dbId}")
    public List<SchemaEntity> getAllSchemasByDbId(@PathVariable Integer dbId) {
        return dataBaseBzService.getSchemaEntityListByDbId(dbId);
    }

    /**
     * 根据 schema ID 分页查询 table 配置
     * 
     * @param schemaId schema ID
     * @param page     当前页码
     * @return 包含 table 配置的分页对象
     */
    @GetMapping("/tables/{schemaId}/{page}")
    public Page<TableEntity> getTablesBySchemaId(@PathVariable Integer schemaId, @PathVariable Integer page) {
        return dataBaseBzService.getTableEntityListBySchemaIdPageable(schemaId, page, pageSize);
    }

    /**
     * 查询指定 schema 下的所有表配置（不分页）
     * 
     * @param schemaId schema ID
     * @return 表配置列表
     */
    @GetMapping("/tables/all/{schemaId}")
    public List<TableEntity> getAllTablesBySchemaId(@PathVariable Integer schemaId) {
        return dataBaseBzService.getTableEntityListBySchemaId(schemaId);
    }

    /**
     * 创建新的数据库配置
     * 
     * @param dbEntity 数据库配置对象
     * @return 创建的数据库配置对象
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public DbEntity createDatabase(@RequestBody DbEntity dbEntity) {
        return dataBaseBzService.createDatabase(dbEntity);
    }

    /**
     * 根据 ID 删除数据库配置
     * 
     * @param id 数据库配置 ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDatabase(@PathVariable Integer id) {
        dataBaseBzService.deleteDatabase(id);
    }
}

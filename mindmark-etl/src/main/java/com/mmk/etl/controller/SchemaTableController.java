package com.mmk.etl.controller;

import com.mmk.etl.jpa.entity.TableEntity;
import com.mmk.etl.jpa.service.DataBaseBzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Schema Table 相关操作的控制器
 * 
 * @author 大漠穷秋
 */
@RestController
@Slf4j
@RequestMapping("/mind-mark/schema-table")
public class SchemaTableController {
    public DataBaseBzService dataBaseBzService;

    @Value("${application.page-size}")
    private Integer pageSize;

    // 添加显式构造函数
    public SchemaTableController(DataBaseBzService dataBaseBzService) {
        this.dataBaseBzService = dataBaseBzService;
    }

    /**
     * 根据 dbId 分页查询 table 配置
     */
    @GetMapping("/list/{dbId}/{page}")
    public Page<TableEntity> getTablesByDbId(@PathVariable Integer dbId, @PathVariable Integer page) {
        return dataBaseBzService.getTableEntityListByDbIdPageable(dbId, page, pageSize);
    }

    /**
     * 查询指定 db 下的所有表配置（不分页）
     * @return 表配置列表
     */
    @GetMapping("/list/all/{dbId}")
    public List<TableEntity> getAllTablesByDbId(@PathVariable Integer dbId) {
        return dataBaseBzService.getTableEntityListByDbId(dbId);
    }

    /**
     * 根据ID获取表配置详情
     * 
     * @param id 表配置ID
     * @return 表配置详情
     */
    @GetMapping("/{id}")
    public TableEntity getTableById(@PathVariable Integer id) {
        return dataBaseBzService.getTableById(id);
    }

    /**
     * 保存表配置
     * 
     * @param tableEntity 表配置对象
     * @return 保存后的表配置对象
     */
    @PutMapping("/save")
    public TableEntity saveTable(@RequestBody TableEntity tableEntity) {
        return dataBaseBzService.saveTable(tableEntity);
    }

    /**
     * 根据ID删除表配置
     * 
     * @param id 表配置ID
     */
    @DeleteMapping("/delete/{id}")
    public void deleteTable(@PathVariable Integer id) {
        dataBaseBzService.deleteTable(id);
    }
}

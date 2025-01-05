package com.mmk.etl.controller;

import com.mmk.etl.jpa.entity.DbEntity;
import com.mmk.etl.jpa.service.DataBaseBzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
     * 查询所有数据库配置（不分页）
     *
     * @param userId 用户ID
     */
    @GetMapping("/list/all/{userId}")
    public List<DbEntity> getAllDatabases(@PathVariable Integer userId) {
        return dataBaseBzService.getDbEntityListByUserId(userId);
    }

    /**
     * 根据用户 ID 分页查询数据库配置
     * 
     * @param userId 用户 ID
     * @param page   当前页码
     * @return 包含数据库配置的分页对象
     */
    @GetMapping("/list/{userId}/{page}")
    public Page<DbEntity> getDatabasesByUserIdPageable(@PathVariable Integer userId, @PathVariable Integer page) {
        return dataBaseBzService.getDbEntityListByUserIdPageable(userId, page, pageSize);
    }

    /**
     * 根据 ID 删除数据库配置
     * 
     * @param id 数据库配置 ID
     * @return 删除结果
     */
    @DeleteMapping("/delete/{id}")
    public void deleteDatabase(@PathVariable Integer id) {
        dataBaseBzService.deleteDatabase(id);
    }

    /**
     * 更新数据库配置
     * 
     * @param dbEntity 数据库配置对象
     * @return 更新后的数据库配置对象
     */
    @PutMapping("/save")
    public DbEntity saveDatabase(@RequestBody DbEntity dbEntity) {
        return dataBaseBzService.saveDatabase(dbEntity);
    }

    /**
     * 根据ID获取数据库配置详情
     * 
     * @param id 数据库配置ID
     * @return 数据库配置详情
     */
    @GetMapping("/{id}")
    public DbEntity getDatabaseById(@PathVariable Integer id) {
        return dataBaseBzService.getDatabaseById(id);
    }
}

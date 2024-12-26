package com.mmk.etl.controller;

import com.mmk.etl.jpa.entity.FileUploadEntity;
import com.mmk.etl.jpa.service.FileBzService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: 加权限控制，未登录用户禁止调用此上传接口
 * @author  大漠穷秋
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/mind-mark/file")
public class FileUploadController {

    private final FileBzService fileBzService;

    @GetMapping("/list/{page}")
    public Page<FileUploadEntity> getAllFilesPageable(@PathVariable Integer page) {
        //TODO: pageSize 放到配置文件中
        int pageSize=15;
        return fileBzService.getAllFilePageable(page, pageSize);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFileById(@PathVariable Integer id) {
        try {
            fileBzService.deleteFileById(id);
            return ResponseEntity.ok("File deleted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }

    /**
     * 文件上传
     *
     * @param files 上传的文件数组
     * @return 上传结果详情
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFiles(MultipartFile[] files) {
        Map<String, Object> response = new HashMap<>();

        if (files == null || files.length == 0) {
            log.warn("上传的文件列表为空");
            response.put("status", "failure");
            response.put("message", "没有检测到上传文件");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            // 调用 FileBzService 处理文件上传
            List<FileUploadEntity> uploadedFiles = fileBzService.upload(files);

            if (uploadedFiles == null || uploadedFiles.isEmpty()) {
                log.warn("所有文件上传失败");
                response.put("status", "failure");
                response.put("message", "所有文件上传失败，请检查文件格式或重试");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

            response.put("status", "success");
            response.put("message", "文件上传成功");
            response.put("uploadedFiles", uploadedFiles);
            log.info("文件上传成功，共上传 {} 个文件", uploadedFiles.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("文件上传过程中出现异常", e);
            response.put("status", "failure");
            response.put("message", "文件上传失败，请联系管理员");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

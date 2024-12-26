package com.mmk.etl.jpa.service;

import com.mmk.etl.EtlConfig;
import com.mmk.etl.jpa.entity.EmbeddingLogEntity;
import com.mmk.etl.jpa.entity.FileUploadEntity;
import com.mmk.etl.jpa.repository.IEmbeddingLogRepository;
import com.mmk.etl.jpa.repository.IFileUploadRepository;
import com.mmk.etl.util.HashUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FileBzService {
    private IEmbeddingLogRepository embeddingLogRepository;

    private IFileUploadRepository fileUploadRepository;

    private EtlConfig etlConfig;

    /**
     * 上传文件
     */
    public List<FileUploadEntity> upload(MultipartFile[] files) {
        try {
            String uploadPath=etlConfig.getWatchFile().getFilePath();

            //确保目录存在
            File targetDir = new File(uploadPath);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            List<FileUploadEntity> resultList = new ArrayList<>();
            for (MultipartFile file : files) {
                //取文件名
                String name = file.getOriginalFilename();
                if (StringUtils.isEmpty(name)) {
                    return null;
                }

                //取文件后缀
                String suffix = StringUtils.isNotBlank(FilenameUtils.getExtension(name)) ? FilenameUtils.getExtension(name) : "";

                //文件重命名，防止同目录文件覆盖
                String targetFileName = HashUtils.generateSaltedSha256() + "." + suffix;
                File targetFile = new File(uploadPath, targetFileName);
                file.transferTo(targetFile);

                //TODO:表里面还有其它字段，这里需要补全
                FileUploadEntity fileEntity = new FileUploadEntity();
                fileEntity.setDisplayName(name.replaceAll("." + suffix, ""));
                fileEntity.setFileName(targetFileName);
                fileEntity.setPath(targetFile.getPath());
                fileEntity.setFileSize(file.getSize());
                fileEntity.setFileSuffix(suffix);
                fileEntity.setUserId(1);//TODO: FIXME 取用户 ID
                fileEntity = this.fileUploadRepository.save(fileEntity);

                resultList.add(fileEntity);
            }
            return resultList;
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getStackTrace().toString());
        }
        return null;
    }

    /**
     * 按照每页的条数获取上传文件总页数
     * @param pageSize 每页条数
     * @return 总页数
     */
    public Integer getAllFilePageCount(int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("每页条数必须大于0");
        }
        long totalFiles = fileUploadRepository.count();
        return (int) Math.ceil((double) totalFiles / pageSize);
    }

    /**
     * 分页查询 file_upload 表
     * @param page
     * @param size
     * @return
     */
    public Page<FileUploadEntity> getAllFilePageable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "upTime"));
        return fileUploadRepository.findAll(pageable);
    }

    /**
     * 保存文件向量化操作的记录
     * @param fileId
     * @return
     */
    public EmbeddingLogEntity saveFileEmbeddingLog(Integer fileId) {
        EmbeddingLogEntity embeddingLogEntity = this.embeddingLogRepository.findByFileId(fileId);
        if (embeddingLogEntity == null) {
            embeddingLogEntity = new EmbeddingLogEntity();
            log.info("No existing EmbeddingLogEntity found, creating a new one.");
        }
        embeddingLogEntity.setFileId(fileId);
        return this.embeddingLogRepository.save(embeddingLogEntity);
    }

    /**
     * 获取文件向量化处理记录
     * @param fileId
     * @return
     */
    public EmbeddingLogEntity getFileEmbeddingLog(Integer fileId){
        return this.embeddingLogRepository.findByFileId(fileId);
    }

    public void deleteFileById(Integer id) {
        this.fileUploadRepository.deleteById(id);
    }
}

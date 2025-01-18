package com.mmk.etl.jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 维护用户上传的文件。
 * @author 大漠穷秋
 */

@Getter
@Setter
@Entity
@Table(name = "mind_mark_file_upload")
public class FileUploadEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    //文件名，实际存储此文件的名称，一般由系统通过 UUID 生成，防止同一目录中文件名冲突，此名称带有后缀
    @Column(name = "file_name")
    private String fileName;

    //文件的展示名称，一般取文件的原始名称，不带后缀
    @Column(name = "display_name")
    private String displayName;

    @Column(name = "fileSuffix")
    private String fileSuffix;

    @Column(name = "file_size")
    private Long fileSize;

    //文件在磁盘上的完整路径
    @Column(name = "path")
    private String path;

    @Column(name = "url")
    private String url;

    @Column(name = "file_desc")
    private String fileDesc;

    @Column(name = "display_order",columnDefinition = "int default 1")
    private Integer displayOrder=1;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="up_time",updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date upTime=new Date();

    @Column(name = "user_id")
    private Integer userId;
}

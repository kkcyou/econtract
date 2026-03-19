package com.yaoan.module.infra.api.file.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author LGY
 */
@Data

public class FilesDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String uploadId;

    private String fileMd5;

    private String url;

    private String name;

    private String type;

    private Long size;

    private Long chunkSize;

    private Integer chunkNum;

    private String path;
    private String configId;

}

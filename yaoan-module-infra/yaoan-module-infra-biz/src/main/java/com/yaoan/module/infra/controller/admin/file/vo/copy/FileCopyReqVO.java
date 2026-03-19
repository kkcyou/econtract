package com.yaoan.module.infra.controller.admin.file.vo.copy;

import com.yaoan.module.infra.enums.FileUploadPathEnum;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025/2/14 11:56
 */
@Data
public class FileCopyReqVO {

    /**
     * 文件id
     * */
    private Long fileId;

    /**
     * 保存路径
     * {@link FileUploadPathEnum}
     * */
    private Integer pathCode;

}

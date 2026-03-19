package com.yaoan.module.econtract.controller.admin.breakpointUpload.vo;


import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class FileUploadInfo {

//    @NotBlank(message = "文件名不能为空")
    private String fileName;

    @NotNull(message = "文件大小不能为空")
    private Long fileSize;

//    @NotBlank(message = "Content-Type不能为空")
    private String contentType;

    @NotNull(message = "分片数量不能为空")
    private Integer chunkNum;
    /**
     * 第几个分片
     * */
    private int chunk;
//    @NotBlank(message = "uploadId 不能为空")
    private String uploadId;

    private Long chunkSize;

    // 桶名称 (按租户分桶)
    private String bucketName  = "ecms-econtract-" + TenantContextHolder.getTenantId();

    //md5
    private String fileMd5;

    //文件类型
    private String type;

    //已上传的分片索引+1
    private List<Integer> chunkUploadedList;

}


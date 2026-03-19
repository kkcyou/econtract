package com.yaoan.module.econtract.controller.admin.breakpointUpload.vo;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class FileUploadCheckVO {

    private Integer code;
    private String message;
    private Integer pdfFileId;
    private String url;
    //已上传的分片索引+1
    private List<Integer> chunkUploadedList;
}


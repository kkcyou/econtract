package com.yaoan.module.econtract.api.contract.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class SppGPTUploadDTO {

    /**
     * 业务类型
     *
     */
    private String busType;

    /**
     * 合同文件
     *
     */
    private MultipartFile file ;

}

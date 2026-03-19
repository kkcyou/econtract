package com.yaoan.module.econtract.controller.admin.version.vo;

import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/8/29 15:20
 */
@Data
public class FileVersionSaveReqVO {

    /**
     * 业务id
     */
    private String businessId;

    /**
     * 是否需要模板留痕
     */
    private Boolean needSource;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 业务类型
     * {@link ActivityConfigurationEnum}
     */
    private Integer businessType;



    /**
     * 拷贝文件id
     */
    private Long copyFileId;

    /**
     * 拷贝文件名称
     */
    private String copyFileName;

    /**
     * 来源的拷贝文件id
     */
    private Long sourceCopyFileId;

    /**
     * 来源的拷贝文件名称
     */
    private String sourceCopyFileName;

    /**
     * 备注
     */
    private String remark;


    public FileVersionSaveReqVO(Integer businessType, String businessId, String remark) {
        this.businessId = businessId;
        this.businessType = businessType;
        this.remark = remark;

    }


}

package com.yaoan.module.econtract.api.gcy.buyplan.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: 合同附件
 * @author: doujl
 * @date: 2023/11/28 11:46
 */
@Data
public class ContractAttachmentVo implements Serializable {

    private static final long serialVersionUID = 1118278442351166880L;
    @Schema(description = "文件名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String fileName;
    /**
     * 合同附件类型
     * 1.合同文本(ContractAtt-0020) (必备附件)。
     * 2.中小企业声明函(ContractAtt-0030) (非必备附件)。
     * 3.联合体协议书(ContractAtt-0040) (非必备附件)。
     * 4.其他附件
     * (ContractAtt-9999) (非必备附件)。
     */
    @Schema(description = "合同附件类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String attachmentType;
    /**
     * 附件内容为空时，附件全路径不可为空
     */
    @Schema(description = "附件内容", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String attachment;
    /**
     * 附件全路径为空时，附件内容不可为空
     */
    @Schema(description = "附件全路径", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String filePath;

}

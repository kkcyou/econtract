package com.yaoan.module.econtract.api.contract.dto;

import cn.hutool.core.annotation.Alias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

/**
 * 合同附件对象
 *
 * @author zhc
 * @since 2023-12-05
 */
@Data
public class ContractFileDTO {
    /**
     * 附件名称
     */
    @Schema(description = "附件名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "附件名称不能为空")
    @Alias("attachmentName")
    private String fileName;
    /**
     *附件全路径（附件全路径为空时，附件内容不可为空）
     */
    @Schema(description = "附件路径", requiredMode = Schema.RequiredMode.REQUIRED)
    private String filePath;
    /**
     * 合同附件类型
     * 此处该参数为：
     * 1.合同文本(ContractAtt-0020) (必备附件)。
     * 2.中小企业声明函(ContractAtt-0030) (非必备附件)。
     * 3.联合体协议书(ContractAtt-0040) (非必备附件)。
     * 4.其他附件(ContractAtt-9999) (非必备附件)。
     * 5.合同公示附件（ContractAtt-0080）
     * 附件类型，完整合同文本传 合同文本（ContractAtt-0020）
     * 去除涉密条款的合同文本传 合同公示附件（ContractAtt-0080）
     *   合同其他附件（ContractAtt-9999）
     */
    @Schema(description = "合同附件类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String attachmentType;
    /**
     * 附件内容（附件内容为空时，附件全路径不可为空）
     */
    @Schema(description = "附件内容", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String attachment;
    /**
     * 文件ID
     */
    @Schema(description = "文件ID ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Alias("attachmentAddId")
    private Long fileId;

    private MultipartFile file;

    private Integer code;

}

package com.yaoan.module.econtract.dal.dataobject.gcy.gpmall;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * @description: 合同关联附件体类
 * @author: zhc
 * @date: 2024/01/03
 */
@Data
@TableName("ecms_neimeng_contract_file")
public class ContractFileDO extends BaseDO {

    private static final long serialVersionUID = -6187630386412093755L;
    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractId;
    @Schema(description = "文件名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;
    @Schema(description = "文件全路径", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileUrl;
    /**
     * 合同附件类型
     * 此处该参数为：z
     * 1.合同文本(ContractAtt-0020) (必备附件)。
     * 2.中小企业声明函(ContractAtt-0030) (非必备附件)。
     * 3.联合体协议书(ContractAtt-0040) (非必备附件)。
     * 4.其他附件(ContractAtt-9999) (非必备附件)。
     * 5.合同公示附件（ContractAtt-0080）
     * 附件类型，完整合同文本传 合同文本（ContractAtt-0020）
     * 去除涉密条款的合同文本传 合同公示附件（ContractAtt-0080）
     *   合同其他附件（ContractAtt-9999）
     *
     */
    @Schema(description = "合同附件类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String attachmentType;
    @Schema(description = "文件ID ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long fileId;


}

package com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yaoan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 对外合同分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OutwardContractPageReqVO extends PageParam {

    @Schema(description = "合同编码")
    private String code;

    @Schema(description = "合同名称", example = "张三")
    private String name;

    @Schema(description = "解除之前的状态", example = "2")
    private Integer oldStatus;

    @Schema(description = "合同状态", example = "1")
    private Integer status;

    @Schema(description = "合同内容")
    private byte[] contractContent;

    @Schema(description = "合同分类")
    private Integer contractCategory;

    @Schema(description = "合同类型", example = "1")
    private String contractType;

    @Schema(description = "合同描述", example = "你说的对")
    private String contractDescription;

    @Schema(description = "签署文件名称", example = "王五")
    private String fileName;

    @Schema(description = "文件地址id", example = "28572")
    private Long fileAddId;

    @Schema(description = "文件pdf地址id", example = "6274")
    private Long pdfFileId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "部门标识", example = "24868")
    private Long deptId;

    @Schema(description = "公司id", example = "18786")
    private Long companyId;

}

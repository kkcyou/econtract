package com.yaoan.module.econtract.controller.admin.businessformfield.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Schema(description = "管理后台 - 表单字段分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BusinessFormFieldPageReqVO extends PageParam {

    @Schema(description = "编码")
    private String fieldCode;

    @Schema(description = "名称", example = "张三")
    private String fieldName;

    @Schema(description = "所属表单id", example = "3557")
    private String formId;

    @Schema(description = "所属业务id", example = "14245")
    private String businessId;

    @Schema(description = "是否展示")
    private Integer isShow;

    @Schema(description = "是否在搜索中展示")
    private Integer isSearch;

   

}
package com.yaoan.module.econtract.controller.admin.review.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Schema(description = "ReviewConfigLogReqVO")
@Data
public class ReviewConfigLogReqVO {

    /**
     * 合同类型id
     */
    @NotNull(message = "合同类型id不能为空")
    @Schema(description = "合同类型id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String typeId;



}


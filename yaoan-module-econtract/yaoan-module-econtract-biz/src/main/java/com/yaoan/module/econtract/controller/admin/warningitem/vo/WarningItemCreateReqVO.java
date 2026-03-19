package com.yaoan.module.econtract.controller.admin.warningitem.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 预警事项表（new预警）创建 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WarningItemCreateReqVO extends WarningItemBaseVO {
    /**
     * 主键
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 检查点id
     */
    private String configId;
    /**
     * 预警事项名称
     */
    private String itemName;
    /**
     * 风险说明
     */
    private String itemRemark;
}

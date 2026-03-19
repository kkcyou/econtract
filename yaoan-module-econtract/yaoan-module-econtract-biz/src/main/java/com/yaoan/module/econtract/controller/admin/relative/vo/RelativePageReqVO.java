package com.yaoan.module.econtract.controller.admin.relative.vo;

import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 用于列表查询前端送值
 */
@Data
@Schema(description = "参数")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RelativePageReqVO extends CommonBpmAutoPageReqVO {
    @Schema(description = "相对方分类id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer categoryId;
    @Schema(description = "相对方名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "相对方编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
    @Schema(description = "相对方等级", requiredMode = Schema.RequiredMode.REQUIRED)
    private String levelNo;
    @Schema(description = "相对方性质", requiredMode = Schema.RequiredMode.REQUIRED)
    private String entityType;
    @Schema(description = "相对方类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String relativeType;
    @Schema(description = "相对方来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sourceType;
    @Schema(description = "黑名单状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;
    @Schema(description = "开始创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date startDate;

    @Schema(description = "创建结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date endDate;

    @Schema(description = "搜索字符串，用于模糊匹配相对方名称，统一信用代码，联系人，手机号码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String searchText;

    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creator;

    @Schema(description = "账号状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accountStatus;


    private List<String> statusList;

}

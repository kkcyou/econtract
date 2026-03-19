package com.yaoan.module.econtract.api.contract.dto;

import com.yaoan.module.econtract.api.gcy.order.GoodsVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 *
 *合同信息对象
 * @author zhc
 * @since 2023-12-05
 */
@Data
public class ContractDataDTO {
    /**
     * 合同信息对象列表
     */
    @Schema(description = "合同信息对象列表", requiredMode = Schema.RequiredMode.REQUIRED)
    ContractDTO contractDTO;
    /**
     * 可变参数
     */
    @Schema(description = "", requiredMode = Schema.RequiredMode.REQUIRED)
    Map<String, Object> resultMap;
    /**
     * 模板id
     */
    @Schema(description = "模板id", requiredMode = Schema.RequiredMode.REQUIRED)
   String modelId ;
    /**
     * code
     */
    String code;
    /**
     * 用户类型   0:系统管理员,1:采购单位,2:供应商,3:代理机构,4:采购监管机构,5:财政业务部门,6:评审专家,7:金融机构用户
     */
    @Schema(description = "用户类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer userType;
    /**
     * 标的信息
     */
    @Schema(description = "标的信息", requiredMode = Schema.RequiredMode.REQUIRED)
    List<GoodsVO> goodsVOS;

    private  String orderCode;

    /**
     * 是否合并订单
     * {@link com.yaoan.module.econtract.enums.common.IfEnums}
     * */
    private String ifMergeOrder;

}

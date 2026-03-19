package com.yaoan.module.econtract.controller.admin.gcy.gpmall;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.module.econtract.api.contract.dto.ContractDataDTO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order.GPMallOrderDetailReqVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order.GroupedDraftOrderInfoVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order.OrderIdListVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageReqVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.service.gcy.gpmall.GPMallOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/8 11:05
 */
@Slf4j
@RestController
@Validated
@Tag(name = "黑龙江订单", description = "黑龙江订单")
public class GPLMallOrderController {

    @Resource
    private GPMallOrderService gpMallOrderService;

    /**
     * 根据订单id获取草拟合同填充信息 - 单位端(HLJ_pro)
     * @param
     * @return
     */
    @DataPermission(enable = true)
    @PostMapping(value = "/econtract/contract/Org/query/OrderIdList")
    @Operation(summary = "根据订单id获取草拟合同填充信息", description = "根据订单id获取草拟合同填充信息")
    public CommonResult<ContractDataDTO> queryByOrgOrderId(@RequestBody OrderIdListVO orderIdListVO) throws Exception {
        ContractDataDTO contractDataDTO = null;
        if (PlatformEnums.GP_GPFA.getCode().equals(orderIdListVO.getContractFrom()) || PlatformEnums.GPMALL.getCode().equals(orderIdListVO.getContractFrom())) {
//            contractDataDTO = gpMallContractService.queryByOrgOrderV2(orderIdListVO);
        } else {
            contractDataDTO = gpMallOrderService.queryByOrgOrderV3(orderIdListVO);
        }
        return success(contractDataDTO);
    }

    /**
     * 卖场订单列表 org 单位端
     */
    @Operation(summary = "卖场订单列表")
    @PostMapping(value = "econtract/gpmall/org/list")
    public CommonResult<PageResult<GroupedDraftOrderInfoVO>> listGPMallOrgOrder(@RequestBody GPMallPageReqVO vo) {
        return CommonResult.success(gpMallOrderService.listGPMallOrgOrder(vo));
    }

    /**
     * 卖场订单查询子订单 org 单位端
     */
    @Operation(summary = "卖场订单查询子订单 org 单位端")
    @PostMapping(value = "econtract/gpmall/org/query")
    public CommonResult<List<GPMallPageRespVO>> queryGPMallOrderOrgDetail(@RequestBody GPMallOrderDetailReqVO vo) {
        return CommonResult.success(gpMallOrderService.queryGPMallOrderOrgDetail(vo));
    }

    /**
     * 订单列表（ 除了交易和卖场其外的所有订单 ）（ HLJ_pro ）
     * 包括：服务工程超市，框采，协议定点平台
     */
    @Operation(summary = "服务工程超市订单列表（ 除了交易和卖场其外的所有订单 ）")
    @PostMapping(value = "econtract/gpmall/list")
    public CommonResult<PageResult<GPMallPageRespVO>> listGPMallOrder( @RequestBody GPMallPageReqVO vo) {
        return CommonResult.success(gpMallOrderService.listGPMallOrder(vo));
    }

}

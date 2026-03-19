package com.yaoan.module.econtract.controller.admin.external;

import com.yaoan.framework.common.pojo.ExternallnterfaceResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.security.core.annotations.PrePermissioned;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BpmContractCreateReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.DraftOrderInfoDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageInfoDO;
import com.yaoan.module.econtract.service.category.CategoryService;
import com.yaoan.module.econtract.service.external.ExternalInterfaceService;
import com.yaoan.module.econtract.service.model.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;


/**
 * 对外提供接口
 *
 * @author zhc
 * @since 2024-04-24
 */
@Slf4j
@RestController
@RequestMapping("econtract")
@Validated
@Tag(name = "对外提供接口", description = "对外提供接口")
public class ExternalInterfaceController {
    @Resource
    private ExternalInterfaceService externalInterfaceService;

    @Resource
    private ModelService modelService;

    @Resource
    private CategoryService categoryService;
    @Resource
    private ContractApi contractApi;

    /**
     * 订单接收接口
     */
    @PrePermissioned
    @PermitAll
    @OperateLog(enable = true)
    @Operation(summary = "订单接收接口")
//    @Idempotent(timeout = 7, message = "正在处理中，请勿重复请求")
    @PostMapping(value = "/order/add")
    ExternallnterfaceResult<String> OrderInfoToEContract(@RequestBody EncryptDTO encryptDTO) {
        externalInterfaceService.checkSignGetClass(encryptDTO);
        EncryptDTO result = externalInterfaceService.saveOrderInfo(encryptDTO);
        return ExternallnterfaceResult.success(result.getRequestParam(), result.getMac());
    }

    /**
     * TODO
     * 订单接收接口(临时)
     */
    @PrePermissioned
    @PermitAll
    @OperateLog(enable = true)
    @Operation(summary = "订单接收接口(临时)")
//    @Idempotent(timeout = 7, message = "正在处理中，请勿重复请求")
    @PostMapping(value = "/order/saveOrderInfo4Temp")
    ExternallnterfaceResult<String> OrderInfoToEContract4Temp(@RequestBody EncryptDTO encryptDTO) {
        EncryptDTO result = externalInterfaceService.saveOrderInfo4Temp(encryptDTO);
        return ExternallnterfaceResult.success(result.getRequestParam(), result.getMac());
    }

    /**
     * 框采和卖场订单接收接口
     */
    @PrePermissioned
    @PermitAll
    @Operation(summary = "框采和卖场订单接收接口")
    @PostMapping(value = "/econtract/draft/order/encrypt")
    ExternallnterfaceResult<String> gPMallOrderInfoFromEContract(@RequestBody EncryptDTO encryptDTO) {
        externalInterfaceService.checkSignGetClass(encryptDTO);
        EncryptDTO result = externalInterfaceService.saveGPMallOrderInfo(encryptDTO);
        ;
        return ExternallnterfaceResult.success(result.getRequestParam(), result.getMac());
    }


    /**
     * 订单状态修改 - 订单状态 orderStatus （取消 作废 退货 回复正常）
     * @param encryptDTO
     * @return
     */
    @PrePermissioned
    @PermitAll
    @OperateLog(enable = true)
    @Operation(summary = "订单状态修改")
    @PostMapping(value = "/order/updateStatus")
    ExternallnterfaceResult<String> updateOrderStatus(@RequestBody EncryptDTO encryptDTO) {
        externalInterfaceService.checkSignGetClass(encryptDTO);
        EncryptDTO result = externalInterfaceService.updateOrderStatus(encryptDTO);
        return ExternallnterfaceResult.success(result.getRequestParam(), result.getMac());
    }

    /**
     * 订单状态修改 - 能否起草状态 status （待起草 已起草）
     */
    @TenantIgnore
    @PrePermissioned
    @PermitAll
    @OperateLog(enable = true)
    @Operation(summary = "订单状态修改")
    @PostMapping(value = "/order/update/status")
    ExternallnterfaceResult<String> updateStatus(@RequestBody DraftOrderInfoDO draftOrderInfoDO) {
        EncryptDTO result = externalInterfaceService.updateStatus(draftOrderInfoDO);
        return ExternallnterfaceResult.success(result.getRequestParam(), result.getMac());
    }

    /**
     * 包状态修改 - 能否起草状态 hidden （待起草 已起草） --- 电子交易
     */
    @TenantIgnore
    @PrePermissioned
    @PermitAll
    @OperateLog(enable = true)
    @Operation(summary = "包状态修改")
    @PostMapping(value = "/order/update/status/package")
    ExternallnterfaceResult<String> updateStatusPackage(@RequestBody PackageInfoDO packageInfoDO) {
        EncryptDTO result = externalInterfaceService.updateStatusPackage(packageInfoDO);
        return ExternallnterfaceResult.success(result.getRequestParam(), result.getMac());
    }

    /**
     * 合同签署信息同步
     */
    @PrePermissioned
    @PermitAll
    @OperateLog(enable = true)
    @Operation(summary = "同步签署信息")
    @PostMapping(value = "/contract/sendSignInfo")
    ExternallnterfaceResult<String> getContractSignInfo(@RequestBody ContractOrderExtDO encryptDTO) throws Exception{
        String result = externalInterfaceService.getContractSignInfo(encryptDTO);
        return ExternallnterfaceResult.success(null,null);
    }

    /**
     * 供应商签章后退回，同步状态(电子交易)
     */
    @PrePermissioned
    @PermitAll
    @OperateLog(enable = true)
    @Operation(summary = "同步状态")
    @PostMapping(value = "/contract/back")
    ExternallnterfaceResult<String> getContractBack(@RequestBody BpmContractCreateReqVO encryptDTO) {
        String result = externalInterfaceService.getContractBack(encryptDTO);
        return ExternallnterfaceResult.success(null,null);
    }
}


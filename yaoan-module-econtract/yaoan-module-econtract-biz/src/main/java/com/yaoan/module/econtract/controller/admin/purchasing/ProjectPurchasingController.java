package com.yaoan.module.econtract.controller.admin.purchasing;

import cn.hutool.json.JSONArray;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.api.purchasing.dto.ReqIdsDTO;
import com.yaoan.module.econtract.service.receive.business.IReceiveBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 对接系统
 */
@Slf4j
@RestController
@RequestMapping("econtract/purchasing")
@Tag(name = "项目采购对接", description = "项目采购对接")
public class ProjectPurchasingController {
    @Resource
    IReceiveBusinessService purchasingService;

    /**
     * 推送标识id
     *
     * @param id
     * @param type
     * @return
     */
    @GetMapping("/send/{id}/{type}/{tenantId}/{deptId}/{createUser}")
    @Operation(summary = "推送标识id", description = "推送标识id")
    @OperateLog(logArgs = false)
    public CommonResult<Object> sendId(@PathVariable("id") String id, @PathVariable("type") Integer type, @PathVariable("tenantId") String tenantId, @PathVariable("deptId") String deptId, @PathVariable("createUser") String createUser) {
        purchasingService.sendId(id, type,tenantId,deptId,createUser);
        return success(true);
    }

    /**
     * 根据推送的项目idS标识获取项目采购信息-批量
     *
     * @param reqIdsDTO
     * @return
     */
    @PostMapping(value = "/ids")
    @Operation(summary = "根据推送的项目idS标识获取项目采购信息-批量", description = "根据推送的项目idS标识获取项目采购信息-批量")
//    @OperateLog(logArgs = true)
    CommonResult<JSONArray> queryPurchasingByIds(@RequestBody ReqIdsDTO reqIdsDTO) {
        JSONArray projectPurchasingDTOS = purchasingService.queryPurchasingByIds(reqIdsDTO);
        return success(projectPurchasingDTOS);
    }

    /**
     * 根据推送的项目idS标识获取框架协议采购信息
     *
     * @param reqIdsDTO
     * @return
     */
    @PostMapping(value = "/framework/ids")
    @Operation(summary = "根据推送的项目idS标识获取框架协议采购信息-批量", description = "根据推送的项目idS标识获取框架协议采购信息-批量")
    @OperateLog(logArgs = false)
    CommonResult<JSONArray> queryFrameworkByIds(@RequestBody ReqIdsDTO reqIdsDTO) {
        JSONArray supplierOrderDTOS = purchasingService.queryFrameworkByIds(reqIdsDTO);
        return success(supplierOrderDTOS);
    }

    /**
     * 根据推送的项目idS标识获取电子卖场信息
     *
     * @param reqIdsDTO
     * @return
     */
    @PostMapping(value = "/elect/ids")
    @Operation(summary = "根据推送的项目idS标识获取电子卖场信息-批量", description = "根据推送的项目idS标识获取电子卖场信息-批量")
    @OperateLog(logArgs = false)
    CommonResult<JSONArray> queryElectronicsStoreByIds(@RequestBody ReqIdsDTO reqIdsDTO) {
        JSONArray supplierOrderDTOS = purchasingService.queryElectronicsStoreByIds(reqIdsDTO);
        return success(supplierOrderDTOS);
    }

}

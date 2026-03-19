package com.yaoan.module.econtract.controller.admin.order;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.api.contract.dto.ContractDataDTO;
import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfo;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.order.vo.*;
import com.yaoan.module.econtract.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/15 15:01
 */
@Slf4j
@RestController
@RequestMapping("econtract/order")
@Validated
@Tag(name = "订单模块", description = "订单模块")
public class OrderController {
    @Resource
    private OrderService orderService;

    /**
     * 接收并保存订单接口
     */
    @PermitAll
    @Operation(summary = "接收并保存订单接口")
    @PostMapping(value = "/receiveOrderInfos")
    CommonResult<String> receiveOrderInfos(@RequestBody List<DraftOrderInfo> draftOrderInfoList) {
        orderService.receiveOrderInfos(draftOrderInfoList);
        return CommonResult.success("success");
    }

    /**
     * 订单列表
     */
    @PostMapping(value = "/list")
    public CommonResult<PageResult<GPMallPageRespVO>> listGPMallOrder(@RequestBody GPMallPageReqVO vo) {
        return CommonResult.success(orderService.listGPMallOrder(vo));
    }

    /**
     * 根据订单获得合同信息，自动补全
     */
    @PostMapping(value = "/autoCreateContractByOrderId")
    public CommonResult<ThirdOrderAutoInfoRespVO> autoCreateContractByOrderId(@RequestBody IdReqVO vo) {
        return CommonResult.success(orderService.autoCreateContractByOrderId(vo));
    }

    /**
     * 查看单个订单的和基本信息
     */
    @PostMapping(value = "/getOrderBaseInfo")
    public CommonResult<OrderBaseInfoRespVO> getOrderBaseInfo(@RequestBody IdReqVO vo) {
        return CommonResult.success(orderService.getOrderBaseInfo(vo));
    }

    /**
     * 根据订单获得合同信息，自动补全
     */
    @PostMapping(value = "/getAutoInfo")
    public CommonResult<OrderAutoInfoRespVO> getAutoInfo(@RequestBody IdReqVO vo) {
        return CommonResult.success(orderService.getAutoInfo(vo));
    }



    /**
     * 第三方数据列表（黑龙江迁移过来的listGPMallOrder接口）
     * 按照公司做数据隔离
     */
    @Operation(summary = "第三方数据列表")
    @PostMapping(value = "/gpmall/listThirdData")
    public CommonResult<PageResult<GPMallPageRespVO>> listThirdData(@RequestBody GPMallPageReqVO vo) {
        return CommonResult.success(orderService.listThirdData(vo));
    }

    /**
     * 卖场订单列表
     */
    @Operation(summary = "卖场订单列表")
    @PostMapping(value = "econtract/gpmall/list2")
    public CommonResult<PageResult<GPMallPageRespVO>> listGPMallOrder2(@RequestBody GPMallPageReqVO vo) {
        return CommonResult.success(orderService.listGPMallOrder2(vo));
    }

    /**
     *  -------------------------------- 第三方数据接口 --------------------------------
     * */
    /**
     * V2订单列表
     */
    @PostMapping(value = "/listV2")
    public CommonResult<PageResult<GPMallPageV2RespVO>> listGPMallOrderV2(@RequestBody GPMallPageReqVO vo) {
        return CommonResult.success(orderService.listGPMallOrderV2(vo));
    }

    /**
     * V2根据订单获得合同信息，自动补全
     */
    @PostMapping(value = "/getAutoInfoV2")
    public CommonResult<OrderAutoInfoV2RespVO> getAutoInfoV2(@RequestBody IdReqVO vo) {
        return CommonResult.success(orderService.getAutoInfoV2(vo));
    }

    /**
     * 根据订单id获取订单详细信息和模板id、模板回填信息
     */
    @GetMapping("/getOrderInfo/{id}")
    @Operation(summary = "根据订单id获取订单详细信息和模板id、模板回填信息")
    @OperateLog(enable = false)
    public CommonResult<ContractDataDTO> getOrderInfoAndTemplateInfo(@PathVariable("id") String id) {
        return CommonResult.success(orderService.getOrderInfoAndTemplateInfo(id));
    }


}

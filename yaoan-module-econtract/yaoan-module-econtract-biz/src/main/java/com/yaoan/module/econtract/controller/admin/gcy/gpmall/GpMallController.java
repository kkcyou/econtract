package com.yaoan.module.econtract.controller.admin.gcy.gpmall;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.idempotent.core.annotation.Idempotent;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.api.contract.dto.ContractDataDTO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractDataVo;
import com.yaoan.module.econtract.controller.admin.contract.vo.UploadContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.save.OrderContractCreateReqV2Vo;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractRespVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;
import com.yaoan.module.econtract.service.gpmall.GPMallContractService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description: 电子卖场
 * @author: Pele
 * @date: 2023/12/4 10:59
 */
@Slf4j
@RestController
@Validated
public class GpMallController {
    @Resource
    private GPMallContractService gpMallContractService;

//    @PostMapping(value = "/econtract/draft/order")
//    String pushGPMallOrderInfoToEContract(@RequestBody DraftOrderInfo info) {
//        gpMallOrderService.saveOrderInfo(info);
//        return "success";
//    }


    /**
     * 根据订单id获取草拟合同填充信息
     *
     * @param
     * @return
     */
    @DataPermission(enable = false)
    @GetMapping(value = "/econtract/gcy/queryByOrderId/{id}")
    @Operation(summary = "根据订单id获取草拟合同填充信息", description = "根据订单id获取草拟合同填充信息")
    public CommonResult<ContractDataDTO> queryByOrderId(@PathVariable("id") String id, @RequestParam String contractFrom) throws Exception {
        ContractDataDTO contractDataDTO = gpMallContractService.queryByOrderId(id);
        return success(contractDataDTO);
    }

    /**
     * 电子卖场_电子合同_保存(HLJ_pro)
     *
     * @param gpMallContractCreateReqVO
     * @return
     */
    @PostMapping(value = "/econtract/contract/neimeng/create")
    @OperateLog(enable = true)
    @Operation(summary = "电子卖场_电子合同_保存(HLJ_pro)")
    @Idempotent(timeout = 5, message = "正在处理中，请勿重复提交")
    public CommonResult<String> saveContract(@RequestBody OrderContractCreateReqV2Vo gpMallContractCreateReqVO) throws JsonProcessingException {
        return success(gpMallContractService.saveContractAll(gpMallContractCreateReqVO));
    }

    /**
     * 电子卖场-推送合同(HLJ_pro)
     *
     * @param
     * @return
     */
    @PostMapping(value = "/econtract/contract/pushContractToEcontract/{id}")
    @OperateLog(enable = true)
    @Operation(summary = "电子卖场-推送合同(HLJ_pro)")
    @Idempotent(timeout = 5, message = "正在处理中，请勿重复提交")
    public CommonResult<String> pushContract(@PathVariable String id) throws JsonProcessingException {
        return success(gpMallContractService.pushContractToEcontract(id));
    }


    /**
     * 根据订单id获取草拟合同填充信息(HLJ_pro)
     * */
//    @DataPermission(enable = true)
//    @GetMapping(value = "/econtract/contract/queryByOrderId/{id}")
//    @Operation(summary = "根据订单id获取草拟合同填充信息(HLJ_pro)", description = "根据订单id获取草拟合同填充信息")
//    public CommonResult<ContractDataDTO> queryByOrderIdV2(@PathVariable("id") String id,@RequestParam String contractFrom) throws Exception {
////        ContractDataDTO contractDataDTO = gpMallContractService.queryByOrderId(id);
//        ContractDataDTO contractDataDTO=null;
//        if(PlatformEnums.GP_GPFA.getCode().equals(contractFrom)||PlatformEnums.GPMALL.getCode().equals(contractFrom)){
////            contractDataDTO = gpMallContractService.queryByOrderIdV2(id,contractFrom);
//        }else {
//            contractDataDTO = gpMallContractService.queryByOrderIdV3(id,contractFrom);
//        }
//        return success(contractDataDTO);
//    }

    /**
     * 查看合同詳情（HLJ_pro 电子合同-编辑时展示）
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/econtract/contract/query/{id}")
    @Operation(summary = "HLJ_pro 查看合同詳情")
    public CommonResult<ContractDataVo> queryById(@PathVariable String id, String contractFrom) throws Exception {
        ContractDataVo orderContractRespVO = null;
//        if(StringUtils.isEmpty(contractFrom)||PlatformEnums.GP_GPFA.getCode().equals(contractFrom)||PlatformEnums.GPMALL.getCode().equals(contractFrom)){
//            orderContractRespVO = gpMallContractService.queryByIdV2(id);
//        }else {
//            orderContractRespVO = gpMallContractService.queryById(id);
//        }
        //由于黑龙江框采、卖场调用的是第三方系统的合同，此处默认使用同一个接口
        orderContractRespVO = gpMallContractService.queryById(id);
        return success(orderContractRespVO);
    }

    /**
     * 上传合同  起草合同
     */
    @PutMapping(value = "/econtract/order/upload/contract")
    @Operation(summary = "上传合同方式起草")
    @Idempotent(timeout = 5, timeUnit = TimeUnit.SECONDS, message = "合同已提交，请勿重复提交")
    public CommonResult<String> uploadContract(@Valid @ModelAttribute UploadContractCreateReqVO vo) throws Exception {
        return success(gpMallContractService.uploadContractCreateOrUpdate(vo));
    }

    /**
     * 删除合同（HLJ_pro_电子卖场）
     */
    @GetMapping("/econtract/contract/neimeng/delete/{id}")
    @OperateLog(enable = true)
    @Operation(summary = "删除合同（HLJ_pro_电子卖场）")
    public CommonResult<Boolean> deleteContractById(@PathVariable String id) throws Exception {
        gpMallContractService.deleteContractById(id);
        return success(true);
    }

    /**
     * 上传合同_获取合同(HLJ_pro)
     */
    @GetMapping(value = "/econtract/contract/get/upload/contract/{id}")
    @Operation(summary = "上传合同_获取合同")
    public CommonResult<UploadContractCreateReqVO> getUploadContractById(@PathVariable("id") String id) throws Exception {
        return success(gpMallContractService.getUploadContractById(id));
    }

    /**
     * 获取订单和商品信息V2(上传合同)
     */
    @PostMapping(value = "/econtract/contract/order/goods")
    @Operation(summary = "获取订单和商品信息V2(上传合同)")
    public CommonResult<List<GPMallPageRespVO> > getOrderAndGoodsByOrderIds(@RequestBody List<String> orderList) {
        return success(gpMallContractService.getOrderAndGoodsByOrderIds(orderList));
    }


    /**
     * 根据订单id获取草拟合同填充信息V4(除了电子交易的其他平台的起草2回填)
     *
     * @param
     * @return
     */
//    @PrePermissioned
    @DataPermission(enable = true)
    @GetMapping(value = "/econtract/contract/queryByOrderIdV4/{id}")
    @Operation(summary = "根据订单id获取草拟合同填充信息V4", description = "根据订单id获取草拟合同填充信息V4")
    public CommonResult<ContractDataDTO> queryByOrderIdV4(@PathVariable("id") String id,@RequestParam String contractFrom) throws Exception {
        ContractDataDTO contractDataDTO = gpMallContractService.queryByOrderIdV4(id,contractFrom);
        return success(contractDataDTO);
    }

    /**
     * 签署-合同详情(非电子交易)
     */
    @GetMapping(value = "/queryById4Sign/{id}")
    @Operation(summary = "签署-合同详情(非电子交易)")
    public CommonResult<GPXContractRespVO> queryById4Sign(@PathVariable String id) throws Exception {
        GPXContractRespVO result = gpMallContractService.queryById4Sign(id);
        return CommonResult.success(result);
    }

}

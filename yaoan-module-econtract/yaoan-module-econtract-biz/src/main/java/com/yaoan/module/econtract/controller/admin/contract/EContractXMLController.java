package com.yaoan.module.econtract.controller.admin.contract;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.econtract.controller.admin.contract.vo.ValidatePdfVO;
import com.yaoan.module.econtract.service.contract.ContractXMLService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.HashMap;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 合同相关XML操作
 */
@RestController
@RequestMapping("econtract/xml")
public class EContractXMLController {

    @Resource
    private ContractXMLService contractxmlService;

    @PostMapping(value = "/getContractXML/{id}")
    @Operation(summary = "合同管理-数据展示", description = "获取合同列表")
    public CommonResult<Object> getContractXML (@PathVariable String id) {
        String contractXML = contractxmlService.getContractXML(id);
        HashMap<Object, Object> result = new HashMap<>();
        result.put("contractXML", contractXML);
        return new CommonResult<>().setCode(0).setData(result);
    }

    /**
     * 接收xml文件保存
     */
    @PostMapping(value = "/saveContractXML/{id}/{contractId}")
    public Long saveContractXML (@PathVariable String id, @PathVariable String contractId) {
         return contractxmlService.saveContractXML(id, contractId);
    }

    /**
     * 验证pdf文件  xml的真实性
     */
    @PutMapping(value = "/validateContractXML")
    public CommonResult<Object> validateContractXML (@ModelAttribute ValidatePdfVO vo) {
        return new CommonResult<>().setCode(0).setData(contractxmlService.validateContractXML(vo));
    }

    /**
     * 获取pdf文件的xml字符串
     */
    @PostMapping(value = "/getContractStructuredInfo")
    @PermitAll
    public CommonResult<String> getContractXMLString (@ModelAttribute ValidatePdfVO vo) {
        return success(contractxmlService.getContractXMLString(vo));
    }

}

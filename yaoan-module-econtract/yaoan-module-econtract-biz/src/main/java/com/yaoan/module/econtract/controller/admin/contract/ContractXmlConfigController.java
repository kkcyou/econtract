package com.yaoan.module.econtract.controller.admin.contract;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractXmlConfigCreateVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractXmlConfigPageVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractXmlConfigQueryVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractXmlConfigVO;
import com.yaoan.module.econtract.service.contract.ContractXmlConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


import static com.yaoan.framework.common.pojo.CommonResult.success;

@Slf4j
@RestController
@RequestMapping("/contract/xml/config")
@Tag(name = "合同xml配置接口", description = "合同xml配置接口")
public class ContractXmlConfigController {

    @Resource
    private ContractXmlConfigService contractXmlConfigService;

    @PostMapping("/save")
    public CommonResult<String> addContractXmlInfo(@RequestBody ContractXmlConfigCreateVO contractXmlInfo) {
        String  id = contractXmlConfigService.addContractXmlInfo(contractXmlInfo);
        return success(id);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResult<String> deleteContractXmlInfo(@PathVariable String id) {
        contractXmlConfigService.deleteContractXmlInfo(id);
        return success("删除成功");
    }


    @PostMapping("/update")
    public CommonResult<String> updateContractXmlInfo(@RequestBody ContractXmlConfigCreateVO contractXmlInfoDO) {
        String id = contractXmlConfigService.updateContractXmlInfo(contractXmlInfoDO);
        return success(id);
    }

    @GetMapping("/queryById/{id}")
    public CommonResult<ContractXmlConfigVO> getContractXmlInfoById(@PathVariable String id) {
        ContractXmlConfigVO contractXmlInfo = contractXmlConfigService.getContractXmlInfoById(id);
        return success(contractXmlInfo);
    }


    @PostMapping("/getList")
    public CommonResult<PageResult<ContractXmlConfigPageVO>> getAllContractXmlInfos(@RequestBody ContractXmlConfigQueryVO contractXmlInfoDO) {
        PageResult<ContractXmlConfigPageVO> result = contractXmlConfigService.getAllContractXmlInfos(contractXmlInfoDO);
        return success(result);
    }

}

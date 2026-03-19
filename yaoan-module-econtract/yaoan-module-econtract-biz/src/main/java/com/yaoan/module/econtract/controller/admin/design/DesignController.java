package com.yaoan.module.econtract.controller.admin.design;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.econtract.controller.admin.design.vo.ContractInfoVO;
import com.yaoan.module.econtract.service.design.DesignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 设计模式功能模块
 *
 * @author doujiale
 */
@Slf4j
@RestController
@RequestMapping("econtract/design")
@Validated
@Tag(name = "设计模式功能模块", description = "设计模式功能模块操作接口")
public class DesignController {

    @Resource
    private DesignService designService;

    /**
     * 构建者
     *
     * @param id param
     * @return info
     */
    @GetMapping("/contract/{id}")
    @Operation(summary = "构建者-建造合同信息")
    public CommonResult<ContractInfoVO> getContractInfo(@PathVariable("id") String id) {
        ContractInfoVO result = designService.getContractDetailById(id);
        return success(result);
    }

    /**
     * 观察者
     */
    @GetMapping("/contract/touch/{id}")
    @Operation(summary = "观察者- do something and send event")
    public CommonResult<Boolean> touchContract(@PathVariable("id") String id) {
        designService.touchContract(id);
        return success(Boolean.TRUE);
    }

}

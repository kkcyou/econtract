package com.yaoan.module.econtract.controller.admin.contractaidraftshow;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowCreateReqVO;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowRespShortVO;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowRespVO;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowUpdateReqVO;
import com.yaoan.module.econtract.convert.contractaidraftshow.ContractAiDraftShowConvert;
import com.yaoan.module.econtract.dal.dataobject.contractaidraftshow.ContractAiDraftShowDO;
import com.yaoan.module.econtract.service.contractaidraftshow.ContractAiDraftShowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 合同模板推荐")
@RestController
@RequestMapping("/ecms/contract-ai-draft-show")
@Validated
public class ContractAiDraftShowController {

    @Resource
    private ContractAiDraftShowService contractAiDraftShowService;

    @PostMapping("/create")
    @Operation(summary = "创建合同模板推荐")
    @PermitAll
    public ContractAiDraftShowRespShortVO createContractAiDraftShow(@Valid @RequestBody ContractAiDraftShowCreateReqVO createReqVO) {
        return contractAiDraftShowService.createContractAiDraftShow(createReqVO);
    }

    @PutMapping("/update")
    @Operation(summary = "更新合同模板推荐")
    public CommonResult<Boolean> updateContractAiDraftShow(@Valid @RequestBody ContractAiDraftShowUpdateReqVO updateReqVO) {
        contractAiDraftShowService.updateContractAiDraftShow(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除合同模板推荐")
    @Parameter(name = "id", description = "编号", required = true)
    @PermitAll
    public CommonResult<Boolean> deleteContractAiDraftShow(@RequestParam("id") Long id) {
        contractAiDraftShowService.deleteContractAiDraftShow(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得合同模板推荐")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PermitAll
    public CommonResult<ContractAiDraftShowRespVO> getContractAiDraftShow(@RequestParam("id") Long id) {
        ContractAiDraftShowDO contractAiDraftShow = contractAiDraftShowService.getContractAiDraftShow(id);
        return success(ContractAiDraftShowConvert.INSTANCE.convert(contractAiDraftShow));
    }

}

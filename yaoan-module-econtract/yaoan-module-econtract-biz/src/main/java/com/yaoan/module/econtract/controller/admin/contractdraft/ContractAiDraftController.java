package com.yaoan.module.econtract.controller.admin.contractdraft;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.AITemplateInfo;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.ContractAiDraftRecordCreateReqVO;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.ContractAiDraftRecordPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.ContractAiDraftRecordRespVO;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.ContractAiDraftRecordUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.ContractTypeShort;
import com.yaoan.module.econtract.controller.admin.contracttype.vo.ContractTypeSelectReqVO;
import com.yaoan.module.econtract.convert.contractaidraftrecord.ContractAiDraftRecordConvert;
import com.yaoan.module.econtract.dal.dataobject.contractaidraftrecord.ContractAiDraftRecordDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.service.contractaidraftrecord.ContractAiDraftRecordService;
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

@Tag(name = "管理后台 - 合同智能起草记录")
@RestController
@RequestMapping("/econtract/contract-ai-draft-record")
@Validated
public class ContractAiDraftController {

    @Resource
    private ContractAiDraftRecordService contractAiDraftRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建合同智能起草记录")
    @PermitAll
    public CommonResult<Long> createContractAiDraftRecord(@Valid @RequestBody ContractAiDraftRecordCreateReqVO createReqVO) {
        return success(contractAiDraftRecordService.createContractAiDraftRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新合同智能起草记录")
    @PermitAll
    public CommonResult<Boolean> updateContractAiDraftRecord(@Valid @RequestBody ContractAiDraftRecordUpdateReqVO updateReqVO) {
        contractAiDraftRecordService.updateContractAiDraftRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除合同智能起草记录")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteContractAiDraftRecord(@RequestParam("id") Long id) {
        contractAiDraftRecordService.deleteContractAiDraftRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得合同智能起草记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ContractAiDraftRecordRespVO> getContractAiDraftRecord(@RequestParam("id") Long id) {
        ContractAiDraftRecordDO contractAiDraftRecord = contractAiDraftRecordService.getContractAiDraftRecord(id);
        return success(ContractAiDraftRecordConvert.INSTANCE.convert(contractAiDraftRecord));
    }

    @GetMapping("/list")
    @Operation(summary = "获得合同智能起草记录列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<ContractAiDraftRecordRespVO>> getContractAiDraftRecordList(@RequestParam("ids") Collection<Long> ids) {
        List<ContractAiDraftRecordDO> list = contractAiDraftRecordService.getContractAiDraftRecordList(ids);
        return success(ContractAiDraftRecordConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得合同智能起草记录分页")
    public CommonResult<PageResult<ContractAiDraftRecordRespVO>> getContractAiDraftRecordPage(@Valid ContractAiDraftRecordPageReqVO pageVO) {
        PageResult<ContractAiDraftRecordDO> pageResult = contractAiDraftRecordService.getContractAiDraftRecordPage(pageVO);
        return success(ContractAiDraftRecordConvert.INSTANCE.convertPage(pageResult));
    }

    //查询合同类型list
    @PostMapping("/list-contract-type")
    @Operation(summary = "展示合同类型列表", description = "展示合同类型列表")
    @PermitAll
    public CommonResult<List<ContractTypeShort>> selectList(@RequestBody ContractTypeSelectReqVO vo) {
        List<ContractType> contractTypes = contractAiDraftRecordService.selectContractTypeList(vo);
        return success(ContractAiDraftRecordConvert.INSTANCE.convertList2(contractTypes));
    }

    //根据合同类型查询模板
    @GetMapping("/list-model")
    @Operation(summary = "根据合同类型查询模板")
    @PermitAll
    public List<AITemplateInfo> selectModel(@RequestParam("id") String id) {
        return contractAiDraftRecordService.selectModelListByContractTypeId(id);
    }


}

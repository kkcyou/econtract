package com.yaoan.module.econtract.controller.admin.relation;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractListRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractRepVO;
import com.yaoan.module.econtract.controller.admin.relation.vo.IncidenceRelationCreatReqVO;
import com.yaoan.module.econtract.controller.admin.relation.vo.IncidenceRelationResplistVO;
import com.yaoan.module.econtract.service.relation.IncidenceRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 合同关联关系
 */
@Slf4j
@RestController
@RequestMapping("econtract/relation")
@Tag(name = "合同关联关系", description = "合同关联关系")
public class IncidenceRelationController {
@Resource
private IncidenceRelationService incidenceRelationService;

    /**
     * 新增合同关联关系
     * @param incidenceRelationCreatReqVO
     * @return
     */
    @PutMapping(value = "/create")
    @Operation(summary = "新增合同关联关系", description = "新增合同关联关系")
    @OperateLog(logArgs = false)
    public CommonResult<String> createIncidenceRelation(@RequestBody IncidenceRelationCreatReqVO incidenceRelationCreatReqVO ) {
        String id = incidenceRelationService.createIncidenceRelation(incidenceRelationCreatReqVO);
        return success(id);
    }

    /**
     * 根据关联关系id取消合同关联关系
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "根据关联关系id取消合同关联关系", description = "根据关联关系id取消合同关联关系")
    @OperateLog(logArgs = false)
    public CommonResult deleteIncidenceRelation(@PathVariable String id ) {
        incidenceRelationService.deleteIncidenceRelation(id);
        return success(true);
    }

    /**
     * 查看该合同是否有此关联关系
     * @param id 为合同id
     * @return
     */
    @PostMapping(value = "/check")
    @Operation(summary = "查看该合同是否有此关联关系", description = "查看该合同是否有此关联关系")
    @OperateLog(logArgs = false)
    public CommonResult checkIncidenceRelation(@RequestParam String id,@RequestParam Integer incidenceRelation ) {
        incidenceRelationService.checkIncidenceRelation(id,incidenceRelation);
        return success(true);
    }

    /**
     * 根据合同id查询该合同下所有的关联关系信息
     * @param contractRepVO
     * @return
     */
    @PostMapping(value = "/list")
    @Operation(summary = "根据合同id查询该合同下所有的关联关系信息", description = "根据合同id查询该合同下所有的关联关系信息")
    @OperateLog(logArgs = false)
    public CommonResult<List<IncidenceRelationResplistVO>> queryAllIncidenceRelation(@RequestBody ContractRepVO contractRepVO) throws Exception {
        List<IncidenceRelationResplistVO> incidenceRelationResplistVOS = incidenceRelationService.queryAllIncidenceRelation(contractRepVO);
        return success(incidenceRelationResplistVOS);
    }
    /**
     * 获取所有合同信息--关联合同列表
     * @param contractRepVO
     * @return
     */
    @PostMapping(value = "contract/list")
    @Operation(summary = "获取所有合同信息--关联合同列表", description = "获取所有合同信息--关联合同列表")
    @OperateLog(logArgs = false)
    public CommonResult<List<ContractListRespVO>> queryAllContractInfo(@RequestBody ContractRepVO contractRepVO) {
        List<ContractListRespVO> contractRespVOPageResult = incidenceRelationService.queryAllContractInfo(contractRepVO);
        return success(contractRespVOPageResult);
    }


}

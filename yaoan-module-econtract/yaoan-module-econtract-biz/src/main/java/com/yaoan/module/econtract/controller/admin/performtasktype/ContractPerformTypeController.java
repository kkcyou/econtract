package com.yaoan.module.econtract.controller.admin.performtasktype;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.performtasktype.vo.*;
import com.yaoan.module.econtract.service.performTaskType.PerformTaskTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/1 13:19
 */
@Slf4j
@Validated
@RestController
@RequestMapping("econtract/contractPerformType")
@Tag(name = "履约任务类型管理", description = "履约任务类型管理接口")
public class ContractPerformTypeController {
    @Resource
    private PerformTaskTypeService performTaskTypeService;

    /**
     * 获得履约任务类型列表
     */
    @PostMapping("/page")
    @Operation(summary = "获得履约任务类型列表")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<PerformTaskTypeRespVO>> getPerformTaskTypePage(@RequestBody @Validated PerformTaskTypeListReqVo vo) {
        return success(performTaskTypeService.getPerformTaskTypePage(vo));
    }

    /**
     * 创建履约任务类型
     */
    @PostMapping("create")
    @Operation(summary = "创建履约任务类型")
    @OperateLog(logArgs = false)
    public CommonResult<Object> createPerformTaskType(@Valid @RequestBody PerformTaskTypeCreateReqVO reqVO) {
        performTaskTypeService.create(reqVO);
        return success(true);
    }

    /**
     * 更新履约任务类型
     */
    @PutMapping("update")
    @Operation(summary = "更新履约任务类型")
    @OperateLog(logArgs = false)
    public CommonResult<Boolean> updatePerformTaskType(@Valid @RequestBody PerformTaskTypeUpdateReqVO reqVO) {
        performTaskTypeService.updatePerformTaskType(reqVO);
        return success(true);
    }

    /**
     * 删除履约任务类
     */
    @DeleteMapping("delete")
    @Operation(summary = "删除履约任务类")
    @OperateLog(logArgs = false)
    public CommonResult<Boolean> deletePerformTaskType(@Valid @RequestBody DeleteVO reqVO) {
        performTaskTypeService.deletePerformTaskType(reqVO);
        return success(true);

    }
}

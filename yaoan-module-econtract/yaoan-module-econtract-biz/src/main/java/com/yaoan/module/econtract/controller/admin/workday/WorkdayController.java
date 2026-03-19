package com.yaoan.module.econtract.controller.admin.workday;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.workday.vo.InitReqVO;
import com.yaoan.module.econtract.controller.admin.workday.vo.UpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.workday.WorkdayDO;
import com.yaoan.module.econtract.service.workday.WorkDayService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Slf4j
@RestController
@RequestMapping("econtract/workday")
@Validated
public class WorkdayController {

    @Resource
    private WorkDayService workDayService;

    @PostMapping("/getList")
    @Operation(summary = "查询日期列表")
    @DataPermission(enable = false)
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<WorkdayDO>> getList(@RequestBody InitReqVO vo) throws Exception {
        return success(workDayService.getList(vo));
    }

    @PostMapping(value = "/init")
    @Operation(summary = "新增工作日", description = "新增工作日--初始化")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(logArgs = false)
    public CommonResult<String> insert(@RequestBody InitReqVO vo) throws Exception {
        return success(workDayService.initByYear(vo));
    }

    @PostMapping(value = "/changeType")
    @Operation(summary = "修改是否工作日", description = "修改-修改是否工作日")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(logArgs = false)
    public CommonResult<String> insert(@RequestBody UpdateReqVO vo) throws Exception {
        return success(workDayService.updateDayType(vo));
    }

    /**
     * 更新节假日
     * 每个节假日之前进行更新
     */
    @GetMapping("/refreshHoliday")
    @Operation(summary = "更新节假日")
    @PermitAll
    public CommonResult<Boolean> refreshHoliday() throws Exception {
        workDayService.refreshHoliday();
        return success(true);
    }

}

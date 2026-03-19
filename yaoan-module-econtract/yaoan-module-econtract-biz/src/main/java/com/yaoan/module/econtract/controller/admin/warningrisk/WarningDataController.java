package com.yaoan.module.econtract.controller.admin.warningrisk;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.excel.core.util.ExcelUtils;
import com.yaoan.module.econtract.controller.admin.warningrisk.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningruleconfig.WarningRuleConfigDO;
import com.yaoan.module.econtract.service.warningrisk.WarningRiskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 风险预警管理控制器，用于管理风险规则配置、规则启用及触发条件
 */
@Tag(name = "管理后台 - 风险预警管理")
@RestController
@RequestMapping("/ecms/warning-risk")
@Validated
public class WarningDataController {

    @Resource
    private WarningRiskService warningRiskService;

    @GetMapping("/get-one")
    @Operation(summary = "获得单个规则详情")
    //根据规则id 判断是什么规则
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<WarningRiskRespVO> getWarningData(@RequestParam("id") String id) {
        WarningRiskRespVO warningData = warningRiskService.getWarningData(id);
        return success(warningData);
    }


    @GetMapping("/page")
    @Operation(summary = "查询风险预警管理列表")
    public CommonResult<PageResult<WarningRiskRespVO>> getWarningDataPage(@Valid WarningRiskPageReqVO pageReqVO) {
        PageResult<WarningRuleConfigDO> pageResult = warningRiskService.getWarningDataPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WarningRiskRespVO.class));
    }

    @PostMapping("/export-excel")
    @Operation(summary = "导出风险预警列表数据 Excel")
    public void exportWarningDataExcel(@Valid @RequestBody WarningRiskPageReqVO pageReqVO,
                                       HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WarningRuleConfigDO> list = warningRiskService.getWarningDataPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "预警结果.xls", "数据", WarningRiskRespVO.class,
                BeanUtils.toBean(list, WarningRiskRespVO.class));
    }

    @PostMapping("/edit-enable")
    @Operation(summary = "编辑风险预警管理--是否启用按钮")
    public CommonResult<Boolean> editEnable(@Valid @RequestBody WarningRiskEditReqVO editReqVO) {

        warningRiskService.editEnable(editReqVO);
        return success(true);
    }

    //用于预警规则管理界面中的规则配置变更。
    @PostMapping("/update-rule")
    @Operation(summary = "变更预警规则")
    public CommonResult<String> createWarningData(@Valid @RequestBody WarningRuleConfigSaveReqVO vo) {
        String result = warningRiskService.createOrUpdateWarningRule(vo);
        return success(result);
    }

    @GetMapping("/getCreditRiskStatus")
    @Operation(summary = "获取相对方资信风险情况")
    public CommonResult<Boolean> getCreditRiskStatus(@RequestParam("id") String id) {
        warningRiskService.getCreditRiskStatus(id);
        return success(true);
    }

    @GetMapping("/riskAlertReminder")
    @Operation(summary = "合同签署提醒")
    public void riskAlertReminder() throws Exception {
         warningRiskService.riskAlertReminder();

    }
    @GetMapping("/getLevelTypeList")
    @Operation(summary = "获取风险等级列表")
    public CommonResult<List<WarningLevelTypeRespVO>> getLevelTypeList()  {
        List<WarningLevelTypeRespVO> result = warningRiskService.getLevelTypeList();
        return success(result);
    }
}
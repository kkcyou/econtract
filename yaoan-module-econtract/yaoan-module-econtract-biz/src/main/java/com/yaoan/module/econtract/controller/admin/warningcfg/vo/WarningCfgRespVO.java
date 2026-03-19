package com.yaoan.module.econtract.controller.admin.warningcfg.vo;

import com.yaoan.module.econtract.controller.admin.warningcfg.vo.get.WarningCfgBase4CfgRespVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.get.WarningItem4CfgRespVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.get.WarningItemRule4CfgRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Schema(description = "管理后台 - 预警检查配置表(new预警) Response VO")
@Data
@ToString(callSuper = true)
public class WarningCfgRespVO {

    /**
     * 检查点基本信息
     */
    private WarningCfgBase4CfgRespVO cfgBase4CfgRespVO;

    /**
     * 预警事项信息
     */
    private List<WarningItem4CfgRespVO> itemRespVOList;

    /**
     * 规则配置
     */
    private List<WarningItemRule4CfgRespVO> ruleRespVOList;


}

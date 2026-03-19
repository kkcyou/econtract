package com.yaoan.module.econtract.controller.admin.warningcfg.vo;

import com.yaoan.module.econtract.controller.admin.warningcfg.vo.create.WarningCfgBase4CfgCreateReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.create.WarningItem4CfgCreateReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.create.WarningItemRule4CfgCreateReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Schema(description = "管理后台 - 预警检查配置表(new预警)创建 Request VO")
@Data
@ToString(callSuper = true)
public class WarningCfgCreateReqVO  {

    /**
     * 检查点基本信息
     */
    private WarningCfgBase4CfgCreateReqVO cfgBase4CfgCreateReqVO;

    /**
     * 预警事项信息
     */
    private List<WarningItem4CfgCreateReqVO> itemReqVOList;
    /**
     * 规则配置
     * */
    private List<WarningItemRule4CfgCreateReqVO> ruleReqVOList;
}

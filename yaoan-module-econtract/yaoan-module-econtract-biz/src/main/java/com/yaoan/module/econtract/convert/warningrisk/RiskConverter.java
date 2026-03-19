package com.yaoan.module.econtract.convert.warningrisk;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo.RiskAlertPageRespVO;
import com.yaoan.module.econtract.controller.admin.warningrisk.vo.WarningLevelTypeRespVO;
import com.yaoan.module.econtract.controller.admin.warningrisk.vo.WarningRiskRespVO;
import com.yaoan.module.econtract.controller.admin.warningrisk.vo.WarningRuleConfigDetailSaveReqVO;
import com.yaoan.module.econtract.controller.admin.warningrisk.vo.WarningRuleConfigSaveReqVO;
import com.yaoan.module.econtract.convert.riskalert.RiskAlertConverter;
import com.yaoan.module.econtract.dal.dataobject.riskalert.RiskAlertDO;
import com.yaoan.module.econtract.dal.dataobject.warningleveltype.WarningLevelTypeDO;
import com.yaoan.module.econtract.dal.dataobject.warningruleconfig.WarningRuleConfigDO;
import com.yaoan.module.econtract.dal.dataobject.warningruleconfigdetail.WarningRuleConfigDetailDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface RiskConverter {

    RiskConverter INSTANCE = Mappers.getMapper(RiskConverter.class);

    WarningRuleConfigDO toRuleConfigDO(WarningRuleConfigSaveReqVO vo);

    WarningRiskRespVO toRiskRespVO(WarningRuleConfigDO vo);

    List<WarningLevelTypeRespVO> toLevelTypeList(List<WarningLevelTypeDO> vo);

    WarningRuleConfigDetailDO toRuleConfigDetailDO(WarningRuleConfigDetailSaveReqVO vo);

    List<WarningRuleConfigDetailDO> toRuleConfigDetailDOList(List<WarningRuleConfigDetailSaveReqVO> vo);

    List<WarningRuleConfigDetailSaveReqVO> toDetailSaveReqVOList(List<WarningRuleConfigDetailDO> vo);
}

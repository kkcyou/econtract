package com.yaoan.module.econtract.convert.warningitemrule;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.controller.admin.warningcfg.vo.create.WarningItemRule4CfgCreateReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.get.WarningItemRule4CfgRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yaoan.module.econtract.controller.admin.warningitemrule.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningitemrule.WarningItemRuleDO;

/**
 * 预警规则（new预警） Convert
 *
 * @author admin
 */
@Mapper
public interface WarningItemRuleConvert {

    WarningItemRuleConvert INSTANCE = Mappers.getMapper(WarningItemRuleConvert.class);

    WarningItemRuleDO convert(WarningItemRuleCreateReqVO bean);

    WarningItemRuleDO convert(WarningItemRuleUpdateReqVO bean);

    WarningItemRuleRespVO convert(WarningItemRuleDO bean);

    List<WarningItemRuleRespVO> convertList(List<WarningItemRuleDO> list);

    PageResult<WarningItemRuleRespVO> convertPage(PageResult<WarningItemRuleDO> page);

    List<WarningItemRuleExcelVO> convertList02(List<WarningItemRuleDO> list);


    WarningItemRuleDO req2DO4Cfg(WarningItemRule4CfgCreateReqVO reqVORule);

    List<WarningItemRule4CfgRespVO> listDo2Resp(List<WarningItemRuleDO> ruleDOList);
}

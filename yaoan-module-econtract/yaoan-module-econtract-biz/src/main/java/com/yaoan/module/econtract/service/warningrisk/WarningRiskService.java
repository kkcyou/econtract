package com.yaoan.module.econtract.service.warningrisk;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.warningrisk.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningruleconfig.WarningRuleConfigDO;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

/**
 * 预警结果 Service 接口
 *
 * @author lls
 */
public interface WarningRiskService {


    /**
     * 更新预警结果
     *
     * @param updateReqVO 更新信息
     */
    void updateWarningData(@Valid WarningRiskEditReqVO updateReqVO);

    /**
     * 删除预警结果
     *
     * @param id 编号
     */
    void deleteWarningData(String id);

    /**
     * 获得预警结果
     *
     * @param id 编号
     * @return 预警结果
     */
    WarningRiskRespVO getWarningData(String id);

    /**
     * 获得预警结果分页
     *
     * @param pageReqVO 分页查询
     * @return 预警结果分页
     */
    PageResult<WarningRuleConfigDO> getWarningDataPage(WarningRiskPageReqVO pageReqVO);

    /**
     * 编辑风险预警信息，是否启用预警
     *
     * @param editReqVO
     * @return
     */
    String editEnable(@Valid WarningRiskEditReqVO editReqVO);

    /**
     * 创建或更新预警规则
     *
     * @param reqVO
     */
    String createOrUpdateWarningRule(@Valid WarningRuleConfigSaveReqVO reqVO);

    void getCreditRiskStatus(String id);

    void riskAlertReminder() throws Exception;

    List<WarningLevelTypeRespVO> getLevelTypeList();
}
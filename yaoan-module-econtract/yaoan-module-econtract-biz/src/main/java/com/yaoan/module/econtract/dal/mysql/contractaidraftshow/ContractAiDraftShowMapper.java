package com.yaoan.module.econtract.dal.mysql.contractaidraftshow;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowExportReqVO;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractaidraftshow.ContractAiDraftShowDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 合同模板推荐 Mapper
 *
 * @author doujiale
 */
@Mapper
public interface ContractAiDraftShowMapper extends BaseMapperX<ContractAiDraftShowDO> {

    default PageResult<ContractAiDraftShowDO> selectPage(ContractAiDraftShowPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ContractAiDraftShowDO>()
                .likeIfPresent(ContractAiDraftShowDO::getTemplateName, reqVO.getTemplateName())
                .eqIfPresent(ContractAiDraftShowDO::getModel, reqVO.getModel())
                .eqIfPresent(ContractAiDraftShowDO::getTemplateContent, reqVO.getTemplateContent())
                .likeIfPresent(ContractAiDraftShowDO::getContractName, reqVO.getContractName())
                .eqIfPresent(ContractAiDraftShowDO::getUseNum, reqVO.getUseNum())
                .betweenIfPresent(ContractAiDraftShowDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ContractAiDraftShowDO::getCreateTime));
    }

    default List<ContractAiDraftShowDO> selectList(ContractAiDraftShowExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ContractAiDraftShowDO>()
                .likeIfPresent(ContractAiDraftShowDO::getTemplateName, reqVO.getTemplateName())
                .eqIfPresent(ContractAiDraftShowDO::getModel, reqVO.getModel())
                .eqIfPresent(ContractAiDraftShowDO::getTemplateContent, reqVO.getTemplateContent())
                .likeIfPresent(ContractAiDraftShowDO::getContractName, reqVO.getContractName())
                .eqIfPresent(ContractAiDraftShowDO::getUseNum, reqVO.getUseNum())
                .betweenIfPresent(ContractAiDraftShowDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ContractAiDraftShowDO::getCreateTime));
    }

}

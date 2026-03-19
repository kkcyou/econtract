package com.yaoan.module.econtract.convert.contractreviewitems;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.*;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule.ContractRuleCommonRespVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ContractReviewItemsDO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewContractTypeRelDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


/**
 * 合同审查规则 Convert
 *
 * @author admin
 */
@Mapper
public interface ContractReviewItemsConvert {

    ContractReviewItemsConvert INSTANCE = Mappers.getMapper(ContractReviewItemsConvert.class);

    ContractReviewItemsDO convert(ContractReviewItemsBaseVO bean);

    ContractReviewItemsBaseVO convertV1(ContractReviewItemsDO bean);

    @Mapping(target = "reviewId", source="id")
    @Mapping(target = "riskLevel", expression = "java(com.yaoan.module.econtract.enums.contractreviewitems.RiskLevelEnums.getInstance(bean.getRiskLevel()).getInfo())")
    @Mapping(target = "riskParty", expression = "java(com.yaoan.module.econtract.enums.contractreviewitems.RiskUnfavorableEnums.getInstance(bean.getRiskParty()).getInfo())")
    ContractRuleCommonRespVO convertRespVO(ContractReviewItemsBaseVO bean);

    List<ContractReviewItemsRespVO> convertList(List<ContractReviewItemsDO> list);

    PageResult<ContractReviewItemsRespVO> convertPage(PageResult<ContractReviewItemsDO> page);

    List<ContractReviewItemsExcelVO> convertListV2(List<ContractReviewItemsDO> list);

    List<ReviewContractTypeVO> convertListV3(List<ReviewContractTypeRelDO> list);
    List<ContractReviewItemsBaseVO> convertListV4(List<ContractReviewItemsDO> list);



}

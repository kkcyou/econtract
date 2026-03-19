package com.yaoan.module.econtract.convert.signet;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.date.DateUtils;
import com.yaoan.module.econtract.controller.admin.signet.vo.*;
import com.yaoan.module.econtract.dal.dataobject.signet.BpmContractSignetDO;
import com.yaoan.module.econtract.dal.dataobject.signet.ContractSignetDO;
import com.yaoan.module.econtract.dal.dataobject.signet.ContractSignetSpecsDO;
import com.yaoan.module.econtract.dal.dataobject.signet.ContractSignetTypeDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Mapper
public interface SignetConverter {

    SignetConverter INSTANCE = Mappers.getMapper(SignetConverter.class);

    ContractSignetDO createVoToEntity(SignetCreateReqVO vo);

    @Mapping(target = "contractId", source = "businessId")
    @Mapping(target = "contractName", source = "businessName")
    @Mapping(target = "contractCode", source = "businessCode")
    @Mapping(target = "sealId", source = "signetId")
    @Mapping(target = "sealName", source = "signetName")
    SignetManageRespVO toManageRespVO(BpmContractSignetDO vo);

    SignetDetailsRespVO doToRespVO(ContractSignetDO vo);
    @Mapping(target = "signetId", source = "sealId")
    @Mapping(target = "signetName", source = "sealName")
    @Mapping(target = "businessId", source = "contractId")
    BpmContractSignetDO sealBpmReqVOtoEntity(SealBpmReqVO vo);

    List<SignetDetailsRespVO> toRespVOList(List<ContractSignetDO> vo);

    List<SignetTypeVO> toSignetTypeVO(List<ContractSignetTypeDO> vo);

    List<Signet> toSignet(List<ContractSignetDO> vo);

    List<SignetSpecsVO> toSignetSpecsVO(List<ContractSignetSpecsDO> vo);

    List<SignetListRespVO> toSignetListRespList(List<ContractSignetDO> list);

    List<SignetProcessPageRespVO> toSignetProcessPageRespList(List<BpmContractSignetDO> list);

    @Mapping(target = "sealName", source = "signetName")
    @Mapping(target = "contractId", source = "businessId")
    @Mapping(target = "contractName", source = "businessName")
    @Mapping(target = "contractCode", source = "businessCode")
    @Mapping(target = "applyDate", source = "createTime")
    @Mapping(target = "applyUser", source = "creator")
    @Mapping(target = "sealId", source = "signetId")
    SignetProcessPageRespVO toSignetProcessPageResp(BpmContractSignetDO list);

    PageResult<SignetProcessPageRespVO> toPageResult2(PageResult<BpmContractSignetDO> pageResult);

    PageResult<SignetPageRespVO> toPageResult(PageResult<ContractSignetDO> pageResult);


    default Date localDateTimeToDate(LocalDateTime localDateTime){
        return  DateUtils.of(localDateTime);
    }

}

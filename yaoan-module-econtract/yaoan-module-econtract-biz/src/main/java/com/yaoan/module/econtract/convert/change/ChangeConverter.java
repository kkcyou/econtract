package com.yaoan.module.econtract.convert.change;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.change.vo.*;
import com.yaoan.module.econtract.controller.admin.contract.vo.ledger.BpmContractChangeRespVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangeDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangePaymentDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.ChangeElementDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.ElementDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/29 14:14
 */
@Mapper
public interface ChangeConverter {
    ChangeConverter INSTANCE = Mappers.getMapper(ChangeConverter.class);


    @Mapping(target = "changeName", source = "name")
    @Mapping(target = "changeExpirationDate", source = "expirationDate")
    BpmContractChangeDO changeSave2DO(ContractChangeSaveReqVO vo);
    BpmContractChangeDO changeSaveDO(ContractChangeStatusSaveReqVO vo);

    List<ContractChangeListApproveRespVO> convertBpmDO2RespList(List<BpmContractChangeDO> doList);

    @Mapping(target = "code", source = "changeCode")
    @Mapping(target = "name", source = "changeName")
    @Mapping(target = "submitTime", source = "createTime")
    ContractChangeListApproveRespVO bpm2Resp(BpmContractChangeDO entity);

    PageResult<ContractChangePageRespVO> do2RespPage(PageResult<BpmContractChangeDO> doList);

    List<ContractChangePageRespVO> simpleDO2RespList(List<BpmContractChangeDO> changeContractList);
    List<BpmContractChangePaymentDO> toPaymentDOList(List<BpmContractChangePaymentVO> changeContractList);

    @Mapping(target = "code", source = "changeCode")
    @Mapping(target = "name", source = "changeName")
    @Mapping(target = "submitTime", source = "createTime")
    ContractChangePageRespVO do2resp(BpmContractChangeDO entity);
    @Mapping(target = "contractCode", source = "contractId")
    ContractChangeListRespVO toListResp(BpmContractChangeDO entity);
    List<ElementRespVO> toElementList(List<ElementDO> entity);
    List<ChangeElementVO> toChangeElementList(List<ChangeElementDO> entity);
    PageResult<ContractChangeListRespVO> toPage(PageResult<BpmContractChangeDO> entity);

    List<BpmContractChangeRespVO> listDo2Resp(List<BpmContractChangeDO> bpmContractChangeDOList);
}

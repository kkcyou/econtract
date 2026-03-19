package com.yaoan.module.econtract.service.contracttype;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contracttype.vo.*;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;

import java.util.List;

/**
 * @author Pele
 * @description 针对表【ecms_contract_type】的数据库操作Service
 * @createDate 2023-08-08 16:50:22
 */
public interface ContractTypeService extends IService<ContractType> {

    void insert(ContractTypeCreateV2Vo vo);

    PageResult<ContractTypePageV2RespVo> getContractTypePage(ContractTypeListV2ReqVO vo);

    void update(ContractTypeUpdateV2Vo vo);

    Object showSelectContractType();

    ContractTypeSelectRespVO getContractTypeDetail(String id);

    Boolean updateContractTypeStatus(ContractTypeUpdateStatusReqVO vo);

    List<ContractTypePageV2RespVo> getList(ContractTypeExportReqVO exportReqVO);

    Object selectList(ContractTypeSelectReqVO vo);

    Object isContractActive(String id);

    Object isNeedSignet(ContractTypeSignetReqVO signetReqVO);

    void delete(List<String> ids);

    /**
     * 展示所有启用的一级合同类型
     *
     * @return {@link List }<{@link ContractTypeRespVO }>
     */
    List<ContractTypeRespVO> listActiveContractType();

//    PageResult<ContractTypePageRespVo> getContractTypePage(ContractTypeListReqVo vo);
//
//
//    void updateType(ContractTypeUpdateVo vo);
//
//    List<ContractTypeSimpleRespVo> getContractTypeSimpleList();
//
//    Object showSelectContractType(ContractTypeSelectReqVO reqVO);
//
//    void insert(ContractTypeCreateVo vo);
//
//    void insertV2(ContractTypeCreateV2Vo vo);
//
//    PageResult<ContractTypePageV2RespVo> getContractTypePageV2(ContractTypeListV2ReqVo vo);
}

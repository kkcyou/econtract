package com.yaoan.module.econtract.service.relation;

import com.yaoan.module.econtract.controller.admin.contract.vo.ContractListRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractRepVO;
import com.yaoan.module.econtract.controller.admin.relation.vo.IncidenceRelationCreatReqVO;
import com.yaoan.module.econtract.controller.admin.relation.vo.IncidenceRelationResplistVO;
import java.util.List;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
public interface IncidenceRelationService {
    String createIncidenceRelation( IncidenceRelationCreatReqVO incidenceRelationCreatReqVO );
    void deleteIncidenceRelation(String id);
    void checkIncidenceRelation(String id,Integer incidenceRelation );
    List<IncidenceRelationResplistVO> queryAllIncidenceRelation(ContractRepVO contractRepVO) throws Exception;
    List<ContractListRespVO> queryAllContractInfo(ContractRepVO contractRepVO);
}

package com.yaoan.module.econtract.convert.relation;

import com.yaoan.module.econtract.controller.admin.ledger.vo.LedgerRelationContractRespVO;
import com.yaoan.module.econtract.controller.admin.relation.vo.IncidenceRelationCreatReqVO;
import com.yaoan.module.econtract.controller.admin.relation.vo.IncidenceRelationResplistVO;
import com.yaoan.module.econtract.dal.dataobject.relation.IncidenceRelation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author：doujl
 * @Description:  转换类
 * @Date: 2023年07月05日14:30:38
 */
//@Mapper(componentModel = "spring")
@Mapper
public interface IncidenceRelationConverter {

    IncidenceRelationConverter INSTANCE = Mappers.getMapper(IncidenceRelationConverter.class);

    IncidenceRelation toEntity(IncidenceRelationCreatReqVO bean);
    List<IncidenceRelationResplistVO> tolistVo(List<IncidenceRelation> incidenceRelationList);
    List<LedgerRelationContractRespVO> tolistVo2(List<IncidenceRelation> incidenceRelationList);

}

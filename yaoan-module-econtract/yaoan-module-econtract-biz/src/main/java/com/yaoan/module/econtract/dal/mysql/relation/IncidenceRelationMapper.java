package com.yaoan.module.econtract.dal.mysql.relation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.dal.dataobject.relation.IncidenceRelation;
import com.yaoan.module.econtract.enums.IncidenceRelationEnums;
import dm.jdbc.util.StringUtil;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Mapper
public interface IncidenceRelationMapper extends BaseMapperX<IncidenceRelation> {
    default Long selectCount(String id, Integer incidenceRelation) {
        return selectCount(new LambdaQueryWrapperX<IncidenceRelation>().eqIfPresent(IncidenceRelation::getContractId,id)
                .eq(IncidenceRelation::getIncidenceRelation,incidenceRelation));
    }

    default Long selectCount2(String id) {
        LambdaQueryWrapperX<IncidenceRelation> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX .eq(IncidenceRelation::getRelationContractId, id);
        queryWrapperX.and(wrapper -> wrapper
                //框架协议
                .or().eq(IncidenceRelation::getIncidenceRelation, IncidenceRelationEnums.OUTLINE_AGREEMENT.getCode())
                //变更协议
                .or().eq(IncidenceRelation::getIncidenceRelation, IncidenceRelationEnums.CONTRACT_CHANGE_AGREEMENT.getCode())
                //续签协议
                .or().eq(IncidenceRelation::getIncidenceRelation, IncidenceRelationEnums.CONTRACT_RENEWAL_AGREEMENT.getCode()));
        return selectCount(queryWrapperX);
    }
    default   List<Map<String, Object>> selectMaps(List<String> contractIds) {
        return selectMaps(new QueryWrapper<IncidenceRelation>().select("COUNT(contract_id) AS count,contract_id AS contraId")
                .in("contract_id",contractIds).eq("incidence_relation", IncidenceRelationEnums.OUTLINE_AGREEMENT.getCode())
                .groupBy("contract_id"));
    }

    default   List<IncidenceRelation> selectIncidenceRelationList(String id) {
        LambdaQueryWrapperX<IncidenceRelation> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.and((queryWrapper) -> {
            if(StringUtil.isNotEmpty(id)) {
                queryWrapper.eq(IncidenceRelation::getRelationContractId, id)
                        .eq(IncidenceRelation::getIncidenceRelation, IncidenceRelationEnums.CONTRACT_CHANGE_AGREEMENT.getCode())
                        .or()
                        .eq(IncidenceRelation::getIncidenceRelation, IncidenceRelationEnums.CONTRACT_RENEWAL_AGREEMENT.getCode());
            } });
        return selectList(wrapperX);
            }

}

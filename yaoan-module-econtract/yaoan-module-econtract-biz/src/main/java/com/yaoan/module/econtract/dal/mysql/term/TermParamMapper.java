package com.yaoan.module.econtract.dal.mysql.term;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.term.vo.tree.TermTreeReqVO;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.dataobject.term.TermParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author doujiale
 * @since 2023-09-18
 */
@Mapper
public interface TermParamMapper extends BaseMapperX<TermParam> {

    default List<TermParam> selectParamByTermIds(List<String> termIds){
        return selectList(TermParam::getTermId,termIds);
    }
}

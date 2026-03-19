package com.yaoan.module.system.dal.mysql.dept;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.api.relative.dto.RelativeContactDTO;
import com.yaoan.module.econtract.api.relative.dto.RelativeDTO;
import com.yaoan.module.system.controller.admin.dept.vo.company.CompanyListReqVO;
import com.yaoan.module.system.dal.dataobject.dept.CompanyDO;
import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CompanyMapper extends BaseMapperX<CompanyDO> {

    default List<CompanyDO> selectList(CompanyListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<CompanyDO>()
                .likeIfPresent(CompanyDO::getName, reqVO.getName())
                .eqIfPresent(CompanyDO::getStatus, reqVO.getStatus()));
    }

    default CompanyDO selectByName(String name) {
        return selectOne(CompanyDO::getName, name);
    }

    default CompanyDO selectByCode(String code) {
        return selectOne(CompanyDO::getCreditCode, code);
    }

    default CompanyDO getByDeptId(Long deptId){
        MPJLambdaWrapper<CompanyDO> mpjLambdaWrapper = new MPJLambdaWrapper<CompanyDO>()
                .leftJoin(DeptDO.class, DeptDO::getCompanyId, CompanyDO::getId)
                .selectAll(CompanyDO.class)
                .eq(DeptDO::getId, deptId)
                .distinct();
        return selectOne(mpjLambdaWrapper);
    }



}

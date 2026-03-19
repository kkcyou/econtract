package com.yaoan.module.econtract.dal.mysql.outwardcontract;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.OutwardContractExportReqVO;
import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.OutwardContractPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.outwardcontract.OutwardContractDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 对外合同 Mapper
 *
 * @author Pele
 */
@Mapper
public interface OutwardContractMapper extends BaseMapperX<OutwardContractDO> {

    default PageResult<OutwardContractDO> selectPage(OutwardContractPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OutwardContractDO>()
                .eqIfPresent(OutwardContractDO::getCode, reqVO.getCode())
                .likeIfPresent(OutwardContractDO::getName, reqVO.getName())
                .eqIfPresent(OutwardContractDO::getOldStatus, reqVO.getOldStatus())
                .eqIfPresent(OutwardContractDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OutwardContractDO::getContractContent, reqVO.getContractContent())
                .eqIfPresent(OutwardContractDO::getContractCategory, reqVO.getContractCategory())
                .eqIfPresent(OutwardContractDO::getContractType, reqVO.getContractType())
                .eqIfPresent(OutwardContractDO::getContractDescription, reqVO.getContractDescription())
                .likeIfPresent(OutwardContractDO::getFileName, reqVO.getFileName())
                .eqIfPresent(OutwardContractDO::getFileAddId, reqVO.getFileAddId())
                .eqIfPresent(OutwardContractDO::getPdfFileId, reqVO.getPdfFileId())
                .betweenIfPresent(OutwardContractDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(OutwardContractDO::getDeptId, reqVO.getDeptId())
                .eqIfPresent(OutwardContractDO::getCompanyId, reqVO.getCompanyId())
                .orderByDesc(OutwardContractDO::getId));
    }

    default List<OutwardContractDO> selectList(OutwardContractExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<OutwardContractDO>()
                .eqIfPresent(OutwardContractDO::getCode, reqVO.getCode())
                .likeIfPresent(OutwardContractDO::getName, reqVO.getName())
                .eqIfPresent(OutwardContractDO::getOldStatus, reqVO.getOldStatus())
                .eqIfPresent(OutwardContractDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OutwardContractDO::getContractContent, reqVO.getContractContent())
                .eqIfPresent(OutwardContractDO::getContractCategory, reqVO.getContractCategory())
                .eqIfPresent(OutwardContractDO::getContractType, reqVO.getContractType())
                .eqIfPresent(OutwardContractDO::getContractDescription, reqVO.getContractDescription())
                .likeIfPresent(OutwardContractDO::getFileName, reqVO.getFileName())
                .eqIfPresent(OutwardContractDO::getFileAddId, reqVO.getFileAddId())
                .eqIfPresent(OutwardContractDO::getPdfFileId, reqVO.getPdfFileId())
                .betweenIfPresent(OutwardContractDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(OutwardContractDO::getDeptId, reqVO.getDeptId())
                .eqIfPresent(OutwardContractDO::getCompanyId, reqVO.getCompanyId())
                .orderByDesc(OutwardContractDO::getId));
    }

}

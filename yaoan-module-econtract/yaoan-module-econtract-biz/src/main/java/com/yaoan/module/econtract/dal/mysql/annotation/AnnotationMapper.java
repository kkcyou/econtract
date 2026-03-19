package com.yaoan.module.econtract.dal.mysql.annotation;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.dal.dataobject.annotation.AnnotationDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/25 15:16
 */
@Mapper
public interface AnnotationMapper extends BaseMapperX<AnnotationDO> {


    default List<AnnotationDO> getAnnotationByFileId(Long fileId) {
        return selectList(new LambdaQueryWrapperX<AnnotationDO>().eqIfPresent(AnnotationDO::getFileId, fileId));
    }

    default Long checkAnnotationsByFileId(Long fileId) {
        return selectCount(new LambdaQueryWrapperX<AnnotationDO>()
                .eqIfPresent(AnnotationDO::getFileId, fileId)
                .eq(AnnotationDO::getStatus, false)
        );
    }

    /**
     * 审批人查看批注
     * 自己和下一个节点审批人
     */
    default List<AnnotationDO> getAnnotationsForApprover(Long fileId, List<Integer> indexList) {
        return selectList(new LambdaQueryWrapperX<AnnotationDO>()
                .eqIfPresent(AnnotationDO::getFileId, fileId)
                .inIfPresent(AnnotationDO::getApproveIndex, indexList));
    }


}

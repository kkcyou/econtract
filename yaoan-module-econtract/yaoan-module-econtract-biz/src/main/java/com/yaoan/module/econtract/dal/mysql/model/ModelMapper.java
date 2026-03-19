package com.yaoan.module.econtract.dal.mysql.model;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.model.ModelBpmDO;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 15:37
 */
@Mapper
public interface ModelMapper extends BaseMapperX<Model> {
    default List<Model> searchTextInfo(String searchText) {
        // 创建QueryWrapper对象
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("id", "select template_id FROM ecms_contract t " +
                "where deleted=0 and t.template_id is not NULL GROUP BY t.template_id ORDER BY create_time DESC");
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.like("name", searchText);
        }
        queryWrapper.select("id,name,code,contract_type,time_effect_model,rtf_pdf_file_id");
        queryWrapper.last("LIMIT 6");
        return selectList(queryWrapper);
    }

    /**
     * 条款审批所用（审批人查看）
     */
    default PageResult<Model> selectApprovePage(CommonBpmAutoPageReqVO pageVO) {
        MPJLambdaWrapper<Model> mpjLambdaWrapper = new MPJLambdaWrapper<Model>()
                .selectAll(Model.class).orderByDesc(Model::getUpdateTime)
                .leftJoin(ModelBpmDO.class, ModelBpmDO::getModelId, Model::getId)
                .distinct();
        //审批时间
        if (ObjectUtil.isNotNull(pageVO.getApproveTime0())) {
            mpjLambdaWrapper.between(Model::getApproveTime, pageVO.getApproveTime0(), pageVO.getApproveTime1());
        }
        //提交时间
        if (ObjectUtil.isNotNull(pageVO.getCreateTime0())) {
            mpjLambdaWrapper.between(ModelBpmDO::getCreateTime, pageVO.getCreateTime0(), pageVO.getCreateTime1());
        }
        //审批状态
        if (ObjectUtil.isNotNull(pageVO.getResult())) {
            mpjLambdaWrapper.eq(ModelBpmDO::getResult, pageVO.getResult());
        }
        //名称
        if (ObjectUtil.isNotNull(pageVO.getName())) {
            mpjLambdaWrapper.like(Model::getName, pageVO.getName());
        }
        //编码
        if (ObjectUtil.isNotNull(pageVO.getCode())) {
            mpjLambdaWrapper.like(Model::getCode, pageVO.getCode());
        }
        //必须在相关流程实例
        if (CollectionUtil.isNotEmpty(pageVO.getInstanceIdList())) {
            mpjLambdaWrapper.in(ModelBpmDO::getProcessInstanceId, pageVO.getInstanceIdList());
        } else {
            return new PageResult<Model>().setTotal(0L).setList(Collections.emptyList());
        }

        return selectPage(pageVO, mpjLambdaWrapper);
    }

    default List<Model> selectlatest(Integer size) {
        return selectList(new LambdaQueryWrapperX<Model>().orderByDesc(Model::getCreateTime).last("LIMIT 10"));
    }
}

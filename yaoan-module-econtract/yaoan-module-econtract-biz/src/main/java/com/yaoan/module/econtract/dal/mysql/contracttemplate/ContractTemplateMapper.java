package com.yaoan.module.econtract.dal.mysql.contracttemplate;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.contracttemplate.vo.TemplateListReqVo;
import com.yaoan.module.econtract.dal.dataobject.bpm.template.TemplateBpmDO;
import com.yaoan.module.econtract.dal.dataobject.category.TemplateCategory;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;
import com.yaoan.module.econtract.enums.StatusEnums;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pele
 * @description 针对表【ecms_contract_template】的数据库操作Mapper
 * @createDate 2023-08-09 16:13:20
 * @Entity contracttemplate.ContractTemplate
 */
@Mapper
public interface ContractTemplateMapper extends BaseMapperX<ContractTemplate> {
    default PageResult<ContractTemplate> selectPage(TemplateListReqVo vo) {
        LambdaQueryWrapperX<ContractTemplate> wrapper = new LambdaQueryWrapperX<ContractTemplate>();
        if (ObjectUtil.isNotNull(vo.getTemplateCategoryId())) {
            wrapper.eqIfPresent(ContractTemplate::getTemplateCategoryId, String.valueOf(vo.getTemplateCategoryId()));
        }
        return selectPage(vo, wrapper.likeIfPresent(ContractTemplate::getName, vo.getName())
                .eqIfPresent(ContractTemplate::getTemplateSource, vo.getTemplateSource())
                .eqIfPresent(ContractTemplate::getPublishOrgan, vo.getPublishOrgan())
                .betweenIfPresent(ContractTemplate::getPublishTime, vo.getStartPublishTime(), vo.getEndPublishTime())
        );
    }

    default List<ContractTemplate> selectListFive(TemplateListReqVo vo) {
        LambdaQueryWrapperX<ContractTemplate> wrapper = new LambdaQueryWrapperX<ContractTemplate>();
        if (ObjectUtil.isNotNull(vo.getTemplateCategoryId())) {
            wrapper.eqIfPresent(ContractTemplate::getTemplateCategoryId, String.valueOf(vo.getTemplateCategoryId()));
        }
        wrapper.likeIfPresent(ContractTemplate::getName, vo.getName())
                .eqIfPresent(ContractTemplate::getTemplateSource, vo.getTemplateSource())
                .eqIfPresent(ContractTemplate::getPublishOrgan, vo.getPublishOrgan())
                .betweenIfPresent(ContractTemplate::getPublishTime, vo.getStartPublishTime(), vo.getEndPublishTime());
        return selectList(wrapper);
    }

    default PageResult<ContractTemplate> selectApprovePage(CommonBpmAutoPageReqVO pageVO) {
        if (CollectionUtils.isEmpty(pageVO.getInstanceIdList())) {
            return new PageResult<ContractTemplate>().setTotal(0L).setList(Collections.emptyList());
        }
        MPJLambdaWrapper<ContractTemplate> mpjLambdaWrapper = new MPJLambdaWrapper<ContractTemplate>()
                .leftJoin(TemplateBpmDO.class, TemplateBpmDO::getTemplateId, ContractTemplate::getId)
                .selectAll(ContractTemplate.class)
                .orderByDesc(ContractTemplate::getUpdateTime)
                .distinct();

        if (ObjectUtil.isNotNull(pageVO.getResult())) {
            mpjLambdaWrapper.eq(TemplateBpmDO::getResult, pageVO.getResult());
        }
        if (CollectionUtil.isNotEmpty(pageVO.getInstanceIdList())) {
            mpjLambdaWrapper.in(TemplateBpmDO::getProcessInstanceId, pageVO.getInstanceIdList());
        }
        if (StringUtils.isNotBlank(pageVO.getName())) {
            mpjLambdaWrapper.like(ContractTemplate::getName, pageVO.getName());
        }
        if (StringUtils.isNotBlank(pageVO.getCode())) {
            mpjLambdaWrapper.like(ContractTemplate::getCode, pageVO.getCode());
        }
        //提交时间
        if (ObjectUtil.isNotNull(pageVO.getSubmitTime0())) {
            mpjLambdaWrapper.between(ContractTemplate::getSubmitTime, pageVO.getSubmitTime0(), pageVO.getSubmitTime1());
        }

        return selectPage(pageVO, mpjLambdaWrapper);
    }

    default PageResult<ContractTemplate> getMyAllTemplatePage(TemplateListReqVo vo) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        LambdaQueryWrapperX<ContractTemplate> wrapperX = new LambdaQueryWrapperX<ContractTemplate>()
                .orderByDesc(ContractTemplate::getUpdateTime)
                //审批状态
                .eqIfPresent(ContractTemplate::getApproveStatus, vo.getApproveStatus())
                //范本来源
                .eqIfPresent(ContractTemplate::getTemplateSource, vo.getTemplateSource())
                //发布时间
                .betweenIfPresent(ContractTemplate::getPublishTime, vo.getStartPublishTime(), vo.getEndPublishTime())
                //范本名称
                .likeIfPresent(ContractTemplate::getName, vo.getName())
                //范本编码
                .likeIfPresent(ContractTemplate::getCode, vo.getCode())
                //创建时间
                .betweenIfPresent(ContractTemplate::getCreateTime, vo.getCreateTime0(), vo.getCreateTime1());
        if (StringUtils.isNotEmpty(vo.getFrontCode())) {
            CommonFlowableReqVOResultStatusEnums enums = CommonFlowableReqVOResultStatusEnums.getFrontInstance(vo.getFrontCode());
            if (ObjectUtil.isNotNull(enums)) {
                switch (enums) {
                    case TO_SEND:
                        wrapperX.and(
                                w -> w.eq(ContractTemplate::getApproveStatus, CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode())
                                        .or()
                                        .eq(ContractTemplate::getApproveStatus, CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode())
                        );

                        break;
                    default:
                        wrapperX.eq(ContractTemplate::getApproveStatus, enums.getResultCode());
                        break;
                }
            }
        }
        //当前用户的租户id和租户id为0
        wrapperX.and(
                w -> w.eq(ContractTemplate::getTenantId, loginUser != null ? loginUser.getTenantId() : null)
                        .or()
                        .eq(ContractTemplate::getTenantId, 0));
        //范本分类
        enhanceWrapperByCategoryIds(wrapperX, vo);
        return selectPage(vo, wrapperX);
    }


    default LambdaQueryWrapperX<ContractTemplate> enhanceWrapperByCategoryIds(LambdaQueryWrapperX<ContractTemplate> wrapperX, TemplateListReqVo vo) {
        if (CollectionUtil.isEmpty(vo.getCategoryIdList())) {
            wrapperX.eqIfPresent(ContractTemplate::getTemplateCategoryId, vo.getTemplateCategoryId());
            return wrapperX;
        }
        //模板分类的树状图搜索条件
        List<Integer> rootCategoryIds = new ArrayList<Integer>();
        rootCategoryIds.add(vo.getTemplateCategoryId());
        List<TemplateCategory> categoryList = getAllModelCategoryFromRoot(rootCategoryIds, vo);
        List<Integer> leafCategoryIds = new ArrayList<Integer>();
        if (CollectionUtil.isNotEmpty(categoryList)) {
            leafCategoryIds = categoryList.stream().map(TemplateCategory::getId).collect(Collectors.toList());
            vo.setCategoryIdList(leafCategoryIds);
        } else {
            leafCategoryIds.add(vo.getTemplateCategoryId());
            vo.setCategoryIdList(leafCategoryIds);
        }
        wrapperX.in(ContractTemplate::getTemplateCategoryId, leafCategoryIds);

        return wrapperX;
    }

    default List<TemplateCategory> getAllModelCategoryFromRoot(List<Integer> rootCategoryIds, TemplateListReqVo vo) {
        List<TemplateCategory> leafCategories = new ArrayList<>(); // 存放叶子节点的ModelCategory列表

        for (Integer rootCategoryId : rootCategoryIds) {
            findLeafCategories(rootCategoryId, vo.getAllCategories(), leafCategories);
        }
        return leafCategories;
    }

    default void findLeafCategories(Integer parentId, List<TemplateCategory> allCategories, List<TemplateCategory> leafCategories) {
        for (TemplateCategory category : allCategories) {
            if (category.getParentId().equals(parentId)) {
                if (hasChildren(category.getId(), allCategories)) {
                    findLeafCategories(category.getId(), allCategories, leafCategories);
                } else {
                    leafCategories.add(category);
                }
            }
        }
    }

    default boolean hasChildren(Integer parentId, List<TemplateCategory> allModelCategories) {
        for (TemplateCategory category : allModelCategories) {
            if (category.getParentId().equals(parentId)) {
                return true;
            }
        }
        return false;
    }

    default PageResult<ContractTemplate> queryTemplatePage(TemplateListReqVo vo) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        LambdaQueryWrapperX<ContractTemplate> wrapperX = new LambdaQueryWrapperX<ContractTemplate>()
                //审核通过
                .eq(ContractTemplate::getApproveStatus, StatusEnums.APPROVED.getCode()).orderByDesc(ContractTemplate::getUpdateTime)
                //发布状态
//                .eq(ContractTemplate::getPublishStatus, PublishStatusEnums.PUBLISH.getCode())
                //发布时间
                .betweenIfPresent(ContractTemplate::getPublishTime, vo.getStartPublishTime(), vo.getEndPublishTime())
                //发布来源
                .eqIfPresent(ContractTemplate::getTemplateSource, vo.getTemplateSource())
                .eqIfPresent(ContractTemplate::getUploadType, vo.getUploadType())
                //范本分类
                .eqIfPresent(ContractTemplate::getTemplateCategoryId, vo.getTemplateCategoryId())
                //范本编码
                .likeIfPresent(ContractTemplate::getCode, vo.getCode())
                //范本名称
                .likeIfPresent(ContractTemplate::getName, vo.getName())
                //范本来源
                .eqIfPresent(ContractTemplate::getTemplateSource, vo.getTemplateSource());
        if(vo.getIsGov() != null){
            //是否政府采购
            wrapperX.eqIfPresent(ContractTemplate::getTenantId, 0L);
        }else{
            //当前用户的租户id和租户id为0
            wrapperX.and(
                    w -> w.eq(ContractTemplate::getTenantId, loginUser != null ? loginUser.getTenantId() : null)
                            .or()
                            .eq(ContractTemplate::getTenantId, 0));
           
        }

        return selectPage(vo, wrapperX);

    }

}





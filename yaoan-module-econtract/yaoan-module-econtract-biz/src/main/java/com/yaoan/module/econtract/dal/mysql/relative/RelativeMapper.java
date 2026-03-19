package com.yaoan.module.econtract.dal.mysql.relative;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aspose.pdf.operators.Re;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.relative.dto.RelativeContactDTO;
import com.yaoan.module.econtract.controller.admin.relative.vo.RelativeBpmPageReqVO;
import com.yaoan.module.econtract.controller.admin.relative.vo.RelativePageReqVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.relativeContact.RelativeContact;
import com.yaoan.module.econtract.dal.dataobject.review.ReviewContractDO;
import com.yaoan.module.econtract.dal.mysql.relativeContact.RelativeContactMapper;
import com.yaoan.module.econtract.enums.EntityTypeEnums;
import com.yaoan.module.econtract.enums.StatusEnums;
import com.yaoan.module.econtract.enums.relative.RelativeStatusV2Enums;
import com.yaoan.module.econtract.util.RedisUtil;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.dal.mysql.user.SupplyMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
@Mapper
public interface RelativeMapper extends BaseMapperX<Relative> {

    default PageResult<Relative> queryAllRelative(RelativePageReqVO relativePageReqVO, String portType, Long relativeCompanyId) {
        QueryWrapper<Relative> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (ObjectUtil.isNotEmpty(relativePageReqVO.getSearchText())) {
            queryWrapper.and(wrapper -> wrapper
                        //1.1 根据搜索字符串模糊匹配联系人
                        .or().like("name", relativePageReqVO.getSearchText())
                        //1.2 根据搜索字符串模糊匹配手机号码
                        .or().like("contact_tel", relativePageReqVO.getSearchText())
                        .or().like("card_no", relativePageReqVO.getSearchText()));
                        //1.3 根据搜索字符串模糊匹配联系人
//                        .or().like("contact_name", relativePageReqVO.getSearchText())
//                        //1.4 根据搜索字符串模糊匹配手机号码
//                        .or().like("contact_tel", relativePageReqVO.getSearchText());
        }
        if (ObjectUtil.isNotEmpty(relativePageReqVO.getName())) {
            queryWrapper.and(wrapper -> wrapper.like("name", relativePageReqVO.getName()));
        }
        if (ObjectUtil.isNotEmpty(relativePageReqVO.getSourceType())) {
            queryWrapper.and(wrapper -> wrapper.eq("source_type", relativePageReqVO.getSourceType()));
        }
        if (ObjectUtil.isNotEmpty(relativePageReqVO.getCode())) {
            queryWrapper.and(wrapper -> wrapper.like("code", relativePageReqVO.getCode()));
        }
        if (ObjectUtil.isNotEmpty(relativePageReqVO.getLevelNo())) {
            queryWrapper.and(wrapper -> wrapper.eq("levelNo", relativePageReqVO.getLevelNo()));
        }
        if (ObjectUtil.isNotEmpty(relativePageReqVO.getStatus())) {
            queryWrapper.and(wrapper -> wrapper.eq("status", relativePageReqVO.getStatus()));
        }
        
        if (ObjectUtil.isNotEmpty(relativePageReqVO.getRelativeType())) {
            queryWrapper.and(wrapper -> wrapper.eq("relative_type", relativePageReqVO.getRelativeType()));
        }
        if (CollectionUtil.isNotEmpty(relativePageReqVO.getStatusList())) {
            queryWrapper.and(wrapper -> wrapper.in("status", relativePageReqVO.getStatusList()));
        }
        //不区分个人和单位
//        if (ObjectUtil.isNotEmpty(relativePageReqVO.getSearchText())) {
//            //个人
//            if (EntityTypeEnums.INDIVIDUAL.getCode().equals(relativePageReqVO.getEntityType())) {
//                queryWrapper.and(wrapper -> wrapper
//                        //1.1 根据搜索字符串模糊匹配联系人
//                        .or().like("name", relativePageReqVO.getSearchText())
//                        //1.2 根据搜索字符串模糊匹配手机号码
//                        .or().like("tel", relativePageReqVO.getSearchText())
//                );
//            } else {
//                //企业或政府
//                queryWrapper.and(wrapper -> wrapper
//                        // 1.1 根据搜索字符串模糊匹配相对方名称
//                        .like("name", relativePageReqVO.getSearchText())
//                        //1.2  根据搜索字符串模糊匹配统一社会信用代码
//                        .or().like("credit_code", relativePageReqVO.getSearchText())
//                        //1.3 根据搜索字符串模糊匹配联系人
//                        .or().like("contact_name", relativePageReqVO.getSearchText())
//                        //1.4 根据搜索字符串模糊匹配手机号码
//                        .or().like("contact_tel", relativePageReqVO.getSearchText())
//                );
//            }
//
//        }
        //2.根据分类id进行查询
//        if (ObjectUtil.isNotEmpty(relativePageReqVO.getCategoryId())) {
//            queryWrapper.and(wrapper -> wrapper.eq("category_id", relativePageReqVO.getCategoryId()));
//
//        }

        //3.根据主体类型进行查询
        if (ObjectUtil.isNotEmpty(relativePageReqVO.getEntityType())) {
            queryWrapper.and(wrapper -> wrapper.eq("entity_type", relativePageReqVO.getEntityType()));
        }
        //4.根据创建人进行查询
        if (ObjectUtil.isNotEmpty(relativePageReqVO.getCreator())) {
            queryWrapper.and(wrapper -> wrapper.like("creator", relativePageReqVO.getCreator()));
        }
        //5.根据账号状态进行查询
//        if (ObjectUtil.isNotEmpty(relativePageReqVO.getAccountStatus())) {
//            queryWrapper.and(wrapper -> wrapper.eq("account_status", relativePageReqVO.getAccountStatus()));
//        }
        //6.根据创建时间周期进行查询
        if (ObjectUtil.isNotEmpty(relativePageReqVO.getStartDate()) && ObjectUtil.isNotEmpty(relativePageReqVO.getEndDate())) {
            queryWrapper.and(wrapper -> wrapper.between("create_time", relativePageReqVO.getStartDate(), relativePageReqVO.getEndDate()));
        }
        //新增合同时添加相对方只展示有联系人的数据
//        if ("2".equals(portType) && !EntityTypeEnums.INDIVIDUAL.getCode().equals(relativePageReqVO.getEntityType())) {
//            queryWrapper.isNotNull("contact_id");
//            //排除相对方为自己的数据
////            if (ObjectUtil.isNotEmpty(relativeCompanyId)) {
////                queryWrapper.and(wrapper -> wrapper.ne("relative_company_id", relativeCompanyId));
////            }
//        }
        // 相对方的租户隔离逻辑
        Long tenantId = TenantContextHolder.getTenantId();
        queryWrapper.in("tenant_id",tenantId,0);
        queryWrapper.orderByDesc("tenant_type");
        return selectPage(relativePageReqVO, queryWrapper);
    }

    /**
     * 校验单位或企业是否重复
     *
     * @param id
     * @param creditCode
     * @return
     */
    default Boolean creditCodeExist(String id, String creditCode) {
        return selectCount(new LambdaQueryWrapperX<Relative>()
                .eqIfPresent(Relative::getCardNo, creditCode)
                .neIfPresent(Relative::getId, id)) > 0;
    }

    /**
     * 校验单位或企业是否重复
     *
     * @param id
     * @param companyName
     * @return
     */
    default Boolean companyNameExist(String id, String companyName) {
        return selectCount(new LambdaQueryWrapperX<Relative>()
                .eqIfPresent(Relative::getName, companyName)
                .neIfPresent(Relative::getId, id)) > 0;
    }

    /**
     * 校验个人身份证号是否重复
     *
     * @param id
     * @param idCard
     * @return
     */
    default Boolean idCardExist(String id, String idCard) {
        return selectCount(new LambdaQueryWrapperX<Relative>()
                .eqIfPresent(Relative::getCardNo, idCard)
                .neIfPresent(Relative::getId, id)) > 0;
    }

    /**
     * 根据合同id找到相对方信息（付款申请所用）
     */
    default List<Relative> selectListByContractId(String contractId) {
        MPJLambdaWrapper<Relative> mpjLambdaWrapper = new MPJLambdaWrapper<Relative>()
                .selectAll(Relative.class).orderByDesc(Relative::getCreateTime);
        mpjLambdaWrapper.leftJoin(SignatoryRelDO.class, SignatoryRelDO::getSignatoryId, Relative::getId)
                .eq(SignatoryRelDO::getContractId, contractId);
        return selectList(mpjLambdaWrapper);

    }

    default List<Relative> selectList4ProcessInstanceId(String processInstanceId){
        MPJLambdaWrapper<Relative> mpjLambdaWrapper = new MPJLambdaWrapper<Relative>()
                .select("t.id").orderByDesc(Relative::getCreateTime);
        mpjLambdaWrapper.leftJoin(SignatoryRelDO.class, SignatoryRelDO::getSignatoryId, Relative::getId)
                .leftJoin(BpmContract.class,BpmContract::getContractId,SignatoryRelDO::getContractId)
                .eq(BpmContract::getProcessInstanceId, processInstanceId);
        return selectList(mpjLambdaWrapper);
    }

    default PageResult<Relative> selectPage(RelativeBpmPageReqVO vo){
        LambdaQueryWrapperX<Relative> wrapperX = new LambdaQueryWrapperX<Relative>();
        //  wrapperX.eq(Relative::getFlowStatus, StatusEnums.NEVER_APPROVED.getCode());
        // 状态：草稿=4，审批中=5
        if(ObjectUtil.isNotNull(vo.getStatusList())){
            wrapperX.in(Relative::getStatus, vo.getStatusList());
        }
        if(StringUtils.isNotEmpty(vo.getName())){
            wrapperX.like(Relative::getName, vo.getName());
        }
        if(StringUtils.isNotEmpty(vo.getCode())){
            wrapperX.like(Relative::getCode, vo.getCode());
        }
        return selectPage(vo,wrapperX.orderByDesc(Relative::getUpdateTime));
    }

    default PageResult<Relative> selectApprovePage(RelativeBpmPageReqVO pageVO){
        if (CollectionUtils.isEmpty(pageVO.getInstanceIdList())) {
            return new PageResult<Relative>().setTotal(0L).setList(Collections.emptyList());
        }
        LambdaQueryWrapperX<Relative> wrapperX = new LambdaQueryWrapperX<>();
        if(CollectionUtil.isNotEmpty(pageVO.getInstanceIdList())){
            wrapperX.in(Relative::getProcessInstanceId,pageVO.getInstanceIdList());
        }
        if(StringUtils.isNotEmpty(pageVO.getName())){
            wrapperX.like(Relative::getName,pageVO.getName());
        }

        return selectPage(pageVO,wrapperX.orderByDesc(Relative::getSubmitTime));
    }

    default Relative get4AffirmPage(Long userId) {
        Relative relative = selectOne(Relative::getContactId, userId);
        if (ObjectUtil.isNotNull(relative)) {
            return relative;
        }
        MPJLambdaWrapper<Relative> mpjLambdaWrapper = new MPJLambdaWrapper<>();
        mpjLambdaWrapper.selectAll(Relative.class)
                .leftJoin(RelativeContact.class, RelativeContact::getRelativeId, Relative::getId)
                .eq(RelativeContact::getUserId, userId);
        relative = selectOne(mpjLambdaWrapper);

        return relative;
    }

    default List<Relative> selectList4Relative(Long userId){
        List<Relative> relativeList = selectList(Relative::getContactId,userId);
        if(relativeList.isEmpty()){
            MPJLambdaWrapper<Relative> wrapper =new MPJLambdaWrapper<Relative>()
                    .selectAll(Relative.class);
            wrapper.leftJoin(RelativeContact.class,RelativeContact::getRelativeId,Relative::getId)
                    .eq(RelativeContact::getUserId,userId);
            return selectList(wrapper);
        }else{
            return relativeList;
        }

    }

    default List<Relative> selectList4Ledger(String contractId){
        MPJLambdaWrapper<Relative> mpjLambdaWrapper = new MPJLambdaWrapper<Relative>()
                .select(Relative::getBankName,Relative::getBankAccount,Relative::getBankAccountName)
                .selectAs(SignatoryRelDO::getContractId,Relative::getArea)
                .orderByDesc(Relative::getCreateTime);
        mpjLambdaWrapper.leftJoin(SignatoryRelDO.class, SignatoryRelDO::getSignatoryId, Relative::getId)
                .eq(SignatoryRelDO::getContractId, contractId);
        return selectList(mpjLambdaWrapper);
    }

    ;
}

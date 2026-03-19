package com.yaoan.module.econtract.dal.mysql.contractarchives;

import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.ContractArchivesPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.dal.dataobject.permission.UserRoleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 合同档案 Mapper
 *
 * @author lls
 */
@Mapper
public interface ContractArchivesMapper extends BaseMapperX<ContractArchivesDO> {

    default PageResult<ContractArchivesDO> selectContractArchivesPage(ContractArchivesPageReqVO reqVO, List<Long> deptIds) {

        MPJLambdaWrapper<ContractArchivesDO> lqw = new MPJLambdaWrapper<>();
        lqw.selectAll(ContractArchivesDO.class);
        //部门
        if(ObjectUtil.isNotEmpty(deptIds)){
            lqw.in(ContractArchivesDO::getDeptId, deptIds);
        }
        //档案状态
        if (ObjectUtil.isNotEmpty(reqVO.getStatus())) {
            lqw.eq(ContractArchivesDO::getStatus, reqVO.getStatus());
        }
        //档号
//        if (ObjectUtil.isNotEmpty(reqVO.getCode())) {
//            lqw.like(ContractArchivesDO::getCode, reqVO.getCode());
//        }
        //档案名称
        if (ObjectUtil.isNotEmpty(reqVO.getName())) {
            lqw.like(ContractArchivesDO::getName, reqVO.getName());
        }
        //全宗号
//        if (ObjectUtil.isNotEmpty(reqVO.getFondsNo())) {
//            lqw.like(ContractArchivesDO::getFondsNo, reqVO.getFondsNo());
//        }
        //一级分类
//        if (ObjectUtil.isNotEmpty(reqVO.getFirstLevelNo())) {
//            lqw.like(ContractArchivesDO::getFirstLevelNo, reqVO.getFirstLevelNo());
//        }
        //二级分类
//        if (ObjectUtil.isNotEmpty(reqVO.getSecondLevelNo())) {
//            lqw.like(ContractArchivesDO::getSecondLevelNo, reqVO.getSecondLevelNo());
//        }
        //案卷号
//        if (ObjectUtil.isNotEmpty(reqVO.getVolumeNo())) {
//            lqw.like(ContractArchivesDO::getVolumeNo, reqVO.getVolumeNo());
//        }
        //项目名
        if (ObjectUtil.isNotEmpty(reqVO.getProName())) {
            lqw.like(ContractArchivesDO::getProName, reqVO.getProName());
        }
        //项目号
        if (ObjectUtil.isNotEmpty(reqVO.getProCode())) {
            lqw.like(ContractArchivesDO::getProCode, reqVO.getProCode());
        }
        //归档人
        if (ObjectUtil.isNotEmpty(reqVO.getArchiveUserName())) {
            lqw.like(ContractArchivesDO::getCreatorName, reqVO.getArchiveUserName());
        }
        //年度
        if (ObjectUtil.isNotEmpty(reqVO.getYear())) {
            lqw.like(ContractArchivesDO::getYear, reqVO.getYear());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getBeginArchiveTime()) && ObjectUtil.isNotEmpty(reqVO.getEndArchiveTime())) {
            //归档时间
            lqw.between(ContractArchivesDO::getArchiveTime, reqVO.getBeginArchiveTime(), reqVO.getEndArchiveTime());
        }
        //保管期限
        if (ObjectUtil.isNotEmpty(reqVO.getArchiveStorageYear())) {
            lqw.in(ContractArchivesDO::getArchiveStorageYear, reqVO.getArchiveStorageYear());
        }
        //开放情况
        if (ObjectUtil.isNotEmpty(reqVO.getOpenStatus())) {
            lqw.in(ContractArchivesDO::getOpenStatus, reqVO.getOpenStatus());
        }
        //关联合同表
        lqw.leftJoin(ContractDO.class, ContractDO::getId, ContractArchivesDO::getContractId);
        //合同名称
        if (ObjectUtil.isNotEmpty(reqVO.getContractName())) {
            lqw.like(ContractDO::getName, reqVO.getContractName());
        }
        //合同编号
        if (ObjectUtil.isNotEmpty(reqVO.getContractCode())) {
            lqw.like(ContractDO::getCode, reqVO.getContractCode());
        }
        //合同金额
        if (ObjectUtil.isNotEmpty(reqVO.getMinAmount()) && ObjectUtil.isNotEmpty(reqVO.getMaxAmount())) {
            lqw.between(ContractDO::getAmount, reqVO.getMinAmount(), reqVO.getMaxAmount());
        }
        //甲方
        if (ObjectUtil.isNotEmpty(reqVO.getPartyAName())) {
            lqw.like(ContractDO::getPartAName, reqVO.getPartyAName());
        }
        //乙方
        if (ObjectUtil.isNotEmpty(reqVO.getPartyBName())) {
            lqw.like(ContractDO::getPartBName, reqVO.getPartyBName());
        }
        //签订时间
        if (ObjectUtil.isNotEmpty(reqVO.getBeginContractSignTime()) && ObjectUtil.isNotEmpty(reqVO.getEndContractSignTime())) {
            lqw.between(ContractDO::getContractSignTime, reqVO.getBeginContractSignTime(), reqVO.getEndContractSignTime());
        }
        //合同生效日期
        if (ObjectUtil.isNotEmpty(reqVO.getBeginEffectiveDate()) && ObjectUtil.isNotEmpty(reqVO.getEndEffectiveDate())) {
            lqw.between(ContractDO::getValidity0, reqVO.getBeginEffectiveDate(), reqVO.getEndEffectiveDate());
        }
        //合同终止日期
        if (ObjectUtil.isNotEmpty(reqVO.getBeginTerminationDate()) && ObjectUtil.isNotEmpty(reqVO.getEndTerminationDate())) {
            lqw.between(ContractDO::getValidity1, reqVO.getBeginTerminationDate(), reqVO.getEndTerminationDate());
        }
        //结算类型
        if (ObjectUtil.isNotEmpty(reqVO.getAmountType())) {
            lqw.in(ContractDO::getAmountType, reqVO.getAmountType());
        }
        //合同类型
        if (ObjectUtil.isNotEmpty(reqVO.getContractType())) {
            lqw.in(ContractDO::getContractType, reqVO.getContractType());
        }

        if(ObjectUtil.isNotEmpty(reqVO.getCreator())){
            lqw.eq(ContractArchivesDO::getCreator, reqVO.getCreator());
        }
        //已归档按照归档时间由近到远排序
        if (ObjectUtil.isNotEmpty(reqVO.getStatus())) {
            if (reqVO.getStatus() == 1) {
                lqw.eq(ContractArchivesDO::getStatus, reqVO.getStatus())
                        .orderByDesc(ContractArchivesDO::getArchiveTime);
            } else {
                //归档中按照创建时间由近到远排序
                lqw.eq(ContractArchivesDO::getStatus, reqVO.getStatus())
                        .orderByDesc(ContractArchivesDO::getUpdateTime);
            }
        }else{
            lqw.orderByDesc(ContractArchivesDO::getUpdateTime);
        }

        return selectPage(reqVO, lqw);
    }
}
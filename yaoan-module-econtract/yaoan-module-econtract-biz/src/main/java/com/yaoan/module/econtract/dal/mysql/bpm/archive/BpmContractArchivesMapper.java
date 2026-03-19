package com.yaoan.module.econtract.dal.mysql.bpm.archive;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.query.MPJQueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.PageReqVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.archive.BpmContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow.ContractBorrowBpmDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Mapper
public interface BpmContractArchivesMapper extends BaseMapperX<BpmContractArchivesDO> {
    default PageResult<BpmContractArchivesDO> selectBpmPage(PageReqVO reqVO) {
        if (CollectionUtils.isEmpty(reqVO.getInstanceIdList())) {
            return new PageResult<BpmContractArchivesDO>().setTotal(0L).setList(Collections.emptyList());
        }
        MPJQueryWrapper<BpmContractArchivesDO> wrapperX = new MPJQueryWrapper<BpmContractArchivesDO>()
                .leftJoin("ecms_contract_archives a on a.id = t.archive_id").selectAll(BpmContractArchivesDO.class);
        // 档案名称
        if (ObjectUtil.isNotEmpty(reqVO.getName())) {
            wrapperX.like("a.name", reqVO.getName());
        }
        // 档号
        if (ObjectUtil.isNotEmpty(reqVO.getCode())) {
            wrapperX.like("a.code", reqVO.getCode());
        }
        // 审批类型
        if (ObjectUtil.isNotEmpty(reqVO.getType())){
            wrapperX.eq("t.type", reqVO.getType());
        }
        // 审批状态
        if (ObjectUtil.isNotEmpty(reqVO.getStatus())){
            wrapperX.eq( "t.result", reqVO.getStatus());
        }
        // 申请人名称
        if (ObjectUtil.isNotEmpty(reqVO.getApplicantName())){
            wrapperX.like( "t.creator_name", reqVO.getApplicantName());
        }
        // 申请时间
        if (ObjectUtil.isNotEmpty(reqVO.getStartTime()) && ObjectUtil.isNotEmpty(reqVO.getEndTime())) {
            wrapperX.between("t.create_time", reqVO.getStartTime(), reqVO.getEndTime());
        }
        //匹配流程实例list
        if(ObjectUtil.isNotEmpty(reqVO.getInstanceIdList())){
            wrapperX.in("t.process_instance_id", reqVO.getInstanceIdList());
        }
        wrapperX.orderByDesc("t.update_time");

        return selectPage(reqVO,wrapperX);
    }

    default Long count4Bench(List<String> instanceIdList){
        if (CollectionUtils.isEmpty(instanceIdList)) {
            return 0L;
        }
        return selectCount(new LambdaQueryWrapperX<BpmContractArchivesDO>().in(BpmContractArchivesDO::getProcessInstanceId,instanceIdList));
    }
}

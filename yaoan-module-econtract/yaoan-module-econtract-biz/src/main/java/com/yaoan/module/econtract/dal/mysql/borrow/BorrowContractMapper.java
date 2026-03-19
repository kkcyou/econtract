package com.yaoan.module.econtract.dal.mysql.borrow;

import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.query.MPJQueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.query.QueryWrapperX;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.ContractBorrowBpmPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.ContractBorrowRecordPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.borrow.BorrowContractDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Mapper
public interface BorrowContractMapper extends BaseMapperX<BorrowContractDO> {
    default PageResult<BorrowContractDO> selectContractPage(ContractBorrowRecordPageReqVO reqVO){
        MPJQueryWrapper<BorrowContractDO> mpjQueryWrapper = new MPJQueryWrapper<BorrowContractDO>()
                .leftJoin("ecms_contract c on t.contract_id = c.id AND c.deleted = 0")
                .eq("t.result", BpmProcessInstanceResultEnum.APPROVE.getResult())
                .selectAll(BorrowContractDO.class).orderByDesc("t.update_time");
        if (ObjectUtil.isNotEmpty(reqVO.getContractName())){
            mpjQueryWrapper.like("c.name", reqVO.getContractName());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getContractCode())){
            mpjQueryWrapper.like("c.code", reqVO.getContractCode());
        }
        return selectPage(reqVO,mpjQueryWrapper);
    }

    default PageResult<BorrowContractDO> selectContractPage2(ContractBorrowBpmPageReqVO reqVO){
        MPJQueryWrapper<BorrowContractDO> mpjQueryWrapper = new MPJQueryWrapper<BorrowContractDO>()
                .leftJoin("ecms_contract c on t.contract_id = c.id AND c.deleted = 0")
                .eq("t.result", BpmProcessInstanceResultEnum.APPROVE.getResult())
                .selectAll(BorrowContractDO.class).orderByDesc("t.update_time");
        if (ObjectUtil.isNotEmpty(reqVO.getContractName())){
            mpjQueryWrapper.like("c.name", reqVO.getContractName());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getContractCode())){
            mpjQueryWrapper.like("c.code", reqVO.getContractCode());
        }
        return selectPage(reqVO,mpjQueryWrapper);
    }

    default List<BorrowContractDO> selectContractList (String id){
        LambdaQueryWrapperX<BorrowContractDO> queryWrapperX = new LambdaQueryWrapperX<BorrowContractDO>()
                .eq(BorrowContractDO::getContractId,id).eq(BorrowContractDO::getResult,BpmProcessInstanceResultEnum.APPROVE.getResult()).orderByDesc(BorrowContractDO::getUpdateTime);
        return selectList(queryWrapperX);
    }

}

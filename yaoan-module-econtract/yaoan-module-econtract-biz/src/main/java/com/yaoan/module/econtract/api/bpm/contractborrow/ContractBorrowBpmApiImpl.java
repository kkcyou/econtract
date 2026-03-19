package com.yaoan.module.econtract.api.bpm.contractborrow;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.contractBorrow.ContractBorrowBpmApi;
import com.yaoan.module.econtract.dal.dataobject.borrow.BorrowContractDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow.ContractBorrowBpmDO;
import com.yaoan.module.econtract.dal.mysql.borrow.BorrowContractMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contractborrow.ContractBorrowBpmMapper;
import com.yaoan.module.econtract.service.contract.ContractService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 合同借阅审批流终节点监听器
 * @author: Pele
 * @date: 2023/10/9 16:14
 */
@Service
public class ContractBorrowBpmApiImpl implements ContractBorrowBpmApi {
    @Resource
    private ContractBorrowBpmMapper borrowBpmMapper;

    @Resource
    private BorrowContractMapper borrowContractMapper;

    @Resource
    private ContractService contractService;
    @Override
    @DataPermission(enable = false)
    public void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) throws Exception {
        ContractBorrowBpmDO borrowBpmDO = borrowBpmMapper.selectById(businessKey);
        if (borrowBpmDO != null) {
            // 更新模板审批流表状态
            borrowBpmDO.setResult(statusEnums.getResult());
            borrowBpmMapper.updateById(borrowBpmDO);

            if (statusEnums.getResult()==BpmProcessInstanceResultEnum.APPROVE.getResult()){
                List<BorrowContractDO> borrowContractDOS = borrowContractMapper.selectList(BorrowContractDO::getBorrowId, borrowBpmDO.getBorrowId());
                if (CollectionUtil.isNotEmpty(borrowContractDOS)){
                    for (BorrowContractDO borrowContractDO : borrowContractDOS) {
                        borrowContractDO.setResult(BpmProcessInstanceResultEnum.APPROVE.getResult());
                        //通过的文件添加水印
                        contractService.addTextWatermarkToPDF(borrowContractDO.getContractId(),null);
                    }
                    borrowContractMapper.updateBatch(borrowContractDOS);
                }
            }
        }
    }
}

package com.yaoan.module.econtract.api.contract;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.api.contract.dto.contract.SignatoryRelDTO;
import com.yaoan.module.econtract.convert.contract.SignatoryRelConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.*;

/**
 * ContractSignatoryApi 实现类
 *
 * @author ZHC
 */
@Service
@Slf4j
public class ContractSignatoryApiImpl implements ContractSignatoryApi {
    @Resource
    private SignatoryRelMapper signatoryRelMapper;


    @Override
    public List<SignatoryRelDTO> getContractSignatoryRelList(String userId, List<String> contractIds) {
        List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(new LambdaQueryWrapperX<SignatoryRelDO>()
                .eq(SignatoryRelDO::getContactId, userId)
                .inIfPresent(SignatoryRelDO::getContractId, contractIds));
        List<SignatoryRelDTO> signatoryRelDTOS = SignatoryRelConverter.INSTANCE.toSignatoryRelDTOS(signatoryRelDOS);
        return signatoryRelDTOS;
    }

    @Override
    public void updateContractSignatory(List<SignatoryRelDTO> signatoryRelList) {
        List<SignatoryRelDO> signatoryRelDOS = SignatoryRelConverter.INSTANCE.toSignatoryRelDOS(signatoryRelList);
        signatoryRelMapper.updateBatch(signatoryRelDOS);
    }
}

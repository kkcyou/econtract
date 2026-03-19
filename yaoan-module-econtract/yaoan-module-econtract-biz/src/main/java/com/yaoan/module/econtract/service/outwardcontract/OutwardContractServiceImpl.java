package com.yaoan.module.econtract.service.outwardcontract;

import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.OutwardContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.OutwardContractExportReqVO;
import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.OutwardContractPageReqVO;
import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.OutwardContractUpdateReqVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import com.yaoan.module.econtract.dal.dataobject.outwardcontract.OutwardContractDO;
import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.convert.outwardcontract.OutwardContractConvert;
import com.yaoan.module.econtract.dal.mysql.outwardcontract.OutwardContractMapper;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DATA_ERROR;

/**
 * 对外合同 Service 实现类
 *
 * @author Pele
 */
@Service
@Validated
public class OutwardContractServiceImpl implements OutwardContractService {

    @Resource
    private OutwardContractMapper outwardContractMapper;

    @Override
    public String createOutwardContract(OutwardContractCreateReqVO createReqVO) {
        // 插入
        OutwardContractDO outwardContract = OutwardContractConvert.INSTANCE.convert(createReqVO);
        outwardContractMapper.insert(outwardContract);
        // 返回
        return outwardContract.getId();
    }

    @Override
    public void updateOutwardContract(OutwardContractUpdateReqVO updateReqVO) {
        // 校验存在
        validateOutwardContractExists(updateReqVO.getId());
        // 更新
        OutwardContractDO updateObj = OutwardContractConvert.INSTANCE.convert(updateReqVO);
        outwardContractMapper.updateById(updateObj);
    }

    @Override
    public void deleteOutwardContract(String id) {
        // 校验存在
        validateOutwardContractExists(id);
        // 删除
        outwardContractMapper.deleteById(id);
    }

    private void validateOutwardContractExists(String id) {
        if (outwardContractMapper.selectById(id) == null) {
            throw exception(DATA_ERROR);
        }
    }

    @Override
    public OutwardContractDO getOutwardContract(String id) {
        return outwardContractMapper.selectById(id);
    }

    @Override
    public List<OutwardContractDO> getOutwardContractList(Collection<String> ids) {
        return outwardContractMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<OutwardContractDO> getOutwardContractPage(OutwardContractPageReqVO pageReqVO) {
        return outwardContractMapper.selectPage(pageReqVO);
    }

    @Override
    public List<OutwardContractDO> getOutwardContractList(OutwardContractExportReqVO exportReqVO) {
        return outwardContractMapper.selectList(exportReqVO);
    }

}

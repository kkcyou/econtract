package com.yaoan.module.system.service.econtractorg;

import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yaoan.module.system.controller.admin.econtractorg.vo.*;
import com.yaoan.module.system.dal.dataobject.econtractorg.EcontractOrgDO;
import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.system.convert.econtractorg.EcontractOrgConvert;
import com.yaoan.module.system.dal.mysql.econtractorg.EcontractOrgMapper;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;

/**
 * 电子合同单位信息 Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class EcontractOrgServiceImpl implements EcontractOrgService {

    @Resource
    private EcontractOrgMapper econtractOrgMapper;

    @Override
    public String createEcontractOrg(EcontractOrgSaveReqVO createReqVO) {
        // 插入
        EcontractOrgDO econtractOrg = EcontractOrgConvert.INSTANCE.convert(createReqVO);
        econtractOrgMapper.insert(econtractOrg);
        // 返回
        return econtractOrg.getId();
    }

    @Override
    public void saveEcontractOrg(EcontractOrgSaveReqVO saveReqVO) {
        EcontractOrgDO econtractOrg = EcontractOrgConvert.INSTANCE.convert(saveReqVO);
        econtractOrgMapper.saveOrUpdateBatch(Arrays.asList(econtractOrg));
    }

    @Override
    public void updateEcontractOrg(EcontractOrgSaveReqVO updateReqVO) {
        // 校验存在
        validateEcontractOrgExists(updateReqVO.getId());
        // 更新
        EcontractOrgDO updateObj = EcontractOrgConvert.INSTANCE.convert(updateReqVO);
        econtractOrgMapper.updateById(updateObj);
    }

    @Override
    public void deleteEcontractOrg(String id) {
        // 校验存在
        validateEcontractOrgExists(id);
        // 删除
        econtractOrgMapper.deleteById(id);
    }

    private void validateEcontractOrgExists(String id) {
        if (econtractOrgMapper.selectById(id) == null) {
            throw exception(DIY_ERROR, "数据不存在");
        }
    }

    @Override
    public EcontractOrgDO getEcontractOrg(String id) {
        return econtractOrgMapper.selectById(id);
    }

    @Override
    public List<EcontractOrgDO> getEcontractOrgList(Collection<String> ids) {
        return econtractOrgMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<EcontractOrgDO> getEcontractOrgPage(EcontractOrgPageReqVO pageReqVO) {
        return econtractOrgMapper.selectPage(pageReqVO);
    }

    @Override
    public List<EcontractOrgDO> getEcontractOrgList(EcontractOrgExportReqVO exportReqVO) {
        return econtractOrgMapper.selectList(exportReqVO);
    }

}

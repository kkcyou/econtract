package com.yaoan.module.system.service.user;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.contract.ContractProcessApi;
import com.yaoan.module.system.api.user.dto.SupplyDTO;
import com.yaoan.module.system.controller.admin.gcy.supplier.vo.SupplierCheckVo;
import com.yaoan.module.system.controller.admin.gcy.supplier.vo.SupplierInfoVo;
import com.yaoan.module.system.controller.admin.gcy.supplier.vo.SupplierPageReqVo;
import com.yaoan.module.system.convert.gcy.supplier.SupplierConvert;
import com.yaoan.module.system.convert.user.SupplyConvert;
import com.yaoan.module.system.dal.dataobject.user.SupplyDO;
import com.yaoan.module.system.dal.mysql.user.SupplyMapper;
import com.yaoan.module.system.enums.ErrorCodeConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * Admin 用户 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class SupplyServiceImpl implements SupplyService {
    @Resource
    private SupplyMapper supplyMapper;

    @Resource
    private ContractProcessApi contractProcessApi;

    @Value("${feign.client.contract.client_id}")
    private String clientId;
    @Value("${feign.client.contract.client_secret}")
    private String clientSecret;

    @Override
    public SupplyDTO getSupply(String id) {
        SupplyDO supplyDO = supplyMapper.selectById(id);
        SupplyDTO dto = SupplyConvert.INSTANCE.toDTO(supplyDO);
        return dto;
    }

    @Override
    public List<SupplyDTO> getSupplyByIds(List<String> ids) {
        if (CollectionUtils.isNotEmpty(ids)) {
            List<SupplyDO> supplyDOS = supplyMapper.selectBatchIds(ids);
            return SupplyConvert.INSTANCE.toDTOS(supplyDOS);
        }
        return null;
    }

    @Override
    public List<SupplyDTO> getSupplyByIdsAndName(List<String> supplierIds, String supplierName) {
        List<SupplyDO> supplyDOS = supplyMapper.getSupplyByIdsAndName(supplierIds, supplierName);
        if (CollectionUtil.isNotEmpty(supplyDOS)) {
            return SupplyConvert.INSTANCE.toDTOS(supplyDOS);
        }
        return Collections.emptyList();
    }

    @Override
    public SupplyDTO getSupplyByName(String supplierName) {
        SupplyDO supplyDO = supplyMapper.selectOne(SupplyDO::getSupplyCn,supplierName);
        SupplyDTO dto = SupplyConvert.INSTANCE.toDTO(supplyDO);
        return dto;
    }

    public SupplierInfoVo check(SupplierCheckVo reqVO) {
        SupplyDO supplyDO = supplyMapper.selectOne(SupplyDO::getSupplyCn, reqVO.getSupplyCn(), SupplyDO::getOrgCode, reqVO.getOrgCode());
        if (supplyDO == null) {
            throw exception(ErrorCodeConstants.UN_USED_DATA_ERROR);
        }
        return SupplierConvert.INSTANCE.convert2Vo(supplyDO);
    }

    public PageResult<SupplyDO> getSupplierPage(SupplierPageReqVo reqVO) {
        return supplyMapper.selectPage(reqVO);
    }
}

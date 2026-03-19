package com.yaoan.module.system.api.user;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.module.system.api.user.dto.SupplyDTO;
import com.yaoan.module.system.convert.user.SupplyConvert;
import com.yaoan.module.system.dal.dataobject.user.SupplyDO;
import com.yaoan.module.system.dal.mysql.user.SupplyMapper;
import com.yaoan.module.system.service.user.SupplyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;


/**
 * Admin 用户 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class SupplyApiImpl implements SupplyApi {

    @Resource
    private SupplyService supplyService;

    @Resource
    private SupplyMapper supplyMapper;

    @Override
    public SupplyDTO getSupply(String id) {
        SupplyDTO supply = supplyService.getSupply(id);
        return supply;
    }

    @Override
    public void insertOrUpdateBatch(List<SupplyDTO> supplyDTOList) {
        List<SupplyDO> supplyDOList = SupplyConvert.INSTANCE.convertList(supplyDTOList);
        supplyMapper.saveOrUpdateBatch(supplyDOList);
    }

    @Override
    public List<SupplyDTO> getSupplyByIds(List<String> ids) {
        List<SupplyDTO> supplyDOList = supplyService.getSupplyByIds(ids);
        return supplyDOList;
    }

    @Override
    public List<SupplyDTO> getSupplyByIdsAndName(List<String> supplierIds, String supplierName) {
        List<SupplyDTO> supplyDTOList = supplyService.getSupplyByIdsAndName(supplierIds, supplierName);
        if (CollectionUtil.isNotEmpty(supplyDTOList)) {
            return supplyDTOList;
        }
        return Collections.emptyList();
    }

    @Override
    public SupplyDTO getSupplyByName(String supplierName) {
        SupplyDTO supply = supplyService.getSupplyByName(supplierName);
        return supply;
    }
}

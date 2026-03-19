package com.yaoan.module.system.api.user;

import com.yaoan.module.system.api.user.dto.SupplyDTO;

import java.util.List;

/**
 * 供应商 用户 API 接口
 *
 * @author 芋道源码
 */
public interface SupplyApi {



    /**
     * 通过供应商 ID 查询供应商信息
     *
     * @param id 供应商 ID
     * @return 查询供应商信息
     */
    SupplyDTO getSupply(String id);

    /**
     * 批量插入修改数据
     */
    void insertOrUpdateBatch(List<SupplyDTO> supplyDTOList);

    /**
     * 通过供应商id集合 查询供应商信息
     */
    List<SupplyDTO> getSupplyByIds(List<String> ids);

    List<SupplyDTO> getSupplyByIdsAndName(List<String> supplierIds,String supplierName);

    SupplyDTO getSupplyByName(String supplierName);
}

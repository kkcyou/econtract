package com.yaoan.module.econtract.service.receive.business;


import cn.hutool.json.JSONArray;
import com.yaoan.module.econtract.api.purchasing.dto.ReqIdsDTO;

/**
 * 项目采购对接接口
 */
public interface IReceiveBusinessService {
    /**
     * 推送标识id
     *
     * @param id
     * @param type
     */
    void sendId(String id, Integer type, String deptId, String tenantId, String createUser);

    /**
     * 根据推送的项目idS标识获取项目采购信息-批量
     *
     * @param reqIdsDTO
     * @return
     */
    JSONArray queryPurchasingByIds(ReqIdsDTO reqIdsDTO);

    /**
     * 根据推送的项目idS标识获取框架协议采购信息-批量
     *
     * @param ids
     * @return
     */
    JSONArray queryFrameworkByIds(ReqIdsDTO ids);

    /**
     * 根据推送的项目idS标识获取电子卖场信息-批量
     *
     * @param ids
     * @return
     */
    JSONArray queryElectronicsStoreByIds(ReqIdsDTO ids);
}

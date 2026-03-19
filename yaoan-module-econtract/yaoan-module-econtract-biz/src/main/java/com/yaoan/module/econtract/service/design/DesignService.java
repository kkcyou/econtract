package com.yaoan.module.econtract.service.design;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractRespVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoCreateReqVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoPageReqVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.design.vo.ContractInfoVO;
import com.yaoan.module.econtract.dal.dataobject.demo.EcmsDemo;

/**
 * <p>
 * design 服务类
 * </p>
 *
 * @author doujl
 * @since 2023-07-04
 */
public interface DesignService {

    /**
     * 根据ID获取合同详情信息
     * @param id 合同ID
     * @return detailInfo
     */
    ContractInfoVO getContractDetailById(String id);

    /**
     * do something and send event
     * @param id 合同ID
     */
    void touchContract(String id);
}

package com.yaoan.module.econtract.service.businessform;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.businessform.vo.BusinessFormPageReqVO;
import com.yaoan.module.econtract.controller.admin.businessform.vo.BusinessFormSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.businessform.BusinessFormDO;

import javax.validation.Valid;


/**
 * 业务表单 Service 接口
 *
 * @author lls
 */
public interface BusinessFormService {

    /**
     * 创建业务表单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createBusinessForm(@Valid BusinessFormSaveReqVO createReqVO);

    /**
     * 更新业务表单
     *
     * @param updateReqVO 更新信息
     */
    void updateBusinessForm(@Valid BusinessFormSaveReqVO updateReqVO);

    /**
     * 删除业务表单
     *
     * @param id 编号
     */
    void deleteBusinessForm(String id);

    /**
     * 获得业务表单
     *
     * @param id 编号
     * @return 业务表单
     */
    BusinessFormDO getBusinessForm(String id);

    /**
     * 获得业务表单分页
     *
     * @param pageReqVO 分页查询
     * @return 业务表单分页
     */
    PageResult<BusinessFormDO> getBusinessFormPage(BusinessFormPageReqVO pageReqVO);

}
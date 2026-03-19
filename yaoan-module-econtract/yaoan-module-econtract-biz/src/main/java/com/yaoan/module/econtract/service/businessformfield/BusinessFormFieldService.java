package com.yaoan.module.econtract.service.businessformfield;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.businessformfield.vo.BusinessFormFieldPageReqVO;
import com.yaoan.module.econtract.controller.admin.businessformfield.vo.BusinessFormFieldSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.businessformfield.BusinessFormFieldDO;

import javax.validation.Valid;


/**
 * 表单字段 Service 接口
 *
 * @author lls
 */
public interface BusinessFormFieldService {

    /**
     * 创建表单字段
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createBusinessFormField(@Valid BusinessFormFieldSaveReqVO createReqVO);

    /**
     * 更新表单字段
     *
     * @param updateReqVO 更新信息
     */
    void updateBusinessFormField(@Valid BusinessFormFieldSaveReqVO updateReqVO);

    /**
     * 删除表单字段
     *
     * @param id 编号
     */
    void deleteBusinessFormField(String id);

    /**
     * 获得表单字段
     *
     * @param id 编号
     * @return 表单字段
     */
    BusinessFormFieldDO getBusinessFormField(String id);

    /**
     * 获得表单字段分页
     *
     * @param pageReqVO 分页查询
     * @return 表单字段分页
     */
    PageResult<BusinessFormFieldDO> getBusinessFormFieldPage(BusinessFormFieldPageReqVO pageReqVO);

}
package com.yaoan.module.econtract.service.businesstype;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.businesstype.vo.BusinessTypePageReqVO;
import com.yaoan.module.econtract.controller.admin.businesstype.vo.BusinessTypeSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.businesstype.BusinessTypeDO;

import javax.validation.Valid;


/**
 * 业务类型 Service 接口
 *
 * @author lls
 */
public interface BusinessTypeService {

    /**
     * 创建业务类型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createBusinessType(@Valid BusinessTypeSaveReqVO createReqVO);

    /**
     * 更新业务类型
     *
     * @param updateReqVO 更新信息
     */
    void updateBusinessType(@Valid BusinessTypeSaveReqVO updateReqVO);

    /**
     * 删除业务类型
     *
     * @param id 编号
     */
    void deleteBusinessType(String id);

    /**
     * 获得业务类型
     *
     * @param id 编号
     * @return 业务类型
     */
    BusinessTypeDO getBusinessType(String id);

    /**
     * 获得业务类型分页
     *
     * @param pageReqVO 分页查询
     * @return 业务类型分页
     */
    PageResult<BusinessTypeDO> getBusinessTypePage(BusinessTypePageReqVO pageReqVO);

}
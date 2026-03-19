package com.yaoan.module.econtract.service.businessroleformfield;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.businessroleformfield.vo.BusinessRoleFormFieldBatchSaveReqVO;
import com.yaoan.module.econtract.controller.admin.businessroleformfield.vo.BusinessRoleFormFieldPageReqVO;
import com.yaoan.module.econtract.controller.admin.businessroleformfield.vo.BusinessRoleFormFieldReqVO;
import com.yaoan.module.econtract.controller.admin.businessroleformfield.vo.BusinessRoleFormFieldSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.businessroleformfield.BusinessRoleFormFieldDO;

import javax.validation.Valid;
import java.util.List;


/**
 * 角色字段关系 Service 接口
 *
 * @author lls
 */
public interface BusinessRoleFormFieldService {

    /**
     * 创建角色字段关系
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createBusinessRoleFormField(@Valid BusinessRoleFormFieldSaveReqVO createReqVO);

    String batchCreateBusinessRoleFormField(@Valid BusinessRoleFormFieldBatchSaveReqVO createReqVO);


    /**
     * 更新角色字段关系
     *
     * @param updateReqVO 更新信息
     */
    void updateBusinessRoleFormField(@Valid BusinessRoleFormFieldSaveReqVO updateReqVO);

    /**
     * 删除角色字段关系
     *
     * @param id 编号
     */
    void deleteBusinessRoleFormField(String id);

    /**
     * 获得角色字段关系
     *
     * @param id 编号
     * @return 角色字段关系
     */
    BusinessRoleFormFieldDO getBusinessRoleFormField(String id);

    /**
     * 获得角色字段关系分页
     *
     * @param pageReqVO 分页查询
     * @return 角色字段关系分页
     */
    PageResult<BusinessRoleFormFieldDO> getBusinessRoleFormFieldPage(BusinessRoleFormFieldPageReqVO pageReqVO);

    /**
     * 获得角色和业务获取字段列表
     */
    List<BusinessRoleFormFieldDO> getFieldsByBusinessTypeAndRole(BusinessRoleFormFieldReqVO reqVO);

}
package com.yaoan.module.econtract.service.businessformfield;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.businessformfield.vo.BusinessFormFieldPageReqVO;
import com.yaoan.module.econtract.controller.admin.businessformfield.vo.BusinessFormFieldSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.businessformfield.BusinessFormFieldDO;
import com.yaoan.module.econtract.dal.mysql.businessformfield.BusinessFormFieldMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;


/**
 * 表单字段 Service 实现类
 *
 * @author lls
 */
@Service
@Validated
public class BusinessFormFieldServiceImpl implements BusinessFormFieldService {

    @Resource
    private BusinessFormFieldMapper businessFormFieldMapper;

    @Override
    public String createBusinessFormField(BusinessFormFieldSaveReqVO createReqVO) {
        // 插入
        BusinessFormFieldDO businessFormField = BeanUtils.toBean(createReqVO, BusinessFormFieldDO.class);
        businessFormFieldMapper.insert(businessFormField);
        // 返回
        return businessFormField.getId();
    }

    @Override
    public void updateBusinessFormField(BusinessFormFieldSaveReqVO updateReqVO) {
        // 校验存在
        validateBusinessFormFieldExists(updateReqVO.getId());
        // 更新
        BusinessFormFieldDO updateObj = BeanUtils.toBean(updateReqVO, BusinessFormFieldDO.class);
        businessFormFieldMapper.updateById(updateObj);
    }

    @Override
    public void deleteBusinessFormField(String id) {
        // 校验存在
        validateBusinessFormFieldExists(id);
        // 删除
        businessFormFieldMapper.deleteById(id);
    }

    private void validateBusinessFormFieldExists(String id) {
        if (businessFormFieldMapper.selectById(id) == null) {
            throw exception(SYSTEM_ERROR,"数据不存在");
        }
    }

    @Override
    public BusinessFormFieldDO getBusinessFormField(String id) {
        return businessFormFieldMapper.selectById(id);
    }

    @Override
    public PageResult<BusinessFormFieldDO> getBusinessFormFieldPage(BusinessFormFieldPageReqVO pageReqVO) {
        return businessFormFieldMapper.selectPage(pageReqVO);
    }

}
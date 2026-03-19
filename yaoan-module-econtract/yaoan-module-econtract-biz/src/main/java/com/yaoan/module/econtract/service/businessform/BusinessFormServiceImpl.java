package com.yaoan.module.econtract.service.businessform;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.businessform.vo.BusinessFormPageReqVO;
import com.yaoan.module.econtract.controller.admin.businessform.vo.BusinessFormSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.businessform.BusinessFormDO;
import com.yaoan.module.econtract.dal.mysql.businessform.BusinessFormMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;

/**
 * 业务表单 Service 实现类
 *
 * @author lls
 */
@Service
@Validated
public class BusinessFormServiceImpl implements BusinessFormService {

    @Resource
    private BusinessFormMapper businessFormMapper;

    @Override
    public String createBusinessForm(BusinessFormSaveReqVO createReqVO) {
        // 插入
        BusinessFormDO businessForm = BeanUtils.toBean(createReqVO, BusinessFormDO.class);
        businessFormMapper.insert(businessForm);
        // 返回
        return businessForm.getId();
    }

    @Override
    public void updateBusinessForm(BusinessFormSaveReqVO updateReqVO) {
        // 校验存在
        validateBusinessFormExists(updateReqVO.getId());
        // 更新
        BusinessFormDO updateObj = BeanUtils.toBean(updateReqVO, BusinessFormDO.class);
        businessFormMapper.updateById(updateObj);
    }

    @Override
    public void deleteBusinessForm(String id) {
        // 校验存在
        validateBusinessFormExists(id);
        // 删除
        businessFormMapper.deleteById(id);
    }

    private void validateBusinessFormExists(String id) {
        if (businessFormMapper.selectById(id) == null) {
            throw exception(SYSTEM_ERROR,"数据不存在");
        }
    }

    @Override
    public BusinessFormDO getBusinessForm(String id) {
        return businessFormMapper.selectById(id);
    }

    @Override
    public PageResult<BusinessFormDO> getBusinessFormPage(BusinessFormPageReqVO pageReqVO) {
        return businessFormMapper.selectPage(pageReqVO);
    }

}
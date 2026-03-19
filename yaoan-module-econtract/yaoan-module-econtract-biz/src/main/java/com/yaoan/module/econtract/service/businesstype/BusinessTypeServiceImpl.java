package com.yaoan.module.econtract.service.businesstype;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.businesstype.vo.BusinessTypePageReqVO;
import com.yaoan.module.econtract.controller.admin.businesstype.vo.BusinessTypeSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.businesstype.BusinessTypeDO;
import com.yaoan.module.econtract.dal.mysql.businesstype.BusinessTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;


/**
 * 业务类型 Service 实现类
 *
 * @author lls
 */
@Service
@Validated
public class BusinessTypeServiceImpl implements BusinessTypeService {

    @Resource
    private BusinessTypeMapper businessTypeMapper;

    @Override
    public String createBusinessType(BusinessTypeSaveReqVO createReqVO) {
        // 插入
        BusinessTypeDO businessType = BeanUtils.toBean(createReqVO, BusinessTypeDO.class);
       // businessType.setId(businessType.getCode());
        businessTypeMapper.insert(businessType);
        // 返回
        return businessType.getId();
    }

    @Override
    public void updateBusinessType(BusinessTypeSaveReqVO updateReqVO) {
        // 校验存在
        validateBusinessTypeExists(updateReqVO.getId());
        // 更新
        BusinessTypeDO updateObj = BeanUtils.toBean(updateReqVO, BusinessTypeDO.class);
        businessTypeMapper.updateById(updateObj);
    }

    @Override
    public void deleteBusinessType(String id) {
        // 校验存在
        validateBusinessTypeExists(id);
        // 删除
        businessTypeMapper.deleteById(id);
    }

    private void validateBusinessTypeExists(String id) {
        if (businessTypeMapper.selectById(id) == null) {
            throw exception(SYSTEM_ERROR,"数据不存在");
        }
    }

    @Override
    public BusinessTypeDO getBusinessType(String id) {
        return businessTypeMapper.selectById(id);
    }

    @Override
    public PageResult<BusinessTypeDO> getBusinessTypePage(BusinessTypePageReqVO pageReqVO) {
        return businessTypeMapper.selectPage(pageReqVO);
    }

}
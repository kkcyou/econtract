package com.yaoan.module.econtract.service.businessfile;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.common.exception.ErrorCode;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.businessfile.vo.BusinessFilePageReqVO;
import com.yaoan.module.econtract.controller.admin.businessfile.vo.BusinessFileSaveReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.BusinessFileVO;
import com.yaoan.module.econtract.convert.businessfile.BusinessFileConverter;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.mysql.businessfile.BusinessFileMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;


/**
 * 业务数据和附件关联关系 Service 实现类
 *
 * @author lls
 */
@Service
@Validated
public class BusinessFileServiceImpl implements BusinessFileService {

    @Resource
    private BusinessFileMapper businessFileMapper;

    @Override
    public String createBusinessFile(BusinessFileSaveReqVO createReqVO) {
        // 插入
        BusinessFileDO businessFile = BeanUtils.toBean(createReqVO, BusinessFileDO.class);
        businessFileMapper.insert(businessFile);
        // 返回
        return businessFile.getId();
    }

    @Override
    public void createBatchBusinessFile(String businessId, List<BusinessFileVO> fileVOList) {
        if (CollectionUtil.isNotEmpty(fileVOList)) {
            List<BusinessFileSaveReqVO> createReqVO = new ArrayList<BusinessFileSaveReqVO>();
            for (BusinessFileVO fileVO : fileVOList) {
                BusinessFileSaveReqVO fileVo = new BusinessFileSaveReqVO().setBusinessId(businessId).setFileId(fileVO.getFileId()).setFileName(fileVO.getFileName());
                createReqVO.add(fileVo);
            }
            List<BusinessFileDO> list = BusinessFileConverter.INSTANCE.listR2D(createReqVO);
            businessFileMapper.insertBatch(list);
        }
    }

    @Override
    public void updateBusinessFile(BusinessFileSaveReqVO updateReqVO) {
        // 校验存在
        validateBusinessFileExists(updateReqVO.getId());
        // 更新
        BusinessFileDO updateObj = BeanUtils.toBean(updateReqVO, BusinessFileDO.class);
        businessFileMapper.updateById(updateObj);
    }

    @Override
    public void deleteBusinessFile(String id) {
        // 校验存在
        validateBusinessFileExists(id);
        // 删除
        businessFileMapper.deleteById(id);
    }

    private void validateBusinessFileExists(String id) {
        if (businessFileMapper.selectById(id) == null) {
            throw exception(new ErrorCode(500, "不存在"));
        }
    }

    @Override
    public BusinessFileDO getBusinessFile(String id) {
        return businessFileMapper.selectById(id);
    }

    @Override
    public PageResult<BusinessFileDO> getBusinessFilePage(BusinessFilePageReqVO pageReqVO) {
        return businessFileMapper.selectPage(pageReqVO);
    }

    @Override
    public void deleteByBusinessId(String id) {
        businessFileMapper.deleteByBusinessId(id);
    }

    @Override
    public List<BusinessFileDO> selectListByBusiness(String id) {
        List<BusinessFileDO> rs = businessFileMapper.selectByBusinessId(id);
        if (CollectionUtil.isNotEmpty(rs)) {
            return rs;
        }
        return Collections.emptyList();
    }

}
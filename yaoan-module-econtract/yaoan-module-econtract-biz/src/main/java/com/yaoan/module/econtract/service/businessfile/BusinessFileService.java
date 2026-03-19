package com.yaoan.module.econtract.service.businessfile;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.businessfile.vo.BusinessFilePageReqVO;
import com.yaoan.module.econtract.controller.admin.businessfile.vo.BusinessFileSaveReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.BusinessFileVO;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;

import javax.validation.Valid;
import java.util.List;


/**
 * 业务数据和附件关联关系 Service 接口
 *
 * @author lls
 */
public interface BusinessFileService {

    /**
     * 创建业务数据和附件关联关系
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createBusinessFile(@Valid BusinessFileSaveReqVO createReqVO);

    /**
     * 创建业务数据和附件关联关系
     */
    void createBatchBusinessFile(String businessId,List<BusinessFileVO> fileVOs);

    /**
     * 更新业务数据和附件关联关系
     *
     * @param updateReqVO 更新信息
     */
    void updateBusinessFile(@Valid BusinessFileSaveReqVO updateReqVO);

    /**
     * 删除业务数据和附件关联关系
     *
     * @param id 编号
     */
    void deleteBusinessFile(String id);

    /**
     * 获得业务数据和附件关联关系
     *
     * @param id 编号
     * @return 业务数据和附件关联关系
     */
    BusinessFileDO getBusinessFile(String id);

    /**
     * 获得业务数据和附件关联关系分页
     *
     * @param pageReqVO 分页查询
     * @return 业务数据和附件关联关系分页
     */
    PageResult<BusinessFileDO> getBusinessFilePage(BusinessFilePageReqVO pageReqVO);

    /**
     * 删除业务数据和附件关联关系
     *
     * @param id 编号
     */
    void deleteByBusinessId(String id);



    List<BusinessFileDO> selectListByBusiness(String id);
}
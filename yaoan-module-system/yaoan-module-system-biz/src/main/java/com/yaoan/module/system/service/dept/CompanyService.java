package com.yaoan.module.system.service.dept;

import com.yaoan.module.system.controller.admin.dept.vo.company.*;
import com.yaoan.module.system.controller.admin.dept.vo.saas.CompanySimpleSaveReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.saas.SaasCompanyRespVO;
import com.yaoan.module.system.dal.dataobject.dept.CompanyDO;

import java.util.Collection;
import java.util.List;

/**
 * 单位 Service 接口
 *
 * @author 芋道源码
 */
public interface CompanyService {

    /**
     * 创建单位
     *
     * @param reqVO 单位信息
     * @return 单位编号
     */
    Long createCompany(CompanyCreateReqVO reqVO);

    /**
     * 创建供应商公司
     * @param reqVO
     * @return
     */
    Long createSupplyCompany(CompanyCreateReqVO reqVO);
    Long createCompanyWithTenantId(CompanyCreateReqVO reqVO, Long tenantId);

    /**
     * 更新单位
     *
     * @param reqVO 单位信息
     */
    void updateCompany(CompanyUpdateReqVO reqVO);

    /**
     * 删除单位
     *
     * @param id 单位编号
     */
    void deleteCompany(Long id);

    /**
     * 获得单位信息
     *
     * @param id 单位编号
     * @return 单位信息
     */
    CompanyRespVO getCompany(Long id);

    /**
     * 获得单位信息数组
     *
     * @param ids 单位编号数组
     * @return 单位信息数组
     */
    List<CompanyDO> getCompanyList(Collection<Long> ids);

    /**
     * 筛选单位列表
     *
     * @param reqVO 筛选条件请求 VO
     * @return 单位列表
     */
    List<CompanyDO> getCompanyList(CompanyListReqVO reqVO);


    /**
     * 根据公司名称获取公司id
     */
    Long getCompanyIdByName(String name);

    /**
     * 信用代码
     * */
    void checkCreditCode(String creditCode);

    Long save(CompanySimpleSaveReqVO reqVO);

    String acheRelativeId(String relativeId);

    List<SaasCompanyRespVO> list4login();
}

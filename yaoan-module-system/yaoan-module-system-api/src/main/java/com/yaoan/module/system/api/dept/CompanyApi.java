package com.yaoan.module.system.api.dept;

import com.yaoan.module.system.api.dept.dto.CompanyRespDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyAllInfoRespDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyInfoRespDTO;

import java.util.Collection;
import java.util.List;

/**
 * 单位 API 接口
 *
 * @author doujl
 */
public interface CompanyApi {

    /**
     * 获得人员的单位信息数组
     *
     * @param userIds 用户ids
     * @return 单位信息数组
     */
    List<UserCompanyInfoRespDTO> getUserCompanyInfoList(Collection<Long> userIds);

    /**
     *
     * 获得人员的单位信息数组  包括单位和企业
     * @param userIds 用户ids
     * @return 单位和企业信息数组
     */
    List<UserCompanyInfoRespDTO> getUserCompanyInfo(List<Long> userIds);
    List<UserCompanyAllInfoRespDTO> getUserCompanyAllInfoList(Collection<Long> userIds);
    /**
     * 根据部门id获得人员的单位信息
     *
     * @param deptId 部门id
     * @return 单位信息数组
     */
    CompanyRespDTO getCompany(Long deptId, Integer status);

    /**
     * 根据单位名称和统一社会信用代码获得人员的单位信息
     *
     * @param deptId 部门id
     * @return 单位信息数组
     */
    CompanyRespDTO getCompany(String name, String creditCode, Integer status);

    /**
     * 根据用户ids
     * 找到单位
     */
    List<CompanyRespDTO> getCompanyByIds(List<Long> userIds);

    /**
     * 修改公司负责人id
     */
    void updateCompanyLeaderUserId(String relativeId, Long leaderUserId);

    CompanyRespDTO getOneById(Long companyId);

    CompanyRespDTO getCompany4CreditCode(String creditCode);
}

package com.yaoan.module.system.api.dept.dto;

import com.yaoan.framework.common.enums.CommonStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 用户单位 Response DTO
 *
 * @author doujiale
 */
@Data
public class UserCompanyDeptRespDTO {

    /**
     * 用户ID
     */
    private Long id;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户帐号状态
     */
    private Integer status;
    /**
     * 身份证
     */
    private String idCard;

    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 岗位编号数组
     */
    private Set<Long> postIds;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 用户账号
     */
    private String username;

    /**
     * 公司信息
     */
    private CompanyRespDTO companyInfo;
    /**
     * 部门信息
     */
    private DeptRespDTO deptInfo;
    /**
     * 单位id
     */
    private String orgId;
}

package com.yaoan.module.system.api.user;

import com.yaoan.module.system.api.user.dto.EcontractOrgDTO;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;

import java.util.List;

/**
 * 单位 用户 API 接口
 *
 * @author zhc
 */
public interface EcontractOrgApi {
    EcontractOrgDTO getEcontractOrgById(String id);

    void saveContractOrg(EcontractOrgDTO econOrgDTO);
}

package com.yaoan.module.system.controller.admin.dept.vo.company;

import com.yaoan.module.system.api.auth.dto.AuthLoginRespDTO;
import lombok.Data;

@Data
public class SaasCompanySaveRespVO {

    private Long companyId;

    private AuthLoginRespDTO authLoginRespDTO;
}

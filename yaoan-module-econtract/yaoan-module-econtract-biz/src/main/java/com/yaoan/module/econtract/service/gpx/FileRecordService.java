package com.yaoan.module.econtract.service.gpx;

import com.yaoan.module.econtract.controller.admin.gpx.vo.file.PermissionRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.file.SaveFileAndCompanyReqVO;

/**
 * @description:
 * @author: Pele
 * @date: 2024/7/4 16:25
 */
public interface FileRecordService {
    String saveFileAndCompanyInfo(SaveFileAndCompanyReqVO contractPageReqVO);

    String deleteFileAndCompanyInfo(String fileId);

    PermissionRespVO getPermission(String contractId);

    Boolean ifPass(String contractId);

    Boolean ifPass1(String id);
}

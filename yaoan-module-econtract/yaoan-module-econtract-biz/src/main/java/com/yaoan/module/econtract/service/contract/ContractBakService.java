package com.yaoan.module.econtract.service.contract;

import com.yaoan.module.econtract.api.contract.dto.FileAcceptDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface ContractBakService {
    void contractBak(String id);
    /**
     * 附件信息上传
     */
    FileAcceptDTO uploaderFile(MultipartFile file, String accessToken, HttpServletRequest request, String platform, String contractId) throws Exception;

}

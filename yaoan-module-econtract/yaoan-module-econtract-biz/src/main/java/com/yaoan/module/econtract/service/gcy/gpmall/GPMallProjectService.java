package com.yaoan.module.econtract.service.gcy.gpmall;

import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.CancellationFileVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/4 11:58
 */
public interface GPMallProjectService {

    /** 暂时按照此逻辑，绕开黑龙江交互(亚冬会结束后就删) */
    String cancelContract(CancellationFileVO vo) throws Exception;

    /** 暂时按照此逻辑，绕开黑龙江交互(亚冬会结束后就删) */
    Boolean invalidateContractById(String id, MultipartFile file) throws Exception;
}

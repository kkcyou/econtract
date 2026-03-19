package com.yaoan.module.econtract.service.gpx.contract;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractRespVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface GPXContractService {

    void deleteById(String id);

    String create(GPXContractCreateReqVO contractCreateReqVO) throws Exception;

    GPXContractRespVO queryById(String id) throws Exception;
    /**
     * 电子交易作废接口
     *
     * @param id
     * @param file
     * @return {@link CommonResult }<{@link Boolean }>
     * @throws Exception
     */
    Boolean invalidateContractById(String id, MultipartFile file) throws Exception;

    String printContract(String id) throws Exception;

    void downloadTContract(HttpServletResponse response, String id) throws Exception;

}

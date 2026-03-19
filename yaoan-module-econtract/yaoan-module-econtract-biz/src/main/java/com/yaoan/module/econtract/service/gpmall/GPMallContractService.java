package com.yaoan.module.econtract.service.gpmall;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yaoan.module.econtract.api.contract.dto.ContractDataDTO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractDataVo;
import com.yaoan.module.econtract.controller.admin.contract.vo.UploadContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.CancellationFileVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.save.OrderContractCreateReqV2Vo;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/6/6 19:29
 */
public interface GPMallContractService {

    ContractDataDTO queryByOrderId(String id);

    /**
     * 电子卖场-保存合同
     *
     * @param gpMallContractCreateReqVO
     * @return
     */
    String saveContractAll(OrderContractCreateReqV2Vo gpMallContractCreateReqVO) throws JsonProcessingException;
    String pushContractToEcontract(String  id) throws JsonProcessingException;

    /**
     * 查看合同详情-黑龙江演示接口
     *
     * @param id
     * @return
     */
    ContractDataVo queryById(String id) throws Exception;
    /**
     * 查看合同详情-黑龙江对接接口
     *
     * @param id
     * @return
     */
    ContractDataVo queryByIdV2(String id);

    /**
     * 上传合同
     * @param vo
     * @return
     */
    String uploadContractCreateOrUpdate(UploadContractCreateReqVO vo) throws Exception;
    /**
     * 删除合同
     *
     * @param id
     */
    void deleteContractById(String id) throws Exception;

    UploadContractCreateReqVO getUploadContractById(String id) throws Exception;

    List<GPMallPageRespVO>  getOrderAndGoodsByOrderIds( List<String> orderList);

    ContractDataDTO queryByOrderIdV4(String id, String contractFrom) throws Exception;

    String cancelContract(CancellationFileVO vo) throws Exception;

    GPXContractRespVO queryById4Sign(String id);
}

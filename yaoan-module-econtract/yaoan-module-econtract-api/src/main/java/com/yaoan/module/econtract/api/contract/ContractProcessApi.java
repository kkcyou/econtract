package com.yaoan.module.econtract.api.contract;

import com.yaoan.module.econtract.api.contract.dto.*;
import com.yaoan.module.econtract.api.contract.dto.gcy.CancellationFileDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ModelCreateReqDTO;
import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfo;
import com.yaoan.module.econtract.api.model.dto.ModelDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 同步电子合同审批结果api
 */

public interface ContractProcessApi {

    /**
     * 获取电子合同code
     */
    String oauthCenterToken( Map<String, Object> paramMap,String grantType,String client_id,String client_secret,String username, String password);

    /**
     * 审批通过api
     *
     * @return
     */
    String sendContract( String accessToken, EncryptDTO encryptDTO);

    /**
     * 审批退回Api
     *
     * @return
     */
    String backContract( String accessToken,  EncryptDTO encryptDTO);


    /**
     * 合同同步电子合同（非交易）
     */
    String pushContractToEcontract( String accessToken, EncryptDTO encryptDTO);

    /**
     * 合同同步电子合同（电子交易）
     */
    String pushGPXContractToEcontract( String accessToken, EncryptDTO encryptDTO);

    /**
     * 上传文件到单位端
     *
     * @param accessToken
     * @return
     */
    String contractUpload( String accessToken, ContractFileDTO contractFileDTO);


    /**
     * 合同签署发送后同步到电子合同
     */
    String pushSignContract( String accessToken, EncryptDTO encryptDTO);

    /**
     * 合同模板同步黑龙江
     */
    String insertModelByUpload( String accessToken, ModelCreateReqDTO dto);

    /**
     * 合同状态同步黑龙江
     */
    String unitSyncContractStatus( String accessToken, SyncContractStatusDTO dto);

    /**
     * 合同作废状态同步黑龙江(交易合同)
     */
    String invalidateContractById( String accessToken,  IdReqDTO dto);

    /**
     * 合同作废状态同步黑龙江(非交易合同)
     */
    String cancelContract( String accessToken, CancellationFileDTO dto);

    /**
     * 订单状态修改 - 能否起草状态 status （待起草 已起草）
     */
    String updateOrderStatus( String accessToken,@RequestBody DraftOrderInfo draftOrderInfo);

    /**
     * 包状态修改 - 能否起草状态 hidden （待起草 已起草） --- 电子交易
     */
    String updateStatusPackage( String accessToken,@RequestBody PackageUpdateDTO packageUpdateDTO);

    /**
     * 单位端开通的单位信息同步黑龙江
     * @param accessToken
     * @param productOrgDTO
     * @return
     */
    String openProductOrg( String accessToken,@RequestBody ProductOrgDTO productOrgDTO);

    /**
     * 根据单位id从黑龙江拉取模板数据
     * @param accessToken
     * @return
     */
    List<ModelDTO> getModelListByOrgId(String accessToken, String orgId);


    /**
     * 根据单位id从黑龙江拉取模板数据
     * @param accessToken
     * @return
     */
    UserDTO getUserByOrgId(String accessToken, String orgId);


    /**
     * 根据供应商id从黑龙江查询供应商数据
     * @param accessToken
     * @return
     */
    SupplyFromHLJDTO getSupplyById(String accessToken, String orgId);
    List<SupplyFromHLJDTO> getSupplyByIds(String accessToken, List<String> orgId);

    EcontractOrgDTO getEcontractOrgById(String accessToken, String id);
}

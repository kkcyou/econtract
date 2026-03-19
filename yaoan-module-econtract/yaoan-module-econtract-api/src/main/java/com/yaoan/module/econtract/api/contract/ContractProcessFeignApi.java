package com.yaoan.module.econtract.api.contract;

import com.yaoan.module.econtract.api.contract.dto.*;
import com.yaoan.module.econtract.api.contract.dto.gcy.CancellationFileDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ModelCreateReqDTO;
import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @desc 同步电子合同审批结果api
 * @author lls
 */

@FeignClient(url = "${feign.client.contract.url}", name = "ContractApi")
public interface ContractProcessFeignApi {

    /**
     * 获取电子合同code
     */
    @PostMapping(value = "/system/oauth2-client/access/code")
    String oauthCenterToken(@RequestBody Map<String, Object> paramMap,
                            @RequestParam(value = "grant_type", required = false) String grantType,
                            @RequestParam(value = "client_id", required = false) String client_id,
                            @RequestParam(value = "client_secret", required = false) String client_secret,
                            @RequestParam(value = "username", required = false) String username,
                            @RequestParam(value = "password", required = false) String password);

    /**
     * 审批通过api
     *
     * @return
     */
    @PostMapping(value = "/econtract/contract/bpm/send")
    String sendContract(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO encryptDTO);

    /**
     * 审批退回Api
     *
     * @return
     */
    @PostMapping(value = "/econtract/contract/bpm/back")
    String backContract(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO encryptDTO);


    /**
     * 合同同步电子合同（非交易）
     */
    @PostMapping(value = "/econtract/contract/neimeng/save/encrypt")
    String pushContractToEcontract(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO encryptDTO);

    /**
     * 合同同步电子合同（电子交易）
     */
    @PostMapping(value = "/econtract/trading/contract/create/encrypt")
    String pushGPXContractToEcontract(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO encryptDTO);

    /**
     * 上传文件到单位端
     *
     * @param accessToken
     * @return
     */
    @PostMapping(value = "/infra/file/v3/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String contractUpload(@RequestHeader("access_token") String accessToken, @RequestBody ContractFileDTO contractFileDTO);


    /**
     * 合同签署发送后同步到电子合同
     */
    @PostMapping(value = "/econtract/contract/bpm/sign")
    String pushSignContract(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO encryptDTO);

    /**
     * 合同模板同步黑龙江
     */
    @PutMapping(value = "econtract/model/pushModel")
    String insertModelByUpload(@RequestHeader("access_token") String accessToken, @RequestBody ModelCreateReqDTO dto);

    /**
     * 合同状态同步黑龙江
     */
    @PostMapping(value = "econtract/contract/unitSyncContractStatus")
    String unitSyncContractStatus(@RequestHeader("access_token") String accessToken, @RequestBody SyncContractStatusDTO dto);

    /**
     * 合同作废状态同步黑龙江(交易合同)
     */
    @PostMapping(value = "trading/contract/invalidate4Agency")
    String invalidateContractById(@RequestHeader("access_token") String accessToken, @RequestBody  IdReqDTO dto);

    /**
     * 合同作废状态同步黑龙江(非交易合同)
     */
    @PostMapping(value = "/econtract/contract/cancelContract4Agency")
    String cancelContract(@RequestHeader("access_token") String accessToken, @RequestBody CancellationFileDTO dto);

    /**
     * 订单状态修改 - 能否起草状态 status （待起草 已起草）
     */
    @PostMapping(value = "/econtract/order/update/status")
    String updateOrderStatus(@RequestHeader("access_token") String accessToken,@RequestBody DraftOrderInfo draftOrderInfo);

    /**
     * 包状态修改 - 能否起草状态 hidden （待起草 已起草） --- 电子交易
     */
    @PostMapping(value = "/econtract/order/update/status/package")
    String updateStatusPackage(@RequestHeader("access_token") String accessToken,@RequestBody PackageUpdateDTO packageUpdateDTO);

    /**
     *  单位开通，新增或修改
     */
    @PostMapping(value = "/econtract/productorg/openProductOrg")
    String openProductOrg(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO dto);


    /**
     *  根据单位id从黑龙江拉取模板数据
     */
    @PostMapping(value = "/econtract/template/listByOrgId")
    String getModelListByOrgId(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO dto);

    /**
     *  根据单位id从黑龙江拉取一个启用的用户
     */
    @PostMapping(value = "/econtract/user/getByOrgId")
    String getUserByOrgId(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO dto);

    /**
     *  根据单位id从黑龙江拉取合同单位数据
     */
    @PostMapping(value = "/system/contract/org/queryById")
    String getEcontractOrgByOrgId(@RequestHeader("access_token") String accessToken, @RequestBody EcontractOrgDTO econtractOrgDTO);

    /**
     *  根据共供应商id从黑龙江查询相应信息
     */
    @PostMapping(value = "/econtract/systemsupply/getById")
    String getSupplyById(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO dto);
    /**
     *  根据共供应商id从黑龙江查询相应信息
     */
    @PostMapping(value = "/econtract/systemsupply/getByIds")
    String getSupplyByIds(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO dto);
}

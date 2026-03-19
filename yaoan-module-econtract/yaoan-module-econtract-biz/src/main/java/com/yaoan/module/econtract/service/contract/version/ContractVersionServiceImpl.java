package com.yaoan.module.econtract.service.contract.version;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yaoan.framework.common.exception.ErrorCode;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.api.contract.ContractProcessApi;
import com.yaoan.module.econtract.api.contract.dto.ContractProcessDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BigContractVersionListRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.ContractVersionListRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.ContractVersionSaveReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.convert.contractversion.ContractVersionConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.version.ContractVersionDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.version.ContractVersionMapper;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.util.gcy.Sm4Utils;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.Exceptions;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/29 11:26
 */
@Service
@Slf4j
public class ContractVersionServiceImpl implements ContractVersionService {

    private final Integer FIRST_VERSION = 1;
    @Resource
    private ContractVersionMapper contractVersionMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractOrderExtMapper contractOrderExtMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private ContractProcessApi contractProcessApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Value("${feign.client.contract.client_id}")
    private String clientId;
    @Value("${feign.client.contract.client_secret}")
    private String clientSecret;

    @Override
    public String save(ContractVersionSaveReqVO vo) {
        ContractVersionDO versionDO = ContractVersionConverter.INSTANCE.req2Entity(vo);
        List<ContractVersionDO> versionList = contractVersionMapper.selectList(new LambdaQueryWrapperX<ContractVersionDO>()
                .eqIfPresent(ContractVersionDO::getContractId, vo.getContractId())
                .orderByDesc(ContractVersionDO::getCreateTime)
        );
        ContractDO contractDO = contractMapper.selectById(vo.getContractId());
        if(ObjectUtil.isNotNull(contractDO)){
            versionDO.setFileId(String.valueOf(contractDO.getPdfFileId()));
            versionDO.setFileName(contractDO.getFileName());
        }

        if (CollectionUtil.isEmpty(versionList)) {
            //如果是首次申请
            versionDO.setVersionNumber(FIRST_VERSION);

        } else {
            //不是首次申请
            ContractVersionDO latestVersion = versionList.get(0);
            versionDO.setVersionNumber(latestVersion.getVersionNumber() + 1);

        }

        contractVersionMapper.insert(versionDO);
        return "success";
    }

    @Override
    public BigContractVersionListRespVO listByContractId(String contractId) {
        BigContractVersionListRespVO result = new BigContractVersionListRespVO();
        ContractDO contractDO = contractMapper.selectById(contractId);
        if (ObjectUtil.isNull(contractDO)) {
            return new BigContractVersionListRespVO();
        }
        Long fileId = contractDO.getPdfFileId();
        FileDTO fileDTO = fileApi.selectById(fileId);
        if (ObjectUtil.isNotNull(fileDTO)) {
            result.setContractName(fileDTO.getName());
        }
        List<ContractVersionDO> doList = contractVersionMapper.selectList(new LambdaQueryWrapperX<ContractVersionDO>().eqIfPresent(ContractVersionDO::getContractId, contractId).orderByDesc(ContractVersionDO::getCreateTime));
        if (CollectionUtil.isNotEmpty(doList)) {
            List<ContractVersionListRespVO> respVOList = ContractVersionConverter.INSTANCE.listEntity2Resp(doList);
            enhanceList(respVOList);
            result.setRespVOS(respVOList);
        }
        return result;
    }

    /**
     * 产品退回合同审批接口
     * @param vo
     * @return
     */
    @Override
    public String productBackContract(IdReqVO vo) {
        //合同退回
        if(StringUtils.isEmpty(vo.getReason())){
            throw new RuntimeException("退回原因不能为空");
        }
        ContractDO contractDO = contractMapper.selectById(vo.getId());
        if(ObjectUtil.isEmpty(contractDO)){
            throw new RuntimeException("合同不存在");
        }
        String oauthTokenStr = "";
        Map paramMap = new HashMap();
        paramMap.put("client_id", clientId);
        paramMap.put("client_secret", clientSecret);
        try{
            oauthTokenStr = contractProcessApi.oauthCenterToken(paramMap,null, null, null, null, null);
        }catch (Exception e ){
            throw exception(-1,"获取token异常，请检查账号配置信息");
        }
        JSONObject jsonObject = JSONObject.parseObject(oauthTokenStr);
        if (jsonObject.get("error") != null) {
            try {
                throw new Exception(jsonObject.getString("error_description"));
            } catch (Exception e) {
                throw new RuntimeException(jsonObject.getString("error_description"));
            }
        }
        EncryptDTO encryptDTO = null;
        try {
            encryptDTO   = Sm4Utils.convertParam(new ContractProcessDTO().setContractId(contractDO.getId()).setReason(vo.getReason()).setUpdaterStr(adminUserApi.getUser(getLoginUserId()).getNickname()));
            String result = contractProcessApi.backContract(jsonObject.getString("access_token"), encryptDTO);
            log.info("电子合同返回结果" +result );
            JSONObject resultJson = JSONObject.parseObject(result);
            if(!"0".equals(resultJson.getString("status"))){
                throw new RuntimeException("退回合同失败");
            }
//            EncryptDTO resultJson =  JSONObject.parseObject(Sm4Utils.decryptEcb(encryptDTO.getRequestParam()), EncryptDTO.class);
//            String contractResult = Sm4Utils.decryptEcb(resultJson.getRequestParam());
//            JSONObject statusResult = JSONObject.parseObject(contractResult);
//            if(!"0".equals(statusResult.getString("status"))){
//                throw new RuntimeException("退回合同失败");
//            }
           
        } catch (Exception e) {
            e.printStackTrace();
            throw exception(new ErrorCode(500,e.getMessage()));
        }
        // 退回合同，修改政采合同状态为已取消，视为供应商未推送改合同，避免合同起草时查不到该订单数据
        contractOrderExtMapper.update(null, new LambdaUpdateWrapper<ContractOrderExtDO>()
                .eq(ContractOrderExtDO::getId, vo.getId())
                .set(ContractOrderExtDO::getStatus,ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode()));

        contractMapper.update(null, new LambdaUpdateWrapper<ContractDO>()
                .eq(ContractDO::getId, vo.getId())
                .set(ContractDO::getStatus, ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode()));
        return "1";
    }

    private void enhanceList(List<ContractVersionListRespVO> respVOList) {
        for (ContractVersionListRespVO respVO : respVOList) {

        }
    }


}

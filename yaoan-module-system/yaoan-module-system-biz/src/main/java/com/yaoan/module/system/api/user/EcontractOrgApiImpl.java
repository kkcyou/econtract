package com.yaoan.module.system.api.user;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.system.api.user.dto.EcontractOrgDTO;
import com.yaoan.module.system.controller.admin.econtractorg.vo.EcontractOrgSaveReqVO;
import com.yaoan.module.system.convert.econtractorg.EcontractOrgConvert;
import com.yaoan.module.system.dal.dataobject.econtractorg.EcontractOrgDO;
import com.yaoan.module.system.service.econtractorg.EcontractOrgService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EcontractOrgApiImpl implements EcontractOrgApi{

    @Resource
    private EcontractOrgService econtractOrgService;

    @Override
    public EcontractOrgDTO getEcontractOrgById(String id) {
        // 从数据库查询单位数据
        EcontractOrgDO econtractOrg = econtractOrgService.getEcontractOrg(id);
        if (ObjectUtil.isNull(econtractOrg)){
            // 如果单位不存在，去政采端查询拉取单位数据
            ContractApi contractApi = SpringUtil.getBean(ContractApi.class);
            econtractOrg = BeanUtils.toBean(contractApi.getEcontractOrgFromZCById(id), EcontractOrgDO.class);
        }
        return EcontractOrgConvert.INSTANCE.convertDTO(econtractOrg);
    }

    @Override
    public void saveContractOrg(EcontractOrgDTO econOrgDTO) {
        EcontractOrgSaveReqVO saveReqVO =EcontractOrgConvert.INSTANCE.convertDTO2SaveVO(econOrgDTO);
        econtractOrgService.saveEcontractOrg(saveReqVO);
    }
}

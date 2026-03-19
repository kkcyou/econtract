package com.yaoan.module.econtract.service.contractaidraftshow;

import cn.hutool.core.bean.BeanUtil;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowCreateReqVO;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowRespShortVO;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowUpdateReqVO;
import com.yaoan.module.econtract.convert.contractaidraftshow.ContractAiDraftShowConvert;
import com.yaoan.module.econtract.dal.dataobject.contractaidraftshow.ContractAiDraftShowDO;
import com.yaoan.module.econtract.dal.mysql.contractaidraftshow.ContractAiDraftShowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;


/**
 * 合同模板推荐 Service 实现类
 *
 * @author doujiale
 */
@Service
@Validated
public class ContractAiDraftShowServiceImpl implements ContractAiDraftShowService {

    @Resource
    private ContractAiDraftShowMapper contractAiDraftShowMapper;

    @Override
    public ContractAiDraftShowRespShortVO createContractAiDraftShow(ContractAiDraftShowCreateReqVO createReqVO) {
        // 插入
        ContractAiDraftShowDO contractAiDraftShow = ContractAiDraftShowConvert.INSTANCE.convert(createReqVO);

        //base64解密
        contractAiDraftShow.setTemplateContent(new String(Base64Utils.decodeFromString(contractAiDraftShow.getTemplateContent())));

        contractAiDraftShowMapper.insert(contractAiDraftShow);

        // 返回
        return BeanUtil.copyProperties(contractAiDraftShow, ContractAiDraftShowRespShortVO.class);
    }

    @Override
    public void updateContractAiDraftShow(ContractAiDraftShowUpdateReqVO updateReqVO) {
        // 校验存在
        validateContractAiDraftShowExists(updateReqVO.getTemplateiId());
        // 更新
        ContractAiDraftShowDO updateObj = ContractAiDraftShowConvert.INSTANCE.convert(updateReqVO);
        contractAiDraftShowMapper.updateById(updateObj);
    }

    @Override
    public void deleteContractAiDraftShow(Long id) {
        // 校验存在
        validateContractAiDraftShowExists(id);
        // 删除
        contractAiDraftShowMapper.deleteById(id);
    }

    private void validateContractAiDraftShowExists(Long id) {
        if (contractAiDraftShowMapper.selectById(id) == null) {
            throw new RuntimeException("数据不存在!");
        }
    }

    @Override
    public ContractAiDraftShowDO getContractAiDraftShow(Long id) {
        return contractAiDraftShowMapper.selectById(id);
    }

}

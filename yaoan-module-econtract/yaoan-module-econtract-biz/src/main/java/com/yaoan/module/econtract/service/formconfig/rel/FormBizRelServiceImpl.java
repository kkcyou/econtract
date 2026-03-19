package com.yaoan.module.econtract.service.formconfig.rel;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.formconfig.formrelation.vo.FormInfoReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.formrelation.vo.FormRelSaveReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormBusinessRelDO;
import com.yaoan.module.econtract.dal.mysql.formconfig.FormBusinessRelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/21 11:51
 */
@Service
public class FormBizRelServiceImpl implements FormBizRelService {
    @Resource
    private FormBusinessRelMapper formBusinessRelMapper;

    @Override
    public String save(FormRelSaveReqVO vo) {
        List<FormInfoReqVO> forms = vo.getFormList();
        if (CollectionUtil.isNotEmpty(forms)) {
            List<FormBusinessRelDO> relDOList = new ArrayList<FormBusinessRelDO>();
            for (FormInfoReqVO formInfoReqVO : forms) {
                FormBusinessRelDO relDO = new FormBusinessRelDO();
                relDO.setSort(formInfoReqVO.getSort());
                relDO.setFormId(formInfoReqVO.getFormId());
                relDO.setBusinessId(vo.getBusinessId());
                relDOList.add(relDO);
            }
            formBusinessRelMapper.insertBatch(relDOList);
        }
        return "success";
    }

    @Override
    public String update(FormRelSaveReqVO vo) {
        List<FormInfoReqVO> forms = vo.getFormList();
        if (CollectionUtil.isNotEmpty(forms)) {
            List<FormBusinessRelDO> relDOList = new ArrayList<FormBusinessRelDO>();
            for (FormInfoReqVO formInfoReqVO : forms) {
                FormBusinessRelDO relDO = new FormBusinessRelDO();
                relDO.setSort(formInfoReqVO.getSort());
                relDO.setFormId(formInfoReqVO.getFormId());
                relDO.setBusinessId(vo.getBusinessId());
                relDOList.add(relDO);
            }
            //先清空关联
            formBusinessRelMapper.delete(new LambdaQueryWrapperX<FormBusinessRelDO>().eq(FormBusinessRelDO::getBusinessId, vo.getBusinessId()));
            //重新关联
            formBusinessRelMapper.insertBatch(relDOList);
        }
        return "success";
    }

    @Override
    public String delete(IdReqVO vo) {
        formBusinessRelMapper.delete(new LambdaQueryWrapperX<FormBusinessRelDO>().in(FormBusinessRelDO::getBusinessId, vo.getIdList()));
        return "success";
    }
}

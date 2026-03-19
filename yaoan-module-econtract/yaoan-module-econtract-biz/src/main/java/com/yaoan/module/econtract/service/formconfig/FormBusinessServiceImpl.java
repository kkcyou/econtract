package com.yaoan.module.econtract.service.formconfig;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.formbusiness.FormBusinessListReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.formbusiness.FormBusinessListRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.formbusiness.FormBusinessSaveReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.convert.formconfig.FormConfigConverter;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormBusinessDO;
import com.yaoan.module.econtract.dal.mysql.formconfig.FormBusinessMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/19 10:21
 */
@Service
public class FormBusinessServiceImpl implements FormBusinessService {
    @Resource
    private FormBusinessMapper formBusinessMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveFormBusiness(FormBusinessSaveReqVO vo) {
        FormBusinessDO entity = FormConfigConverter.INSTANCE.bizReq2DO(vo);
        formBusinessMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public String updateFormBusiness(FormBusinessSaveReqVO vo) {
        FormBusinessDO entity = FormConfigConverter.INSTANCE.bizReq2DO(vo);
        formBusinessMapper.updateById(entity);
        return "success";
    }

    @Override
    public String deleteFormBusiness(IdReqVO vo) {
        formBusinessMapper.deleteBatchIds(vo.getIdList());
        return "success";
    }

    @Override
    public PageResult<FormBusinessListRespVO> listFormBusiness(FormBusinessListReqVO vo) {
        PageResult<FormBusinessListRespVO> result=new PageResult<FormBusinessListRespVO>().setTotal(0L);
        PageResult<FormBusinessDO> doPageResult=formBusinessMapper.selectPage(vo,null);
        if(CollectionUtils.isNotEmpty(doPageResult.getList())){
          result= FormConfigConverter.INSTANCE.bizPageDo2Resp(doPageResult);
        }
        return result;
    }

}

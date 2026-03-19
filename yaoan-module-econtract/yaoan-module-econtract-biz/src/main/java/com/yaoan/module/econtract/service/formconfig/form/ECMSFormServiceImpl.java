package com.yaoan.module.econtract.service.formconfig.form;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.*;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.convert.formconfig.FormConverter;
import com.yaoan.module.econtract.convert.formconfig.FormItemConverter;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormDO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormItemDO;
import com.yaoan.module.econtract.dal.mysql.formconfig.FormDOMapper;
import com.yaoan.module.econtract.dal.mysql.formconfig.FormItemMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/20 17:29
 */
@Service
public class ECMSFormServiceImpl implements ECMSFormService {
    @Resource
    private FormItemMapper formItemMapper;
    @Resource
    private FormDOMapper formDOMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(FormSaveReqVO vo) {
        FormDO formDO = FormConverter.INSTANCE.saveReq2Entity(vo);
        existCode(formDO);
        formDOMapper.insert(formDO);
        List<FormItemInfoReqVO> itemInfoReqVOS = vo.getItemReqVOList();
        if (CollectionUtil.isNotEmpty(itemInfoReqVOS)) {
            List<FormItemDO> items = new ArrayList<FormItemDO>();
            for (FormItemInfoReqVO info : itemInfoReqVOS) {
                FormItemDO itemDO = new FormItemDO();
                itemDO.setId(info.getId());
                itemDO.setFormId(formDO.getId());
                itemDO.setSort(info.getSort());
                items.add(itemDO);
            }
            formItemMapper.updateBatch(items);
        }
        return formDO.getId();
    }

    private void existCode(FormDO formDO) {
        Long count = formDOMapper.selectCount(new LambdaQueryWrapperX<FormDO>().eq(FormDO::getCode, formDO.getId()));
        Boolean rs = 0 < count;
        if (rs) {
            throw exception(ErrorCodeConstants.CODE_EXIST_ERROR);
        }
    }

    @Override
    public String update(FormUpdateReqVO vo) {
        //表单信息
        FormDO formDO = FormConverter.INSTANCE.updateReq2Entity(vo);
        formDOMapper.updateById(formDO);
        List<FormItemDO> items = new ArrayList<FormItemDO>();
        List<FormItemInfoReqVO> itemInfoReqVOS = vo.getItemReqVOList();
        if (CollectionUtil.isNotEmpty(itemInfoReqVOS)) {

            for (FormItemInfoReqVO info : itemInfoReqVOS) {
                FormItemDO itemDO = new FormItemDO();
                itemDO.setId(info.getId());
                itemDO.setFormId(formDO.getId());
                itemDO.setSort(info.getSort());
                items.add(itemDO);
            }
        }
        //清空关联，将formId相关的表项关联的formId都清空
        formItemMapper.cleanByFormId(new FormItemDO().setFormId(""), formDO.getId());
        //重新关联
        formItemMapper.updateBatch(items);

        return "success";
    }

    @Override
    public FormOneRespVO getOne(IdReqVO vo) {
        FormOneRespVO result = new FormOneRespVO();
        String formId = vo.getId();
        FormDO formDO = formDOMapper.selectById(formId);
        if (ObjectUtil.isNotNull(formDO)) {
            result = FormConverter.INSTANCE.entity2OneRespVO(formDO);
            //关联表项
            List<FormItemDO> items = formItemMapper.selectList(new LambdaQueryWrapperX<FormItemDO>().eq(FormItemDO::getFormId, formId));
            if (CollectionUtil.isNotEmpty(items)) {
                List<FormShowItemRespVO> itemsShow = FormItemConverter.INSTANCE.listEntity2ShowResp(items);
                result.setItemReqVOList(itemsShow);
            }
        }
        return result;
    }


    @Override
    public PageResult<FormListRespVO> list(FormSaveReqVO vo) {
        PageResult<FormListRespVO> result = new PageResult<FormListRespVO>();
        PageResult<FormDO> doPageResult = formDOMapper.selectPage(vo);
        if (CollectionUtil.isNotEmpty(doPageResult.getList())) {
            result = FormConverter.INSTANCE.pageEntity2Resp(doPageResult);
        } else {
            result.setTotal(doPageResult.getTotal());
        }
        return result;
    }

    @Override
    public String deleteBatch(IdReqVO vo) {
        List<String> formIds = vo.getIdList();
        formDOMapper.deleteBatchIds(formIds);
        formItemMapper.delete(new LambdaQueryWrapperX<FormItemDO>().inIfPresent(FormItemDO::getFormId, formIds));
        return "success";
    }

    @Override
    public List<FormOneRespVO> listFormByBizId(IdReqVO vo) {
        List<FormOneRespVO> result = new ArrayList<FormOneRespVO>();
        String bizId = vo.getId();
        List<FormDO> formDoList = formDOMapper.listFormByBizId(bizId);
        if (CollectionUtil.isNotEmpty(formDoList)) {
            result = FormConverter.INSTANCE.listEntity2Resp(formDoList);
        }
        return result;
    }
}

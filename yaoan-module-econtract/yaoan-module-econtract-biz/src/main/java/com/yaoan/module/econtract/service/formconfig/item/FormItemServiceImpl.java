package com.yaoan.module.econtract.service.formconfig.item;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormSaveReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.item.vo.FormItemListRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.item.vo.FormItemOneRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.item.vo.FormItemSaveReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.convert.formconfig.FormItemConverter;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormItemDO;
import com.yaoan.module.econtract.dal.mysql.formconfig.FormItemMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.CODE_EXIST_ERROR;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/20 17:31
 */
@Service
public class FormItemServiceImpl implements FormItemService {
    @Resource
    private FormItemMapper formItemMapper;

    @Override
    public String save(FormItemSaveReqVO vo) {
        FormItemDO formItemDO = FormItemConverter.INSTANCE.req2Entity(vo);
        formItemMapper.insert(formItemDO);
        return formItemDO.getId();
    }

    private void checkCode(String code) {
        Long count = formItemMapper.selectCount(new LambdaQueryWrapperX<FormItemDO>().eq(FormItemDO::getCode, code));
        if (0 < count) {
            throw exception(CODE_EXIST_ERROR);
        }
    }

    @Override
    public FormItemOneRespVO getOne(IdReqVO vo) {
        FormItemOneRespVO result = new FormItemOneRespVO();
        String formId = vo.getId();
        FormItemDO formItemDO = formItemMapper.selectById(formId);
        if (ObjectUtil.isNotNull(formItemDO)) {
            result = FormItemConverter.INSTANCE.entity2Resp(formItemDO);
        }
        return result;
    }

    @Override
    public String update(FormItemSaveReqVO vo) {
        FormItemDO formItemDO = FormItemConverter.INSTANCE.req2Entity(vo);
        formItemMapper.updateById(formItemDO);
        return "success";
    }

    @Override
    public String deleteBatch(IdReqVO vo) {
        List<String> ids = vo.getIdList();
        if (CollectionUtil.isNotEmpty(ids)) {
            formItemMapper.deleteBatchIds(ids);
        }
        return "success";
    }

    @Override
    public PageResult<FormItemListRespVO> list(FormSaveReqVO vo) {
        PageResult<FormItemDO> formItemDOPage = formItemMapper.list(vo);
        return enhancePage(formItemDOPage);
    }

    private PageResult<FormItemListRespVO> enhancePage(PageResult<FormItemDO> formItemDOPage) {
        PageResult<FormItemListRespVO> result = FormItemConverter.INSTANCE.pageEntity2Resp(formItemDOPage);
        List<FormItemDO> entityList = formItemDOPage.getList();
        if (CollectionUtil.isNotEmpty(entityList)) {

        }
        return result;
    }

    @Override
    public List<FormItemListRespVO> getItemsByFormId(IdReqVO vo) {
        List<FormItemListRespVO> result = new ArrayList<FormItemListRespVO>();
        String formId = vo.getId();
        List<FormItemDO> entityList = formItemMapper.getItemsByFormId(formId);
        if (CollectionUtil.isNotEmpty(entityList)) {
            result = FormItemConverter.INSTANCE.listDO2Resp(entityList);
        }
        return result;
    }


}

package com.yaoan.module.econtract.service.formconfig;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.FormConfigSaveReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.FormConfigSingleRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.FormReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.FormRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.item.FormItemReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.item.FormItemRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.convert.formconfig.FormConfigConverter;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormBusinessDO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormBusinessRelDO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormDO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormItemDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.formconfig.FormBusinessMapper;
import com.yaoan.module.econtract.dal.mysql.formconfig.FormBusinessRelMapper;
import com.yaoan.module.econtract.dal.mysql.formconfig.FormDOMapper;
import com.yaoan.module.econtract.dal.mysql.formconfig.FormItemMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/18 19:30
 */
@Service
public class FormConfigServiceImpl implements FormConfigService {
    @Resource
    private FormDOMapper formDOMapper;
    @Resource
    private FormItemMapper formItemMapper;
    @Resource
    private FormBusinessRelMapper formBusinessRelMapper;
    @Resource
    private FormBusinessMapper formBusinessMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;

    /**
     * 新增表单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveFormConfig(FormConfigSaveReqVO vo) {
        if (CollectionUtil.isNotEmpty(vo.getFormReqVOS())) {
            String businessId = vo.getBusinessId();
            List<FormDO> formDOList = new ArrayList<FormDO>();
            List<FormBusinessRelDO> formBusinessRelDOList = new ArrayList<FormBusinessRelDO>();
            List<FormItemDO> formItemDOList = new ArrayList<FormItemDO>();
            for (FormReqVO formReqVO : vo.getFormReqVOS()) {
                //保存表单信息
                String formId = IdUtil.simpleUUID();
                formReqVO.setId(formId);
                FormDO entity = FormConfigConverter.INSTANCE.formReq2DO(formReqVO);
                formDOList.add(entity);
                //保存业务和表单的关系
                FormBusinessRelDO formBusinessRelDO = new FormBusinessRelDO();
                formBusinessRelDO.setBusinessId(businessId);
                formBusinessRelDO.setFormId(formId);
                formBusinessRelDO.setSort(formReqVO.getSort());
                formBusinessRelDOList.add(formBusinessRelDO);
                //保存表项
                if (CollectionUtil.isNotEmpty(formReqVO.getItemReqVOS())) {
                    List<FormItemDO> formItems = FormConfigConverter.INSTANCE.listItemReq2DO(formReqVO.getItemReqVOS());
                    formItems = formItems.stream().map(item -> {
                        item.setFormId(formId);
                        return item;
                    }).collect(Collectors.toList());
                    formItemDOList.addAll(formItems);
                }
            }
            formDOMapper.insertBatch(formDOList);
            formBusinessRelMapper.insertBatch(formBusinessRelDOList);
            formItemMapper.insertBatch(formItemDOList);
        }
        return "success";
    }

    /**
     * 编辑表单配置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateFormConfig(FormConfigSaveReqVO vo) {
        //更新表单业务关系（增改删）
        updateFormRel(vo);
        //更新表单和表项（增改删）
        updateFormItem(vo);
        return "success";
    }

    /**
     * 更新表单业务关系
     */
    private void updateFormRel(FormConfigSaveReqVO vo) {
        List<FormBusinessRelDO> formBusinessRelDOList = new ArrayList<FormBusinessRelDO>();
        List<FormReqVO> formReqVOS = vo.getFormReqVOS();
        String businessId = vo.getBusinessId();
        //原有的表单id
        List<String> oldFormIds = new ArrayList<String>();
        //输入的表单id
        List<String> newFormIds = new ArrayList<String>();
        List<String> deleteIds = new ArrayList<String>();
        //原有的业务
        List<FormBusinessRelDO> oldFormRelList = formBusinessRelMapper.selectList(new LambdaQueryWrapperX<FormBusinessRelDO>()
                .eq(FormBusinessRelDO::getBusinessId, businessId));
        if (CollectionUtil.isNotEmpty(oldFormRelList)) {
            oldFormIds = oldFormRelList.stream().map(FormBusinessRelDO::getFormId).collect(Collectors.toList());
            newFormIds = formReqVOS.stream().map(FormReqVO::getId)
                    .filter(id -> id != null && !id.isEmpty())
                    .collect(Collectors.toList());
            List<String> finalNewFormIds = newFormIds;
            deleteIds = oldFormIds.stream()
                    .filter(id -> !finalNewFormIds.contains(id))
                    .collect(Collectors.toList());
        }
        // 删除关系
        if (CollectionUtil.isNotEmpty(deleteIds)) {
            formBusinessRelMapper.delete(new LambdaQueryWrapperX<FormBusinessRelDO>()
                    .eq(FormBusinessRelDO::getBusinessId, businessId)
                    .in(FormBusinessRelDO::getFormId, deleteIds)
                    .select(FormBusinessRelDO::getId));
        }
        // 更新关系
        if (CollectionUtil.isNotEmpty(formReqVOS)) {
            for (FormReqVO formReqVO : formReqVOS) {
                String formId = StringUtils.isNotBlank(formReqVO.getId()) ? formReqVO.getId() : IdUtil.simpleUUID();
                FormBusinessRelDO relDO = new FormBusinessRelDO();
                relDO.setBusinessId(businessId);
                relDO.setFormId(formId);
                relDO.setSort(formReqVO.getSort());
                formBusinessRelDOList.add(relDO);
            }

            //新增和修改
            formBusinessRelMapper.saveOrUpdateBatch(formBusinessRelDOList);

        }
    }

    /**
     * 更新表单和表项关系
     */
    private void updateFormItem(FormConfigSaveReqVO vo) {
        List<FormReqVO> formReqVOS = vo.getFormReqVOS();
        if (CollectionUtil.isNotEmpty(formReqVOS)) {
            List<String> oldIds = new ArrayList<String>();
            List<String> newIds = new ArrayList<String>();
            List<String> deleteIds = new ArrayList<String>();
            List<String> formIds = formReqVOS.stream().map(FormReqVO::getId).collect(Collectors.toList());
            newIds = formReqVOS.stream().map(FormReqVO::getId)
                    .filter(id -> id != null && !id.isEmpty())
                    .collect(Collectors.toList());
            List<FormItemDO> itemDOList = new ArrayList<FormItemDO>();
            List<FormItemDO> saveItemDOList = new ArrayList<FormItemDO>();
            List<FormItemDO> oldDOList = new ArrayList<FormItemDO>();
            Map<String, FormItemDO> formItemDOMap = new HashMap<String, FormItemDO>();
            oldDOList = formItemMapper.selectList(new LambdaQueryWrapperX<FormItemDO>().inIfPresent(FormItemDO::getFormId, formIds));
            oldIds = oldDOList.stream().map(FormItemDO::getId).collect(Collectors.toList());

            for (FormReqVO formReqVO : vo.getFormReqVOS()) {
                List<FormItemReqVO> itemReqVOS = formReqVO.getItemReqVOS();
                if (CollectionUtil.isNotEmpty(itemReqVOS)) {
                    //给新项加id,并返回新IdList
                    enhanceItemId(itemReqVOS);
                    itemDOList = FormConfigConverter.INSTANCE.listItemReq2DO(itemReqVOS);
                    //新增和编辑
                    saveItemDOList.addAll(itemDOList);
                }
            }

            List<String> finalNewFormIds = newIds;
            deleteIds = oldIds.stream()
                    .filter(id -> !finalNewFormIds.contains(id))
                    .collect(Collectors.toList());

            formItemMapper.saveOrUpdateBatch(saveItemDOList);
            if (CollectionUtil.isNotEmpty(deleteIds)) {
                formItemMapper.deleteBatchIds(deleteIds);
            }
        }
    }

    private List<String> enhanceItemId(List<FormItemReqVO> itemReqVOS) {
        List<String> newIds = new ArrayList<String>();
        if (CollectionUtil.isNotEmpty(itemReqVOS)) {
            for (FormItemReqVO vo : itemReqVOS) {
                if (StringUtils.isBlank(vo.getId())) {
                    vo.setId(IdUtil.simpleUUID());
                    newIds.add(vo.getId());
                }
            }
        }
        return newIds;
    }

    /**
     * 查看表单配置
     */
    @Override
    public FormConfigSingleRespVO getFormBusinessByBusinessId(IdReqVO vo) {
        FormConfigSingleRespVO result = new FormConfigSingleRespVO();
        String businessId = vo.getId();
        FormBusinessDO formBusinessDO = formBusinessMapper.selectById(businessId);
        if (ObjectUtils.isNull(formBusinessDO)) {
            return null;
        }
        List<FormDO> formDOList = new ArrayList<FormDO>();
        List<FormRespVO> formRespVOS = new ArrayList<FormRespVO>();
        List<FormItemDO> itemDOList = new ArrayList<FormItemDO>();
        List<FormItemRespVO> formItemRespVOList = new ArrayList<FormItemRespVO>();
        List<String> formIds = new ArrayList<>();
        Map<String, List<FormItemRespVO>> itemRespMap = new HashMap<String, List<FormItemRespVO>>();
        formDOList = formDOMapper.getFormBusinessByBusinessId(businessId);
        if (CollectionUtil.isEmpty(formDOList)) {
            return null;
        }
        formRespVOS = FormConfigConverter.INSTANCE.listFormDO2Resp(formDOList);
        formIds = formDOList.stream().map(FormDO::getId).collect(Collectors.toList());
        List<FormBusinessRelDO> relDOList = formBusinessRelMapper.selectList(new LambdaQueryWrapperX<FormBusinessRelDO>()
                .inIfPresent(FormBusinessRelDO::getFormId,formIds)
                .eq(FormBusinessRelDO::getBusinessId, businessId));
        Map<String, FormBusinessRelDO> relDOMap = new HashMap<String, FormBusinessRelDO>();
        if (CollectionUtil.isNotEmpty(relDOList)) {
            relDOMap = CollectionUtils.convertMap(relDOList, FormBusinessRelDO::getFormId);
        }
        for (FormRespVO formRespVO : formRespVOS) {
            FormBusinessRelDO relDO = relDOMap.get(formRespVO.getId());
            if (ObjectUtils.isNotNull(relDO)) {
                formRespVO.setSort(relDO.getSort());
            }
        }

        itemDOList = formItemMapper.selectList(new LambdaQueryWrapperX<FormItemDO>().inIfPresent(FormItemDO::getFormId, formIds));
        if (CollectionUtil.isNotEmpty(itemDOList)) {
            formItemRespVOList = FormConfigConverter.INSTANCE.listItemDO2Resp(itemDOList);
            itemRespMap = formItemRespVOList.stream().collect(Collectors.groupingBy(FormItemRespVO::getFormId));
            for (FormRespVO formRespVO : formRespVOS) {
                List<FormItemRespVO> itemRespVOList = itemRespMap.get(formRespVO.getId());
                if (CollectionUtil.isNotEmpty(itemRespVOList)) {
                    itemRespVOList=itemRespVOList.stream().sorted(Comparator.comparingInt(FormItemRespVO::getSort)).collect(Collectors.toList());
                    formRespVO.setItemReqVOS(itemRespVOList);
                }
            }
            formRespVOS=   formRespVOS.stream()
                    .sorted(Comparator.comparingInt(FormRespVO::getSort))
                    .collect(Collectors.toList());
            result.setFormReqVOS(formRespVOS);
            result.setBusinessId(businessId);
            result.setName(formBusinessDO.getName());
        }
        return result;
    }

}

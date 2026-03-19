package com.yaoan.module.bpm.api.task;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.bpm.api.bpm.activity.ActReModelApi;
import com.yaoan.module.bpm.api.bpm.activity.dto.ActReModelDTO;
import com.yaoan.module.bpm.dal.dataobject.act.ActReModel;
import com.yaoan.module.bpm.dal.mysql.act.ActReModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActReModelApiImpl implements ActReModelApi {

    @Resource
    private ActReModelMapper actReModelMapper;

    @Override
    public List<ActReModelDTO> getActReModelByTenantId(String tenantId) {
        List<ActReModel> actReModels = actReModelMapper.selectList(ActReModel::getTenantId, tenantId);
        if (CollectionUtil.isNotEmpty(actReModels)){
            return BeanUtils.toBean(actReModels, ActReModelDTO.class);
        }
        return new ArrayList<>();
    }

    @Override
    public void insertActReModels(List<ActReModelDTO> actReModelDTOS) {
        List<ActReModel> actReModels = BeanUtils.toBean(actReModelDTOS, ActReModel.class);
        actReModelMapper.insertBatch(actReModels);
    }
}

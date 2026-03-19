package com.yaoan.module.bpm.api.task;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.bpm.api.bpm.activity.ActGeByteArrayApi;
import com.yaoan.module.bpm.api.bpm.activity.dto.ActGeByteArrayDTO;
import com.yaoan.module.bpm.dal.dataobject.act.ActGeByteArray;
import com.yaoan.module.bpm.dal.mysql.act.ActGeByteArraylMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActGeByteArrayApiImpl implements ActGeByteArrayApi {


    @Resource
    private ActGeByteArraylMapper actGeByteArraylMapper;

    @Override
    public List<ActGeByteArrayDTO> getActReModelByIds(List<String> ids) {
        List<ActGeByteArray> actGeByteArrays = actGeByteArraylMapper.selectBatchIds(ids);
        if (CollectionUtil.isNotEmpty(actGeByteArrays)) {
            return BeanUtils.toBean(actGeByteArrays, ActGeByteArrayDTO.class);
        }
        return new ArrayList<>();
    }

    @Override
    public void insertActGeByteArrays(List<ActGeByteArrayDTO> actGeByteArrayDTOS) {
        List<ActGeByteArray> actGeByteArrays = BeanUtils.toBean(actGeByteArrayDTOS, ActGeByteArray.class);
        actGeByteArraylMapper.insertBatch(actGeByteArrays);
    }
}

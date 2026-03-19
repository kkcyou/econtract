package com.yaoan.module.econtract.api.model;

import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.api.model.dto.ModelDTO;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;

@Service
@Slf4j
public class ModelApiImpl implements ModelApi{
    @Resource
    private ModelMapper modelMapper;
    @Override
    public void insertModels(List<ModelDTO> modelDTOS, Long tenantId) {
        List<Model> models = BeanUtils.toBean(modelDTOS, Model.class);
        models.forEach(item-> item.setTenantId(tenantId));
        modelMapper.insertBatch(models);
    }
}

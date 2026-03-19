package com.yaoan.module.econtract.api.model;

import com.yaoan.module.econtract.api.model.dto.ModelDTO;
import org.springframework.ui.Model;

import java.util.List;

public interface ModelApi {
    // 为system模块提供一个批量插入模板的接口
    void insertModels(List<ModelDTO> models, Long tenantId);
}

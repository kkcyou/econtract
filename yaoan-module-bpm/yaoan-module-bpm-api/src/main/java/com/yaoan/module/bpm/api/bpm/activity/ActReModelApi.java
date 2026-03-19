package com.yaoan.module.bpm.api.bpm.activity;

import com.yaoan.module.bpm.api.bpm.activity.dto.ActReModelDTO;

import java.util.List;

public interface ActReModelApi {
    List<ActReModelDTO> getActReModelByTenantId(String tenantId);
    void insertActReModels(List<ActReModelDTO> actReModelDTOS);
}

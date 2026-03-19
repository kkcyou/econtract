package com.yaoan.module.bpm.api.bpm.activity;

import com.yaoan.module.bpm.api.bpm.activity.dto.ActGeByteArrayDTO;
import com.yaoan.module.bpm.api.bpm.activity.dto.ActReModelDTO;

import java.util.List;

public interface ActGeByteArrayApi {
    List<ActGeByteArrayDTO> getActReModelByIds(List<String> ids);
    void insertActGeByteArrays(List<ActGeByteArrayDTO> actGeByteArrayDTOS);
}

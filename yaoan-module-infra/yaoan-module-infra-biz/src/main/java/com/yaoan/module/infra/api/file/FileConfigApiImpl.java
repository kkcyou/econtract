package com.yaoan.module.infra.api.file;

import com.yaoan.module.infra.dal.mysql.file.FileConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

@Slf4j
@Service
@Validated
public class FileConfigApiImpl implements FileConfigApi {
    @Resource
    private FileConfigMapper fileConfigMapper;


    @Override
    public Long selectTrueConfigId() {
        return fileConfigMapper.selectTrueConfigId();
    }
}

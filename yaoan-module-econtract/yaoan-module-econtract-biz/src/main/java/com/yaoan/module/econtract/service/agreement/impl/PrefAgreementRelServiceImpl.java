package com.yaoan.module.econtract.service.agreement.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.module.econtract.controller.admin.agreement.vo.PrefAgreementRelCreateReqVO;
import com.yaoan.module.econtract.convert.agreement.PrefAgreementRelConverter;
import com.yaoan.module.econtract.dal.dataobject.agreement.PrefAgreementRelDO;
import com.yaoan.module.econtract.dal.mysql.agreement.PrefAgreementRelMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.service.agreement.PrefAgreementRelService;
import com.yaoan.module.infra.api.file.FileApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

@DataPermission(enable = false)
@Service
@Slf4j
public class PrefAgreementRelServiceImpl implements PrefAgreementRelService {
    @Resource
    private PrefAgreementRelConverter prefAgreementRelConverter;

    @Resource
    private PrefAgreementRelMapper prefAgreementRelMapper;

    @Resource
    private FileApi fileApi;

    /**
     * 创建补充协议(修改)
     * @param prefAgreementRelCreateReqVO
     * @return
     */
    @Override
    public String createAgreement(PrefAgreementRelCreateReqVO prefAgreementRelCreateReqVO) throws Exception {
        PrefAgreementRelDO prefAgreementRelDO = prefAgreementRelConverter.toEntity(prefAgreementRelCreateReqVO);
        //校验文件名称是否重复
        if (nameExist(prefAgreementRelDO.getId(),prefAgreementRelDO.getFileName())){
            throw exception(ErrorCodeConstants.NAME_EXISTS);
        }
        if (StringUtils.isBlank(prefAgreementRelCreateReqVO.getFileName())){
            throw exception(ErrorCodeConstants.FILE_NAME_NOT_NULL);
        }
        if (ObjectUtil.isNotEmpty(prefAgreementRelDO.getId())){
            Long infraFileId = prefAgreementRelMapper.selectById(prefAgreementRelDO.getId()).getInfraFileId();
            fileApi.deleteFile(infraFileId);
            prefAgreementRelMapper.updateById(prefAgreementRelDO);
        }else {
            prefAgreementRelMapper.insert(prefAgreementRelDO);
        }
        return prefAgreementRelDO.getId();
    }

    public Boolean nameExist(String id, String name) {
        return prefAgreementRelMapper.nameExist(id,name);
    }
}

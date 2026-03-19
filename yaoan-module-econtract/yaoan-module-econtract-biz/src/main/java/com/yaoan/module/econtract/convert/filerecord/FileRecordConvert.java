package com.yaoan.module.econtract.convert.filerecord;

import com.yaoan.module.econtract.controller.admin.gpx.vo.file.SaveFileAndCompanyReqVO;
import com.yaoan.module.econtract.dal.dataobject.filerecord.FileRecordDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @description:
 * @author: Pele
 * @date: 2024/7/4 17:15
 */
@Mapper
public interface FileRecordConvert {
    FileRecordConvert INSTANCE = Mappers.getMapper(FileRecordConvert.class);

    FileRecordDO r2d(SaveFileAndCompanyReqVO contractPageReqVO);
}

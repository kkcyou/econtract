package com.yaoan.module.econtract.convert.version;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.listen.dto.FileVersionEventDTO;
import com.yaoan.module.econtract.controller.admin.version.vo.FileVersionSaveReqVO;
import com.yaoan.module.econtract.controller.admin.version.vo.list.FileVersionPageRespVO;
import com.yaoan.module.econtract.dal.dataobject.version.FileVersionDO;
import com.yaoan.module.econtract.framework.core.event.version.FileVersionEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/8/29 11:45
 */
@Mapper
public interface FileVersionConvert {
    FileVersionConvert INSTANCE = Mappers.getMapper(FileVersionConvert.class);

    FileVersionDO r2d(FileVersionSaveReqVO reqVO);

    PageResult<FileVersionPageRespVO> pageR2D(PageResult<FileVersionDO> doPageResult);

    List<FileVersionPageRespVO> list1(List<FileVersionDO> i);

    FileVersionPageRespVO c1(FileVersionDO i);

    FileVersionEvent dto2DO(FileVersionEventDTO dto);
}

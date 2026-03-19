package com.yaoan.module.infra.convert.file;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.infra.api.file.dto.FileWpsDTO;
import com.yaoan.module.infra.api.file.dto.FilesDTO;
import com.yaoan.module.infra.controller.admin.file.vo.file.FileRespVO;
import com.yaoan.module.infra.dal.dataobject.file.FileDO;
import com.yaoan.module.infra.dal.dataobject.file.FileFastDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FileConvert {

    FileConvert INSTANCE = Mappers.getMapper(FileConvert.class);

    FileRespVO convert(FileDO bean);

    FileDTO toDTO(FileDO bean);

    FileDO toDO(FileDTO bean);

    PageResult<FileRespVO> convertPage(PageResult<FileDO> page);

    List<FileDTO> getDTO(List<FileDO> fileDOList);

    List<FileDTO> convertList4(List<FileDO> fileDOList);

    List<FileDTO> convertListFast(List<FileFastDO> fileDOList);

    FileWpsDTO toWpsDTO(FileDO fileDO);

    FileDO wps2FileDO(FileWpsDTO fileWpsDTO);

    FilesDTO toFilesDTO(FileDO fileDO);

    FileDO toFiles2DO(FilesDTO files);
}

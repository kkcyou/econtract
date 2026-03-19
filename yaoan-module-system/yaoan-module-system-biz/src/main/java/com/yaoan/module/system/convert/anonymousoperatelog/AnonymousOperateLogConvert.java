package com.yaoan.module.system.convert.anonymousoperatelog;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.system.api.logger.dto.AnonymousOperateLogDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yaoan.module.system.controller.admin.anonymousoperatelog.vo.*;
import com.yaoan.module.system.dal.dataobject.anonymousoperatelog.AnonymousOperateLogDO;

/**
 * 匿名用户操作日志记录 Convert
 *
 * @author doujiale
 */
@Mapper
public interface AnonymousOperateLogConvert {

    AnonymousOperateLogConvert INSTANCE = Mappers.getMapper(AnonymousOperateLogConvert.class);

    AnonymousOperateLogDO convert(AnonymousOperateLogDTO bean);

    AnonymousOperateLogDO convert(AnonymousOperateLogUpdateReqVO bean);

    AnonymousOperateLogRespVO convert(AnonymousOperateLogDO bean);

    List<AnonymousOperateLogRespVO> convertList(List<AnonymousOperateLogDO> list);

    PageResult<AnonymousOperateLogRespVO> convertPage(PageResult<AnonymousOperateLogDO> page);

    List<AnonymousOperateLogExcelVO> convertList02(List<AnonymousOperateLogDO> list);

}

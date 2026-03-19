package com.yaoan.module.system.convert.invitecode;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yaoan.module.system.controller.admin.invitecode.vo.*;
import com.yaoan.module.system.dal.dataobject.invitecode.InviteCodeDO;

/**
 * 邀请码管理 Convert
 *
 * @author admin
 */
@Mapper
public interface InviteCodeConvert {

    InviteCodeConvert INSTANCE = Mappers.getMapper(InviteCodeConvert.class);

    InviteCodeDO convert(InviteCodeSaveReqVO bean);

    InviteCodeRespVO convert(InviteCodeDO bean);

    List<InviteCodeRespVO> convertList(List<InviteCodeDO> list);

    PageResult<InviteCodeRespVO> convertPage(PageResult<InviteCodeDO> page);

    List<InviteCodeExcelVO> convertList02(List<InviteCodeDO> list);

}

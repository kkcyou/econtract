package com.yaoan.module.econtract.convert.relativeBlacklist.relative;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistApplyReqVO;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistHandleReqVO;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistRespVO;
import com.yaoan.module.econtract.dal.dataobject.relativeBlacklist.RelativeBlacklist;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author：doujl
 * @Description:  转换类
 * @Date: 2023年07月05日14:30:38
 */
@Mapper(componentModel = "spring")
public interface RelativeBlacklistConverter {

    RelativeBlacklistConverter INSTANCE = Mappers.getMapper(RelativeBlacklistConverter.class);

    RelativeBlacklist toEntity(BlacklistApplyReqVO bean);
    RelativeBlacklist toEntity(BlacklistHandleReqVO bean);
    BlacklistRespVO toVO(RelativeBlacklist relativeBlacklist);
    PageResult<BlacklistRespVO> convertPage(PageResult<RelativeBlacklist> page);

    
}

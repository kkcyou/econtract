package com.yaoan.module.system.convert.user;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.system.controller.admin.user.saas.vo.SaasUserPageRespVO;
import com.yaoan.module.system.controller.admin.user.saas.vo.SaasUserSaveReqVO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-23 20:21
 */
@Mapper
public interface SaasUserConvert {

    SaasUserConvert INSTANCE = Mappers.getMapper(SaasUserConvert.class);


    AdminUserDO convert(SaasUserSaveReqVO reqVO);
    PageResult<SaasUserPageRespVO> toUserPage(PageResult<AdminUserDO> uderPageResult);
}

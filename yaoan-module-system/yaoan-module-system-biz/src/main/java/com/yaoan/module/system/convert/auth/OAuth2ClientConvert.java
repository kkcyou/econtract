package com.yaoan.module.system.convert.auth;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.system.api.oauth2.dto.OAuth2ClientCheckRespDTO;
import com.yaoan.module.system.controller.admin.oauth2.vo.client.*;
import com.yaoan.module.system.dal.dataobject.oauth2.OAuth2ClientDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * OAuth2 客户端 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface OAuth2ClientConvert {

    OAuth2ClientConvert INSTANCE = Mappers.getMapper(OAuth2ClientConvert.class);

    OAuth2ClientDO convert(OAuth2ClientCreateReqVO bean);

    OAuth2ClientDO convert(OAuth2ClientUpdateReqVO bean);

    OAuth2ClientRespVO convert(OAuth2ClientDO bean);

    OAuth2ClientCheckRespDTO convert2DTO(OAuth2ClientDO bean);

    List<OAuth2ClientRespVO> convertList(List<OAuth2ClientDO> list);

    PageResult<OAuth2ClientRespVO> convertPage(PageResult<OAuth2ClientDO> page);
    PageResult<OAuth2ClientV2RespVO> convertPageV2(PageResult<OAuth2ClientDO> page);

}

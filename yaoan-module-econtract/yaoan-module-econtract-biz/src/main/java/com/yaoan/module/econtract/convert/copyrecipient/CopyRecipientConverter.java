package com.yaoan.module.econtract.convert.copyrecipient;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.copyrecipient.vo.CopyRecipientPageRespVO;
import com.yaoan.module.econtract.dal.dataobject.copyrecipients.BpmCopyRecipientsDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/2 11:50
 */
@Mapper
public interface CopyRecipientConverter {
    CopyRecipientConverter INSTANCE = Mappers.getMapper(CopyRecipientConverter.class);

    PageResult<CopyRecipientPageRespVO> pageDO2Resp(PageResult<BpmCopyRecipientsDO> doPage);

    List<CopyRecipientPageRespVO> listDO2Resp(List<BpmCopyRecipientsDO> list);

    CopyRecipientPageRespVO do2Resp(BpmCopyRecipientsDO item);
}

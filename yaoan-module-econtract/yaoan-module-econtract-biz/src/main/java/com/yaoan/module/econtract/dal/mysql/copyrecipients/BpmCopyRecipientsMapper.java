package com.yaoan.module.econtract.dal.mysql.copyrecipients;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.copyrecipient.vo.CopyRecipientPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.copyrecipients.BpmCopyRecipientsDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/1 20:19
 */
@Mapper
public interface BpmCopyRecipientsMapper extends BaseMapperX<BpmCopyRecipientsDO> {

    default PageResult<BpmCopyRecipientsDO> selectPage(CopyRecipientPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BpmCopyRecipientsDO>()
                .orderByDesc(BpmCopyRecipientsDO::getUpdateTime)
                .eqIfPresent(BpmCopyRecipientsDO::getRecipientId, SecurityFrameworkUtils.getLoginUserId())
        );
    }
}

package com.yaoan.module.system.service.notify;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.system.controller.admin.notify.vo.message.NotifyMessageMyPageReqVO;
import com.yaoan.module.system.controller.admin.notify.vo.message.NotifyMessagePageReqVO;
import com.yaoan.module.system.dal.dataobject.notify.NotifyMessageDO;
import com.yaoan.module.system.dal.dataobject.notify.NotifyTemplateDO;
import com.yaoan.module.system.dal.mysql.notify.NotifyMessageMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 站内信 Service 实现类
 *
 * @author xrcoder
 */
@Service
@Validated
public class NotifyMessageServiceImpl implements NotifyMessageService {

    @Resource
    private NotifyMessageMapper notifyMessageMapper;

    @Override
    public Long createNotifyMessage(Long userId, Integer userType,
                                    NotifyTemplateDO template, String templateContent, Map<String, Object> templateParams) {
        NotifyMessageDO message = new NotifyMessageDO().setUserId(userId).setUserType(userType)
                .setTemplateId(template.getId()).setTemplateCode(template.getCode())
                .setTemplateType(template.getType()).setTemplateNickname(template.getNickname())
                .setTemplateContent(templateContent).setTemplateParams(templateParams).setReadStatus(false);
        notifyMessageMapper.insert(message);
        return message.getId();
    }
    @Override
    public Long createNotifyMessageV2(Long userId, Integer userType,
                                    NotifyTemplateDO template, String modelName,String templateContent, Map<String, Object> templateParams) {
        NotifyMessageDO message = new NotifyMessageDO().setUserId(userId).setUserType(userType)
                .setTemplateId(template.getId()).setTemplateCode(template.getCode())
                .setTemplateType(template.getType()).setTemplateNickname(template.getNickname())
                .setTemplateContent(templateContent).setTemplateParams(templateParams).setReadStatus(false)
                .setModelName(modelName);
        notifyMessageMapper.insert(message);
        return message.getId();
    }
    @Override
    public Long createNotifyMessageForWarning(Long userId,Long tenantId, Integer userType,
                                    NotifyTemplateDO template, String templateContent, Map<String, Object> templateParams, Map warningMap) {
        Map<String, Long> map = new HashMap();
        TenantUtils.executeIgnore(() -> {
            NotifyMessageDO message = new NotifyMessageDO().setUserId(userId).setUserType(userType)
                    .setTemplateId(template.getId()).setTemplateCode(template.getCode())
                    .setTemplateType(template.getType()).setTemplateNickname(template.getNickname())
                    .setBusinessId(String.valueOf(warningMap.get("businessId")))
                    .setTaskId(String.valueOf(warningMap.get("taskId")))
                    .setRuleId(String.valueOf(warningMap.get("ruleId")))
                    .setWarnName(String.valueOf(warningMap.get("warnName")))
                    .setItemName(String.valueOf(warningMap.get("itemName")))
                    .setModelName(String.valueOf(warningMap.get("modelName")))
                    .setTenantId(tenantId)
                    .setTemplateContent(templateContent).setTemplateParams(templateParams).setReadStatus(false);
            notifyMessageMapper.insert(message);
            map.put("id", message.getId());
        });

        return map.get("id");
    }

    @Override
    public PageResult<NotifyMessageDO> getNotifyMessagePage(NotifyMessagePageReqVO pageReqVO) {
        return notifyMessageMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<NotifyMessageDO> getMyMyNotifyMessagePage(NotifyMessageMyPageReqVO pageReqVO, Long userId, Integer userType) {
        return notifyMessageMapper.selectPage(pageReqVO, userId, userType);
    }

    @Override
    public NotifyMessageDO getNotifyMessage(Long id) {
        return notifyMessageMapper.selectById(id);
    }

    @Override
    public List<NotifyMessageDO> getUnreadNotifyMessageList(Long userId, Integer userType, Integer size) {
        return notifyMessageMapper.selectUnreadListByUserIdAndUserType(userId, userType, size);
    }

    @Override
    public Long getUnreadNotifyMessageCount(Long userId, Integer userType) {
        return notifyMessageMapper.selectUnreadCountByUserIdAndUserType(userId, userType);
    }

    @Override
    public int updateNotifyMessageRead(Collection<Long> ids, Long userId, Integer userType) {
        return notifyMessageMapper.updateListRead(ids, userId, userType);
    }

    @Override
    public int updateAllNotifyMessageRead(Long userId, Integer userType) {
        return notifyMessageMapper.updateListRead(userId, userType);
    }

}

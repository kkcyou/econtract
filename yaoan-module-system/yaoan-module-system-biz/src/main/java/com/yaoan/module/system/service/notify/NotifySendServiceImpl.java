package com.yaoan.module.system.service.notify;

import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.common.enums.UserTypeEnum;
import com.yaoan.module.system.dal.dataobject.notify.NotifyTemplateDO;
import com.google.common.annotations.VisibleForTesting;
import com.yaoan.module.system.enums.notify.NotifyTemplateTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.system.enums.ErrorCodeConstants.*;

/**
 * 站内信发送 Service 实现类
 *
 * @author xrcoder
 */
@Service
@Validated
@Slf4j
public class NotifySendServiceImpl implements NotifySendService {

    @Resource
    private NotifyTemplateService notifyTemplateService;

    @Resource
    private NotifyMessageService notifyMessageService;

    @Override
    public Long sendSingleNotifyToAdmin(Long userId, String templateCode, Map<String, Object> templateParams) {
        return sendSingleNotify(userId, UserTypeEnum.ADMIN.getValue(), templateCode, templateParams);
    }
    @Override
    public Long sendSingleNotifyToAdminV2(Long userId, String templateCode,String modelName, Map<String, Object> templateParams) {
        return sendSingleNotifyV2(userId, UserTypeEnum.ADMIN.getValue(), templateCode,modelName, templateParams);
    }
    @Override
    public Long sendSingleNotifyToMember(Long userId, String templateCode, Map<String, Object> templateParams) {
        return sendSingleNotify(userId, UserTypeEnum.MEMBER.getValue(), templateCode, templateParams);
    }

    @Override
    public Long sendSingleNotify(Long userId, Integer userType, String templateCode, Map<String, Object> templateParams) {
        // 校验模版
        NotifyTemplateDO template = validateNotifyTemplate(templateCode);
        if (Objects.equals(template.getStatus(), CommonStatusEnum.DISABLE.getStatus())) {
            log.info("[sendSingleNotify][模版({})已经关闭，无法给用户({}/{})发送]", templateCode, userId, userType);
            return null;
        }
        // 校验参数
        validateTemplateParams(template, templateParams);

        // 发送站内信
        String content = notifyTemplateService.formatNotifyTemplateContent(template.getContent(), templateParams);
        return notifyMessageService.createNotifyMessage(userId, userType, template, content, templateParams);
    }
    @Override
    public Long sendSingleNotifyV2(Long userId, Integer userType, String templateCode, String modelName,Map<String, Object> templateParams) {
        // 校验模版
        NotifyTemplateDO template = validateNotifyTemplate(templateCode);
        if (Objects.equals(template.getStatus(), CommonStatusEnum.DISABLE.getStatus())) {
            log.info("[sendSingleNotify][模版({})已经关闭，无法给用户({}/{})发送]", templateCode, userId, userType);
            return null;
        }
        // 校验参数
        validateTemplateParams(template, templateParams);

        // 发送站内信
        String content = notifyTemplateService.formatNotifyTemplateContent(template.getContent(), templateParams);
        return notifyMessageService.createNotifyMessageV2(userId, userType, template, modelName,content, templateParams);
    }

    @Override
    public Long sendSingleNotifyForWarning(Long userId,Long tenantId, Integer userType, String template, Map<String, Object> templateParams, Map warningMap) {
        // 发送站内信
        String content = notifyTemplateService.formatNotifyTemplateContent(template, templateParams);
        return notifyMessageService.createNotifyMessageForWarning(userId,tenantId, userType, new NotifyTemplateDO().setId(1L).setCode("1").setContent(content).setType(NotifyTemplateTypeEnum.WARNING_MESSAGE.getType()).setNickname("系统管理员"), content, templateParams, warningMap);
    }

    @VisibleForTesting
    public NotifyTemplateDO validateNotifyTemplate(String templateCode) {
        // 获得站内信模板。考虑到效率，从缓存中获取
        NotifyTemplateDO template = notifyTemplateService.getNotifyTemplateByCodeFromCache(templateCode);
        // 站内信模板不存在
        if (template == null) {
            throw exception(NOTICE_NOT_FOUND);
        }
        return template;
    }

    /**
     * 校验站内信模版参数是否确实
     *
     * @param template 邮箱模板
     * @param templateParams 参数列表
     */
    @VisibleForTesting
    public void validateTemplateParams(NotifyTemplateDO template, Map<String, Object> templateParams) {
        template.getParams().forEach(key -> {
            Object value = templateParams.get(key);
            if (value == null) {
                throw exception(NOTIFY_SEND_TEMPLATE_PARAM_MISS, key);
            }
        });
    }
}

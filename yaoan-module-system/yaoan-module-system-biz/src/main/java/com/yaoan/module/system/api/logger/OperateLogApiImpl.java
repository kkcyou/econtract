package com.yaoan.module.system.api.logger;

import cn.hutool.core.bean.BeanUtil;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.module.system.api.logger.dto.AnonymousOperateLogDTO;
import com.yaoan.module.system.api.logger.dto.OperateLogCreateReqDTO;
import com.yaoan.module.system.service.anonymousoperatelog.AnonymousOperateLogService;
import com.yaoan.module.system.service.logger.OperateLogService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 操作日志 API 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class OperateLogApiImpl implements OperateLogApi {

    @Resource
    private OperateLogService operateLogService;

    @Resource
    private AnonymousOperateLogService anonymousOperateLogService;

    @Override
    public void createOperateLog(OperateLogCreateReqDTO createReqDTO) {
        operateLogService.createOperateLog(createReqDTO);
    }

    @Override
    public void createAnonymousOperateLog(OperateLogCreateReqDTO createReqDTO) {
        AnonymousOperateLogDTO anonymousOperateLogDTO = BeanUtil.toBean(createReqDTO, AnonymousOperateLogDTO.class);
        anonymousOperateLogDTO.setSessionId(createReqDTO.getUserId());
        anonymousOperateLogService.createAnonymousOperateLog(anonymousOperateLogDTO);
    }
}

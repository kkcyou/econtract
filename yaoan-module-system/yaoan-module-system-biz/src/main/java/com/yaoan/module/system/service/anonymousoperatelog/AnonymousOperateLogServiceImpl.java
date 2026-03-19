package com.yaoan.module.system.service.anonymousoperatelog;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.module.system.api.logger.dto.AnonymousOperateLogDTO;
import com.yaoan.module.system.controller.admin.oauth2.vo.client.OAuth2JWTReqVO;
import com.yaoan.module.system.controller.admin.oauth2.vo.token.OAuth2AccessJWTRespVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yaoan.module.system.controller.admin.anonymousoperatelog.vo.*;
import com.yaoan.module.system.dal.dataobject.anonymousoperatelog.AnonymousOperateLogDO;
import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.system.convert.anonymousoperatelog.AnonymousOperateLogConvert;
import com.yaoan.module.system.dal.mysql.anonymousoperatelog.AnonymousOperateLogMapper;

/**
 * 匿名用户操作日志记录 Service 实现类
 *
 * @author doujiale
 */
@Service
@Validated
public class AnonymousOperateLogServiceImpl implements AnonymousOperateLogService {

    @Resource
    private AnonymousOperateLogMapper anonymousOperateLogMapper;
    @Resource
    private RedisUtils redisUtils;

    @Override
    public Long createAnonymousOperateLog(AnonymousOperateLogDTO createReqVO) {
        // 插入
        AnonymousOperateLogDO anonymousOperateLog = AnonymousOperateLogConvert.INSTANCE.convert(createReqVO);
        OAuth2JWTReqVO oAuth2JWTReqVO = BeanUtil.toBean(JSON.parse( redisUtils.get("contract:session:" + createReqVO.getSessionId()).toString()), OAuth2JWTReqVO.class);
        if (oAuth2JWTReqVO != null) {
            anonymousOperateLog.setJwtData(redisUtils.get("contract:session:" + createReqVO.getSessionId()))
                    .setAud(oAuth2JWTReqVO.getAud())
                    .setIss(oAuth2JWTReqVO.getIss())
                    .setBizId(oAuth2JWTReqVO.getBizId());
        }
        anonymousOperateLogMapper.insert(anonymousOperateLog);
        // 返回
        return anonymousOperateLog.getId();
    }

    @Override
    public void deleteAnonymousOperateLog(Long id) {
        // 校验存在
        validateAnonymousOperateLogExists(id);
        // 删除
        anonymousOperateLogMapper.deleteById(id);
    }

    private void validateAnonymousOperateLogExists(Long id) {
        if (anonymousOperateLogMapper.selectById(id) == null) {
//            throw exception(ANONYMOUS_OPERATE_LOG_NOT_EXISTS);
        }
    }

    @Override
    public AnonymousOperateLogDO getAnonymousOperateLog(Long id) {
        return anonymousOperateLogMapper.selectById(id);
    }

    @Override
    public List<AnonymousOperateLogDO> getAnonymousOperateLogList(Collection<Long> ids) {
        return anonymousOperateLogMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<AnonymousOperateLogDO> getAnonymousOperateLogPage(AnonymousOperateLogPageReqVO pageReqVO) {
        return anonymousOperateLogMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AnonymousOperateLogDO> getAnonymousOperateLogList(AnonymousOperateLogExportReqVO exportReqVO) {
        return anonymousOperateLogMapper.selectList(exportReqVO);
    }

}

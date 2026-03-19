package com.yaoan.module.system.dal.mysql.anonymousoperatelog;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.system.dal.dataobject.anonymousoperatelog.AnonymousOperateLogDO;
import org.apache.ibatis.annotations.Mapper;
import com.yaoan.module.system.controller.admin.anonymousoperatelog.vo.*;

/**
 * 匿名用户操作日志记录 Mapper
 *
 * @author doujiale
 */
@Mapper
public interface AnonymousOperateLogMapper extends BaseMapperX<AnonymousOperateLogDO> {

    default PageResult<AnonymousOperateLogDO> selectPage(AnonymousOperateLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AnonymousOperateLogDO>()
                .eqIfPresent(AnonymousOperateLogDO::getTraceId, reqVO.getTraceId())
                .eqIfPresent(AnonymousOperateLogDO::getSessionId, reqVO.getSessionId())
                .eqIfPresent(AnonymousOperateLogDO::getJwtData, reqVO.getJwtData())
                .eqIfPresent(AnonymousOperateLogDO::getModule, reqVO.getModule())
                .likeIfPresent(AnonymousOperateLogDO::getName, reqVO.getName())
                .eqIfPresent(AnonymousOperateLogDO::getType, reqVO.getType())
                .eqIfPresent(AnonymousOperateLogDO::getContent, reqVO.getContent())
                .eqIfPresent(AnonymousOperateLogDO::getExts, reqVO.getExts())
                .eqIfPresent(AnonymousOperateLogDO::getRequestMethod, reqVO.getRequestMethod())
                .eqIfPresent(AnonymousOperateLogDO::getRequestUrl, reqVO.getRequestUrl())
                .eqIfPresent(AnonymousOperateLogDO::getUserIp, reqVO.getUserIp())
                .eqIfPresent(AnonymousOperateLogDO::getUserAgent, reqVO.getUserAgent())
                .eqIfPresent(AnonymousOperateLogDO::getJavaMethod, reqVO.getJavaMethod())
                .eqIfPresent(AnonymousOperateLogDO::getJavaMethodArgs, reqVO.getJavaMethodArgs())
                .betweenIfPresent(AnonymousOperateLogDO::getStartTime, reqVO.getStartTime())
                .eqIfPresent(AnonymousOperateLogDO::getDuration, reqVO.getDuration())
                .eqIfPresent(AnonymousOperateLogDO::getResultCode, reqVO.getResultCode())
                .eqIfPresent(AnonymousOperateLogDO::getResultMsg, reqVO.getResultMsg())
                .eqIfPresent(AnonymousOperateLogDO::getResultData, reqVO.getResultData())
                .betweenIfPresent(AnonymousOperateLogDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AnonymousOperateLogDO::getIss, reqVO.getIss())
                .eqIfPresent(AnonymousOperateLogDO::getAud, reqVO.getAud())
                .eqIfPresent(AnonymousOperateLogDO::getBizId, reqVO.getBizId())
                .orderByDesc(AnonymousOperateLogDO::getId));
    }

    default List<AnonymousOperateLogDO> selectList(AnonymousOperateLogExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<AnonymousOperateLogDO>()
                .eqIfPresent(AnonymousOperateLogDO::getTraceId, reqVO.getTraceId())
                .eqIfPresent(AnonymousOperateLogDO::getSessionId, reqVO.getSessionId())
                .eqIfPresent(AnonymousOperateLogDO::getJwtData, reqVO.getJwtData())
                .eqIfPresent(AnonymousOperateLogDO::getModule, reqVO.getModule())
                .likeIfPresent(AnonymousOperateLogDO::getName, reqVO.getName())
                .eqIfPresent(AnonymousOperateLogDO::getType, reqVO.getType())
                .eqIfPresent(AnonymousOperateLogDO::getContent, reqVO.getContent())
                .eqIfPresent(AnonymousOperateLogDO::getExts, reqVO.getExts())
                .eqIfPresent(AnonymousOperateLogDO::getRequestMethod, reqVO.getRequestMethod())
                .eqIfPresent(AnonymousOperateLogDO::getRequestUrl, reqVO.getRequestUrl())
                .eqIfPresent(AnonymousOperateLogDO::getUserIp, reqVO.getUserIp())
                .eqIfPresent(AnonymousOperateLogDO::getUserAgent, reqVO.getUserAgent())
                .eqIfPresent(AnonymousOperateLogDO::getJavaMethod, reqVO.getJavaMethod())
                .eqIfPresent(AnonymousOperateLogDO::getJavaMethodArgs, reqVO.getJavaMethodArgs())
                .betweenIfPresent(AnonymousOperateLogDO::getStartTime, reqVO.getStartTime())
                .eqIfPresent(AnonymousOperateLogDO::getDuration, reqVO.getDuration())
                .eqIfPresent(AnonymousOperateLogDO::getResultCode, reqVO.getResultCode())
                .eqIfPresent(AnonymousOperateLogDO::getResultMsg, reqVO.getResultMsg())
                .eqIfPresent(AnonymousOperateLogDO::getResultData, reqVO.getResultData())
                .betweenIfPresent(AnonymousOperateLogDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AnonymousOperateLogDO::getIss, reqVO.getIss())
                .eqIfPresent(AnonymousOperateLogDO::getAud, reqVO.getAud())
                .eqIfPresent(AnonymousOperateLogDO::getBizId, reqVO.getBizId())
                .orderByDesc(AnonymousOperateLogDO::getId));
    }

}

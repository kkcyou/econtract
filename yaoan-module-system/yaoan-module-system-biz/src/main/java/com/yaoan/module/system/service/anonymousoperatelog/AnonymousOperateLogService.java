package com.yaoan.module.system.service.anonymousoperatelog;

import java.util.*;
import javax.validation.*;

import com.yaoan.module.system.api.logger.dto.AnonymousOperateLogDTO;
import com.yaoan.module.system.controller.admin.anonymousoperatelog.vo.*;
import com.yaoan.module.system.dal.dataobject.anonymousoperatelog.AnonymousOperateLogDO;
import com.yaoan.framework.common.pojo.PageResult;

/**
 * 匿名用户操作日志记录 Service 接口
 *
 * @author doujiale
 */
public interface AnonymousOperateLogService {

    /**
     * 创建匿名用户操作日志记录
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAnonymousOperateLog(@Valid AnonymousOperateLogDTO createReqVO);

    /**
     * 删除匿名用户操作日志记录
     *
     * @param id 编号
     */
    void deleteAnonymousOperateLog(Long id);

    /**
     * 获得匿名用户操作日志记录
     *
     * @param id 编号
     * @return 匿名用户操作日志记录
     */
    AnonymousOperateLogDO getAnonymousOperateLog(Long id);

    /**
     * 获得匿名用户操作日志记录列表
     *
     * @param ids 编号
     * @return 匿名用户操作日志记录列表
     */
    List<AnonymousOperateLogDO> getAnonymousOperateLogList(Collection<Long> ids);

    /**
     * 获得匿名用户操作日志记录分页
     *
     * @param pageReqVO 分页查询
     * @return 匿名用户操作日志记录分页
     */
    PageResult<AnonymousOperateLogDO> getAnonymousOperateLogPage(AnonymousOperateLogPageReqVO pageReqVO);

    /**
     * 获得匿名用户操作日志记录列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 匿名用户操作日志记录列表
     */
    List<AnonymousOperateLogDO> getAnonymousOperateLogList(AnonymousOperateLogExportReqVO exportReqVO);

}

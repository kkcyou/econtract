package com.yaoan.module.econtract.service.warningparam;

import java.util.*;
import javax.validation.*;
import com.yaoan.module.econtract.controller.admin.warningparam.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningparam.WarningParamDO;
import com.yaoan.framework.common.pojo.PageResult;

/**
 * 预警消息模板参数(new预警) Service 接口
 *
 * @author admin
 */
public interface WarningParamService {

    /**
     * 创建预警消息模板参数(new预警)
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWarningParam(@Valid WarningParamCreateReqVO createReqVO);

    /**
     * 更新预警消息模板参数(new预警)
     *
     * @param updateReqVO 更新信息
     */
    void updateWarningParam(@Valid WarningParamUpdateReqVO updateReqVO);

    /**
     * 删除预警消息模板参数(new预警)
     *
     * @param id 编号
     */
    void deleteWarningParam(String id);

    /**
     * 获得预警消息模板参数(new预警)
     *
     * @param id 编号
     * @return 预警消息模板参数(new预警)
     */
    WarningParamDO getWarningParam(String id);

    /**
     * 获得预警消息模板参数(new预警)列表
     *
     * @param ids 编号
     * @return 预警消息模板参数(new预警)列表
     */
    List<WarningParamDO> getWarningParamList(Collection<String> ids);

    /**
     * 获得预警消息模板参数(new预警)分页
     *
     * @param pageReqVO 分页查询
     * @return 预警消息模板参数(new预警)分页
     */
    PageResult<WarningParamDO> getWarningParamPage(WarningParamPageReqVO pageReqVO);

    /**
     * 获得预警消息模板参数(new预警)列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 预警消息模板参数(new预警)列表
     */
    List<WarningParamDO> getWarningParamList(WarningParamExportReqVO exportReqVO);

}

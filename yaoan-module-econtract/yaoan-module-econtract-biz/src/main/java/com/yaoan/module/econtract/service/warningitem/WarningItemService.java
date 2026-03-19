package com.yaoan.module.econtract.service.warningitem;

import java.util.*;
import javax.validation.*;
import com.yaoan.module.econtract.controller.admin.warningitem.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningitem.WarningItemDO;
import com.yaoan.framework.common.pojo.PageResult;

/**
 * 预警事项表（new预警） Service 接口
 *
 * @author admin
 */
public interface WarningItemService {

    /**
     * 创建预警事项表（new预警）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWarningItem(@Valid WarningItemCreateReqVO createReqVO);

    /**
     * 更新预警事项表（new预警）
     *
     * @param updateReqVO 更新信息
     */
    void updateWarningItem(@Valid WarningItemUpdateReqVO updateReqVO);

    /**
     * 删除预警事项表（new预警）
     *
     * @param id 编号
     */
    void deleteWarningItem(String id);

    /**
     * 获得预警事项表（new预警）
     *
     * @param id 编号
     * @return 预警事项表（new预警）
     */
    WarningItemDO getWarningItem(String id);

    /**
     * 获得预警事项表（new预警）列表
     *
     * @param ids 编号
     * @return 预警事项表（new预警）列表
     */
    List<WarningItemDO> getWarningItemList(Collection<String> ids);

    /**
     * 获得预警事项表（new预警）分页
     *
     * @param pageReqVO 分页查询
     * @return 预警事项表（new预警）分页
     */
    PageResult<WarningItemDO> getWarningItemPage(WarningItemPageReqVO pageReqVO);

    /**
     * 获得预警事项表（new预警）列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 预警事项表（new预警）列表
     */
    List<WarningItemDO> getWarningItemList(WarningItemExportReqVO exportReqVO);

}

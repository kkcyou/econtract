package com.yaoan.module.econtract.service.watermark;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.watermark.vo.*;
import com.yaoan.module.econtract.controller.admin.watermark.vo.page.WatermarkPageRespVO;
import com.yaoan.module.econtract.dal.dataobject.watermark.WatermarkDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


/**
 * 水印管理 Service 接口
 *
 * @author lls
 */
public interface WatermarkService {

    /**
     * 创建水印管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createWatermark(@Valid WatermarkCreateReqVO createReqVO);

    /**
     * 更新水印管理
     *
     * @param updateReqVO 更新信息
     */
    void updateWatermark(@Valid WatermarkUpdateReqVO updateReqVO);

    /**
     * 删除水印管理
     *
     * @param id 编号
     */
    void deleteWatermark(String id);

    /**
     * 获得水印管理
     *
     * @param id 编号
     * @return 水印管理
     */
    WatermarkRespVO getWatermark(String id);

    /**
     * 获得水印管理列表
     *
     * @param ids 编号
     * @return 水印管理列表
     */
    List<WatermarkDO> getWatermarkList(Collection<String> ids);

    /**
     * 获得水印管理分页
     *
     * @param pageReqVO 分页查询
     * @return 水印管理分页
     */
    PageResult<WatermarkPageRespVO> getWatermarkPage(WatermarkPageReqVO pageReqVO);

    /**
     * 获得水印管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 水印管理列表
     */
    List<WatermarkDO> getWatermarkList(WatermarkExportReqVO exportReqVO);

}

package com.yaoan.module.econtract.dal.mysql.warningitem;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.warningitem.WarningItemDO;
import org.apache.ibatis.annotations.Mapper;
import com.yaoan.module.econtract.controller.admin.warningitem.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 预警事项表（new预警） Mapper
 *
 * @author admin
 */
@Mapper
public interface WarningItemMapper extends BaseMapperX<WarningItemDO> {

    default PageResult<WarningItemDO> selectPage(WarningItemPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WarningItemDO>()
                .eqIfPresent(WarningItemDO::getConfigId, reqVO.getConfigId())
                .likeIfPresent(WarningItemDO::getItemName, reqVO.getItemName())
                .eqIfPresent(WarningItemDO::getItemRemark, reqVO.getItemRemark())
                .betweenIfPresent(WarningItemDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningItemDO::getId));
    }

    default List<WarningItemDO> selectList(WarningItemExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<WarningItemDO>()
                .eqIfPresent(WarningItemDO::getConfigId, reqVO.getConfigId())
                .likeIfPresent(WarningItemDO::getItemName, reqVO.getItemName())
                .eqIfPresent(WarningItemDO::getItemRemark, reqVO.getItemRemark())
                .betweenIfPresent(WarningItemDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningItemDO::getId));
    }

    @Update("<script>"
            + "UPDATE ecms_warning_item "
            + "SET deleted = 1 "
            + "WHERE config_id = #{configId}"
            + "</script>")
    void deleteBatch(@Param("configId")  String configId);


}

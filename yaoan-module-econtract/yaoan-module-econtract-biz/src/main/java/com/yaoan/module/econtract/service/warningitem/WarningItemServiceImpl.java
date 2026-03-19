package com.yaoan.module.econtract.service.warningitem;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yaoan.module.econtract.controller.admin.warningitem.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningitem.WarningItemDO;
import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.convert.warningitem.WarningItemConvert;
import com.yaoan.module.econtract.dal.mysql.warningitem.WarningItemMapper;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 预警事项表（new预警） Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class WarningItemServiceImpl implements WarningItemService {

    @Resource
    private WarningItemMapper warningItemMapper;

    @Override
    public String createWarningItem(WarningItemCreateReqVO createReqVO) {
        // 插入
        WarningItemDO warningItem = WarningItemConvert.INSTANCE.convert(createReqVO);
        warningItemMapper.insert(warningItem);
        // 返回
        return warningItem.getId();
    }

    @Override
    public void updateWarningItem(WarningItemUpdateReqVO updateReqVO) {
        // 校验存在
        validateWarningItemExists(updateReqVO.getId());
        // 更新
        WarningItemDO updateObj = WarningItemConvert.INSTANCE.convert(updateReqVO);
        warningItemMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarningItem(String id) {
        // 校验存在
        validateWarningItemExists(id);
        // 删除
        warningItemMapper.deleteById(id);
    }

    private void validateWarningItemExists(String id) {
        if (warningItemMapper.selectById(id) == null) {
//            throw exception(WARNING_ITEM_NOT_EXISTS);
        }
    }

    @Override
    public WarningItemDO getWarningItem(String id) {
        return warningItemMapper.selectById(id);
    }

    @Override
    public List<WarningItemDO> getWarningItemList(Collection<String> ids) {
        return warningItemMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<WarningItemDO> getWarningItemPage(WarningItemPageReqVO pageReqVO) {
        return warningItemMapper.selectPage(pageReqVO);
    }

    @Override
    public List<WarningItemDO> getWarningItemList(WarningItemExportReqVO exportReqVO) {
        return warningItemMapper.selectList(exportReqVO);
    }

}

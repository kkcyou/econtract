package com.yaoan.module.econtract.service.watermark;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.watermark.vo.*;
import com.yaoan.module.econtract.controller.admin.watermark.vo.page.WatermarkPageRespVO;
import com.yaoan.module.econtract.convert.watermark.WatermarkConvert;
import com.yaoan.module.econtract.dal.dataobject.watermark.WatermarkDO;
import com.yaoan.module.econtract.dal.mysql.watermark.WatermarkMapper;
import com.yaoan.module.econtract.enums.watermark.WatermarkTypeEnums;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;

/**
 * 水印管理 Service 实现类
 *
 * @author lls
 */
@Service
@Validated
public class WatermarkServiceImpl implements WatermarkService {

    @Resource
    private WatermarkMapper watermarkMapper;

    @Override
    public String createWatermark(WatermarkCreateReqVO createReqVO) {
        // 插入
        WatermarkDO watermark = WatermarkConvert.INSTANCE.convert(createReqVO);
        watermarkMapper.insert(watermark);
        // 返回
        return watermark.getId();
    }

    @Override
    public void updateWatermark(WatermarkUpdateReqVO updateReqVO) {
        // 校验存在
        validateWatermarkExists(updateReqVO.getId());
        // 更新
        WatermarkDO updateObj = WatermarkConvert.INSTANCE.convert(updateReqVO);
        watermarkMapper.updateById(updateObj);
    }

    @Override
    public void deleteWatermark(String id) {
        // 校验存在
        validateWatermarkExists(id);
        // 删除
        watermarkMapper.deleteById(id);
    }

    private void validateWatermarkExists(String id) {
        if (watermarkMapper.selectById(id) == null) {
            throw exception(SYSTEM_ERROR, "水印已被删除");
        }
    }

    @Override
    public WatermarkRespVO getWatermark(String id) {
        WatermarkDO watermark = watermarkMapper.selectById(id);
        if (ObjectUtil.isNull(watermark)) {
            return null;
        }
        WatermarkRespVO respVO = WatermarkConvert.INSTANCE.convert(watermark);
        WatermarkTypeEnums typeEnums =WatermarkTypeEnums.getInstance(respVO.getType()) ;
        if(ObjectUtil.isNotNull(typeEnums)) {
            respVO.setTypeName(typeEnums.getInfo());
        }
        return respVO;
    }

    @Override
    public List<WatermarkDO> getWatermarkList(Collection<String> ids) {
        return watermarkMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<WatermarkPageRespVO> getWatermarkPage(WatermarkPageReqVO pageReqVO) {
         PageResult<WatermarkDO>  watermarkDOPageResult =   watermarkMapper.selectPage(pageReqVO);
         if(CollectionUtil.isEmpty(watermarkDOPageResult.getList())){
             return new PageResult<WatermarkPageRespVO>().setTotal(watermarkDOPageResult.getTotal()).setTotal(0L);
         }
        PageResult<WatermarkPageRespVO> result = WatermarkConvert.INSTANCE.pageDo2Resp(watermarkDOPageResult);
        return enhancePage(result);
    }

    private PageResult<WatermarkPageRespVO> enhancePage(PageResult<WatermarkPageRespVO> result) {
        for (WatermarkPageRespVO respVO : result.getList()) {
            WatermarkTypeEnums typeEnums =WatermarkTypeEnums.getInstance(respVO.getType()) ;
            if(ObjectUtil.isNotNull(typeEnums)) {
                respVO.setTypeName(typeEnums.getInfo());
            }
        }
        return result;
    }

    @Override
    public List<WatermarkDO> getWatermarkList(WatermarkExportReqVO exportReqVO) {
        return watermarkMapper.selectList(exportReqVO);
    }

}

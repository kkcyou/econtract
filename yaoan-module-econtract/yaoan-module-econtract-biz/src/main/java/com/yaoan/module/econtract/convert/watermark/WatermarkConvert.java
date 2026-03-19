package com.yaoan.module.econtract.convert.watermark;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.watermark.vo.WatermarkCreateReqVO;
import com.yaoan.module.econtract.controller.admin.watermark.vo.WatermarkExcelVO;
import com.yaoan.module.econtract.controller.admin.watermark.vo.WatermarkRespVO;
import com.yaoan.module.econtract.controller.admin.watermark.vo.WatermarkUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.watermark.vo.page.WatermarkPageRespVO;
import com.yaoan.module.econtract.dal.dataobject.watermark.WatermarkDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


/**
 * 水印管理 Convert
 *
 * @author lls
 */
@Mapper
public interface WatermarkConvert {

    WatermarkConvert INSTANCE = Mappers.getMapper(WatermarkConvert.class);

    WatermarkDO convert(WatermarkCreateReqVO bean);

    WatermarkDO convert(WatermarkUpdateReqVO bean);

    WatermarkRespVO convert(WatermarkDO bean);

    List<WatermarkRespVO> convertList(List<WatermarkDO> list);

    PageResult<WatermarkRespVO> convertPage(PageResult<WatermarkDO> page);

    List<WatermarkExcelVO> convertList02(List<WatermarkDO> list);

    PageResult<WatermarkPageRespVO> pageDo2Resp(PageResult<WatermarkDO> watermarkDOPageResult);
}

package com.yaoan.module.econtract.convert.reviewresult;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yaoan.module.econtract.controller.admin.reviewresult.vo.*;
import com.yaoan.module.econtract.dal.dataobject.reviewresult.ReviewResultDO;

/**
 * 智能审查结果 Convert
 *
 * @author admin
 */
@Mapper
public interface ReviewResultConvert {

    ReviewResultConvert INSTANCE = Mappers.getMapper(ReviewResultConvert.class);

    ReviewResultDO convert(ReviewResultCreateReqVO bean);

    ReviewResultDO convert(ReviewResultUpdateReqVO bean);

    ReviewResultRespVO convert(ReviewResultDO bean);

    List<ReviewResultRespVO> convertList(List<ReviewResultDO> list);

    PageResult<ReviewResultRespVO> convertPage(PageResult<ReviewResultDO> page);

    List<ReviewResultExcelVO> convertList02(List<ReviewResultDO> list);

}

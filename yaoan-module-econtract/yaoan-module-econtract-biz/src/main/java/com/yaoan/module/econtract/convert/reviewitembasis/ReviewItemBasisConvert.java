package com.yaoan.module.econtract.convert.reviewitembasis;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.controller.admin.reviewitembasis.vo.*;
import com.yaoan.module.econtract.dal.dataobject.reviewitembasis.ReviewItemBasisDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * 合同审查规则依据 Convert
 *
 * @author admin
 */
@Mapper
public interface ReviewItemBasisConvert {

    ReviewItemBasisConvert INSTANCE = Mappers.getMapper(ReviewItemBasisConvert.class);

    ReviewItemBasisDO convert(ReviewItemBasisCreateReqVO bean);

    ReviewItemBasisDO convert(ReviewItemBasisUpdateReqVO bean);

    ReviewItemBasisRespVO convert(ReviewItemBasisDO bean);

    List<ReviewItemBasisRespVO> convertList(List<ReviewItemBasisDO> list);

    PageResult<ReviewItemBasisRespVO> convertPage(PageResult<ReviewItemBasisDO> page);

    List<ReviewItemBasisExcelVO> convertList02(List<ReviewItemBasisDO> list);

    List<ReviewItemBasisDO> convertList03(List<ReviewItemBasisBaseVO> list);

    List<ReviewItemBasisBaseVO> convertList04(List<ReviewItemBasisDO> list);



}

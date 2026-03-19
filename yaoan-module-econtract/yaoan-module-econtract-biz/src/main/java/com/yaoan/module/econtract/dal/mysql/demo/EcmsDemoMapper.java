package com.yaoan.module.econtract.dal.mysql.demo;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.demo.EcmsDemo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * demo Mapper 接口
 * </p>
 *
 * @author doujl
 * @since 2023-07-04
 */
@Mapper
public interface EcmsDemoMapper extends BaseMapperX<EcmsDemo> {

    default PageResult<EcmsDemo> selectPage(DemoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<EcmsDemo>()
                .likeIfPresent(EcmsDemo::getName, reqVO.getName()));
    }

    List<EcmsDemo> selectXmlList();


}

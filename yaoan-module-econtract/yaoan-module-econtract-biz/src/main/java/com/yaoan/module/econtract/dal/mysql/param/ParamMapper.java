package com.yaoan.module.econtract.dal.mysql.param;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.param.vo.ParamPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.param.Param;
import dm.jdbc.util.StringUtil;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Objects;


/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
@Mapper
public interface ParamMapper extends BaseMapperX<Param> {
    /**
     * 查询参数信息
     *
     * @param paramPageReqVO
     * @return
     */
    default PageResult<Param> selectSentPage(ParamPageReqVO paramPageReqVO) {
        LambdaQueryWrapperX<Param> queryWrapperX = new LambdaQueryWrapperX<>();
        if (StringUtil.isNotEmpty(paramPageReqVO.getSearchText())) {
            queryWrapperX.like(Param::getName, paramPageReqVO.getSearchText()).or()
                    .like(Param::getCreator, paramPageReqVO.getSearchText());
        }
        queryWrapperX
                .orderByDesc(Param::getCreateTime)
                .eqIfPresent(Param::getCategoryId, paramPageReqVO.getCategoryId())
                .eqIfPresent(Param::getId, paramPageReqVO.getId())
                .likeIfPresent(Param::getName, paramPageReqVO.getName())

                .betweenIfPresent(Param::getCreateTime, paramPageReqVO.getStartDate(), paramPageReqVO.getEndDate())
                //查询租户为0 和当前租户的参数
                .in(Param::getTenantId,0, Objects.requireNonNull(SecurityFrameworkUtils.getLoginUser()).getTenantId())
                ;
        return selectPage(paramPageReqVO, queryWrapperX);
    }

    /**
     * 校验名称是否存在
     *
     * @param
     * @return
     */
    default Boolean nameExist(String id, String name) {
        return selectCount(new LambdaQueryWrapperX<Param>()
                .eqIfPresent(Param::getName, name)
                .neIfPresent(Param::getId, id)) > 0;
    }

    /**
     * 校验编码是否重复
     *
     * @param id
     * @param code
     * @return
     */
    default Boolean codeExist(String id, String code) {
        return selectCount(new LambdaQueryWrapperX<Param>()
                .eqIfPresent(Param::getCode, code)
                .neIfPresent(Param::getId, id)) > 0;
    }

    /**
     * 根据条件匹配查询参数信息
     *
     * @param name
     * @param categoryId
     * @param Ids
     * @return
     */
    default List<Param> selectList(String name, Integer categoryId, List<String> Ids,String contractType) {
        return selectList(new LambdaQueryWrapperX<Param>()
                .eqIfPresent(Param::getCategoryId, categoryId)
                .eqIfPresent(Param::getContractType, contractType)
                .inIfPresent(Param::getId, Ids)
                //模糊匹配name
                .likeIfPresent(Param::getName, name));
    }

}

package com.yaoan.module.econtract.dal.mysql.model;

import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.outward.template.vo.ModelApiListReqVO;
import com.yaoan.module.econtract.dal.dataobject.catalog.ModelCatalogDo;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.DraftOrderInfoDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.GoodsDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageDetailInfoDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageInfoDO;
import com.yaoan.module.econtract.dal.dataobject.model.SimpleModel;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.enums.order.ProjectCategoryEnums;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: doujl
 * @date: 2023/8/3 15:37
 */
@Mapper
public interface SimpleModelMapper extends BaseMapperX<SimpleModel> {
    /**
     * 模板列表
     */
    default PageResult<SimpleModel> selectPage(ModelApiListReqVO vo) {
        return selectPage(vo, new LambdaQueryWrapperX<SimpleModel>()
                .likeIfPresent(SimpleModel::getName, vo.getTemplateName())
                .likeIfPresent(SimpleModel::getCode, vo.getTemplateCode())
                .eqIfPresent(SimpleModel::getRegionCode, vo.getRegionCode())
                .eqIfPresent(SimpleModel::getCategoryId, vo.getCategoryId())
                .inIfPresent(SimpleModel::getCategoryId, vo.getCategoryIdList())
        );
    }

    /**
     * 模板查询
     */
    default PageResult<SimpleModel> searchList(ModelApiListReqVO vo) {
        return selectPage(vo, new LambdaQueryWrapperX<SimpleModel>()
                .likeIfPresent(SimpleModel::getName, vo.getTemplateName())
                .likeIfPresent(SimpleModel::getCode, vo.getTemplateCode())
                .eqIfPresent(SimpleModel::getRegionCode, vo.getRegionCode())
                .eqIfPresent(SimpleModel::getCategoryId, vo.getCategoryId())
                .inIfPresent(SimpleModel::getCategoryId, vo.getCategoryIdList())
        );
    }


    /**
     * 匹配当前起草订单所属合同类型，与当前采购包或订单相同交易类型(电子交易/框架协议/协议定点!.)的模板，
     * 优先有品目的
     */
    default List<SimpleModel> getModel4OrderCatalog(DraftOrderInfoDO draftOrderInfoDO) {
        MPJLambdaWrapper<SimpleModel> wrapperX = new MPJLambdaWrapper<SimpleModel>();
        wrapperX.leftJoin(ContractType.class, ContractType::getId, SimpleModel::getContractType)
                .leftJoin(ModelCatalogDo.class, ModelCatalogDo::getModelId, SimpleModel::getId)
                .leftJoin(GoodsDO.class, GoodsDO::getPurCatalogCode, ModelCatalogDo::getCatalogCode)
                .selectAll(SimpleModel.class)
                .eq(SimpleModel::getApproveStatus,2)

                //订单品目
                .eq(GoodsDO::getOrderId, draftOrderInfoDO.getOrderGuid())
                //业务类型
                .eq(SimpleModel::getPlatform, draftOrderInfoDO.getContractFrom())
                .eq(SimpleModel::getEffectStatus, 1)
        ;
        ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(draftOrderInfoDO.getProjectCategoryCode());
        // 合同类型
        wrapperX.eq(ContractType::getPlatId, projectCategoryEnums.getValue());
        wrapperX.orderByDesc(SimpleModel::getUpdateTime);
        return selectList(wrapperX);
    }

    default List<SimpleModel> getModel4OrderNoCatalog(DraftOrderInfoDO draftOrderInfoDO) {
        MPJLambdaWrapper<SimpleModel> wrapperX = new MPJLambdaWrapper<SimpleModel>();
        wrapperX.leftJoin(ContractType.class, ContractType::getId, SimpleModel::getContractType)
                .selectAll(SimpleModel.class)
                .eq(SimpleModel::getPlatform, draftOrderInfoDO.getContractFrom())
                .eq(SimpleModel::getEffectStatus, 1)
                .eq(SimpleModel::getApproveStatus,2)

        ;
        ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(draftOrderInfoDO.getProjectCategoryCode());
        wrapperX.eq(ContractType::getPlatId, projectCategoryEnums.getValue());
        wrapperX.orderByDesc(SimpleModel::getUpdateTime);
        return selectList(wrapperX);
    }

    default List<SimpleModel> getModel4OrderAllPlatform(DraftOrderInfoDO draftOrderInfoDO) {
        MPJLambdaWrapper<SimpleModel> wrapperX = new MPJLambdaWrapper<SimpleModel>();
        wrapperX.leftJoin(ContractType.class, ContractType::getId, SimpleModel::getContractType)
                .selectAll(SimpleModel.class)
                .eq(SimpleModel::getPlatform, PlatformEnums.ALL.getCode())
                .eq(SimpleModel::getEffectStatus, 1)
                .eq(SimpleModel::getApproveStatus,2)

        ;
        ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(draftOrderInfoDO.getProjectCategoryCode());
        wrapperX.eq(ContractType::getPlatId, projectCategoryEnums.getValue());
        wrapperX.orderByDesc(SimpleModel::getUpdateTime);
        return selectList(wrapperX);
    }

    default List<SimpleModel> getModel4PackageCatalog(PackageInfoDO packageInfoDO) {
        MPJLambdaWrapper<SimpleModel> wrapperX = new MPJLambdaWrapper<SimpleModel>();
        wrapperX.leftJoin(ContractType.class, ContractType::getId, SimpleModel::getContractType)
                .leftJoin(ModelCatalogDo.class, ModelCatalogDo::getModelId, SimpleModel::getId)
                .leftJoin(PackageDetailInfoDO.class, PackageDetailInfoDO::getCatalogueCode, ModelCatalogDo::getCatalogCode)
                .selectAll(SimpleModel.class)
                //订单品目
                .eq(PackageDetailInfoDO::getPackageGuid, packageInfoDO.getPackageGuid())
                //业务类型
                .eq(SimpleModel::getPlatform, PlatformEnums.GPMS_GPX.getCode())
                .eq(SimpleModel::getEffectStatus, 1)
                .eq(SimpleModel::getApproveStatus,2)

        ;
        ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(packageInfoDO.getProjectType());
        // 合同类型
        if (ObjectUtil.isNotNull(projectCategoryEnums)) {
            wrapperX.eq(ContractType::getPlatId, projectCategoryEnums.getValue());
        }
        wrapperX.orderByDesc(SimpleModel::getUpdateTime);
        return selectList(wrapperX);
    }

    default List<SimpleModel> getModel4PackageNoCatalog(PackageInfoDO packageInfoDO) {
        MPJLambdaWrapper<SimpleModel> wrapperX = new MPJLambdaWrapper<SimpleModel>();
        wrapperX.leftJoin(ContractType.class, ContractType::getId, SimpleModel::getContractType)
                .selectAll(SimpleModel.class)
                //业务类型
                .eq(SimpleModel::getPlatform, PlatformEnums.GPMS_GPX.getCode())
                .eq(SimpleModel::getEffectStatus, 1)
                .eq(SimpleModel::getApproveStatus,2)

        ;
        ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(packageInfoDO.getProjectType());
        // 合同类型
        if (ObjectUtil.isNotNull(projectCategoryEnums)) {
            wrapperX.eq(ContractType::getPlatId, projectCategoryEnums.getValue());
        }
        wrapperX.orderByDesc(SimpleModel::getUpdateTime);
        return selectList(wrapperX);
    }

    default List<SimpleModel> getModel4PackageAllPlatform(PackageInfoDO packageInfoDO) {
        MPJLambdaWrapper<SimpleModel> wrapperX = new MPJLambdaWrapper<SimpleModel>();
        wrapperX.leftJoin(ContractType.class, ContractType::getId, SimpleModel::getContractType)
                .selectAll(SimpleModel.class)
                .eq(SimpleModel::getApproveStatus,2)
                //业务类型
                .eq(SimpleModel::getPlatform, PlatformEnums.ALL.getCode())
                .eq(SimpleModel::getEffectStatus, 1)
        ;
        ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(packageInfoDO.getProjectType());
        // 合同类型
        if (ObjectUtil.isNotNull(projectCategoryEnums)) {
            wrapperX.eq(ContractType::getPlatId, projectCategoryEnums.getValue());
        }
        wrapperX.orderByDesc(SimpleModel::getUpdateTime);
        return selectList(wrapperX);
    }

    // 统计数量，为了避免编号重复，删除的数量也查出来
    @Select("SELECT COUNT( * ) AS total FROM ecms_model WHERE  (create_time BETWEEN  #{startTime} AND #{endTime})")
    Long selectAllCountWithTime(@Param( "startTime") LocalDateTime startTime, @Param( "endTime") LocalDateTime endTime);
}

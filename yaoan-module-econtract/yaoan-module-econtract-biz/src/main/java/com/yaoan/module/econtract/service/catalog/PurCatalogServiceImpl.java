package com.yaoan.module.econtract.service.catalog;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.catalog.vo.ModelIdVO;
import com.yaoan.module.econtract.controller.admin.catalog.vo.PurCatalogShowVO;
import com.yaoan.module.econtract.controller.admin.catalog.vo.PurCatalogVO;
import com.yaoan.module.econtract.convert.modelcategory.ModelCategoryConverter;
import com.yaoan.module.econtract.dal.dataobject.catalog.ModelCatalogDo;
import com.yaoan.module.econtract.dal.dataobject.catalog.PurCatalogDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.DraftOrderInfoDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageInfoDO;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.model.SimpleModel;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ClientModelCategory;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ModelCategory;
import com.yaoan.module.econtract.dal.mysql.catalog.ModelCatalogMapper;
import com.yaoan.module.econtract.dal.mysql.catalog.PurCatalogMapper;
import com.yaoan.module.econtract.dal.mysql.gpx.PackageInfoMapper;
import com.yaoan.module.econtract.dal.mysql.model.SimpleModelMapper;
import com.yaoan.module.econtract.dal.mysql.order.GPMallOrderOldMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.supervise.PurCatalogTypeEnums;
import com.yaoan.module.econtract.enums.templatecategory.PlatformTemplateCategoryEnums;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

@Slf4j
@Service
public class PurCatalogServiceImpl implements PurCatalogService {

    @Resource
    private GPMallOrderOldMapper gpMallOrderMapper;
    @Resource
    private ModelCatalogMapper modelCatalogMapper;
    @Resource
    private SimpleModelMapper simpleModelMapper;
    @Resource
    private PackageInfoMapper packageInfoMapper;
    @Resource
    private PurCatalogMapper purCatalogMapper;
    
    @Override
    public List<PurCatalogShowVO> getPurCatalogByCode(PurCatalogVO purCatalogVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
//        String regionCode = loginUser.getRegionCode();
        LambdaQueryWrapperX<PurCatalogDO> wrapperX = new LambdaQueryWrapperX<PurCatalogDO>()
                .eqIfPresent(PurCatalogDO::getRegionCode, "230101")
                .eqIfPresent(PurCatalogDO::getPurCatalogType, purCatalogVO.getPurCatalogType())
                .eqIfPresent(PurCatalogDO::getKind, purCatalogVO.getKind());
        if (StringUtil.isNotEmpty(purCatalogVO.getPurCatalogCode())) {
            wrapperX.eq(PurCatalogDO::getPurCatalogPcode, purCatalogVO.getPurCatalogCode());
        } else {
            //默认查询所有顶级节点
//            wrapperX.isNull(PurCatalogDO::getPurCatalogPcode);
            wrapperX.apply("pur_catalog_pcode REGEXP '^[A-Za-z0-9]$'");
        }
        List<PurCatalogDO> purCatalogDOS = purCatalogMapper.selectList(wrapperX);
        List<PurCatalogShowVO> showVOS = BeanUtils.toBean(purCatalogDOS,PurCatalogShowVO.class);
        //List<PurCatalogShowVO> showVOS = PurCatalogConverter.INSTANCE.do2ShowVO(purCatalogDOS);
        List<PurCatalogDO> pcodeList = purCatalogMapper.selectList(new LambdaQueryWrapperX<PurCatalogDO>()
                .select(PurCatalogDO::getPurCatalogPcode)
                .isNotNull(PurCatalogDO::getPurCatalogPcode)
                .eq(PurCatalogDO::getRegionCode, "230101")
                .groupBy(PurCatalogDO::getPurCatalogPcode)
        );
        List<String> collect = pcodeList.stream().map(PurCatalogDO::getPurCatalogPcode).collect(Collectors.toList());
        showVOS.forEach(vo -> {
            //判断是否有子节点
            if (collect.contains(vo.getPurCatalogCode())) {
                //不是叶子节点
                vo.setIsLeaf(false);
            } else {
                vo.setIsLeaf(true);
            }
        });
        return showVOS;
    }
    /**
     * 通过订单信息可品目code返回模板id
     *
     * @param orderId
     * @param code
     * @return
     */
    @Override
    @DataPermission(enable = false)
    public List<ModelIdVO> getModelIdByOrderCode(String orderId, String code, String type, String templateId, String purchaserOrg, String purchaserOrgGuid) {
        if (StringUtils.isNotEmpty(templateId)) {
            List<SimpleModel> simpleModel = simpleModelMapper.selectList(new MPJLambdaWrapper<SimpleModel>()
                    .select(SimpleModel::getId, SimpleModel::getName, SimpleModel::getCode, SimpleModel::getCategoryId)
                    .eq(SimpleModel::getApproveStatus, CommonFlowableReqVOResultStatusEnums.SUCCESS.getResultCode())
                    .eq(SimpleModel::getOrgId, purchaserOrgGuid)
                    .eq(SimpleModel::getTemplateId, templateId)
                    .orderByDesc(SimpleModel::getApproveTime));
            if (ObjectUtil.isNotEmpty(simpleModel)) {
                return ModelCategoryConverter.INSTANCE.modelToModelVO(simpleModel);
            } else {
                String orgName = StringUtils.isBlank(purchaserOrg) ? "采购单位" : purchaserOrg;
                throw exception(ErrorCodeConstants.MODEL_NOT_FIND_EMPTY, orgName);
            }

        }
        //根据订单类型返回相应的模板id
//        DraftOrderInfoDO draftOrderInfoDO = gpMallOrderMapper.selectOne(new LambdaQueryWrapperX<DraftOrderInfoDO>().select(DraftOrderInfoDO::getProjectCategoryCode).eq(DraftOrderInfoDO::getId, orderId));
        DraftOrderInfoDO order = orderId == null ? null : gpMallOrderMapper.selectById(orderId);

//        if (ObjectUtil.isEmpty(order)) {
//            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "订单" + orderId + "查询为空");
//        }
        PackageInfoDO packageInfoDO = null;
        if (ObjectUtil.isEmpty(order)) {

            packageInfoDO = orderId == null ? null : packageInfoMapper.selectOne(new LambdaQueryWrapperX<PackageInfoDO>().eq(PackageInfoDO::getPackageGuid, orderId).orderByDesc(PackageInfoDO::getCreateTime).last(" limit 1"));
        }
        String clientId = ObjectUtil.isEmpty(order) ? (ObjectUtil.isEmpty(packageInfoDO) ? "ALL" : PlatformTemplateCategoryEnums.DZJYF.getClientId()) : PlatformTemplateCategoryEnums.getClientIdByPlatform(order.getContractFrom());
        String orderType = StringUtils.isEmpty(type) ? (ObjectUtil.isEmpty(order) ? ObjectUtil.isEmpty(packageInfoDO) ? null : packageInfoDO.getProjectType() : order.getProjectCategoryCode()) : type;
        //TODO 由于平台推送数据确认时品目数据，暂时默认托底数据为 A
        if(StringUtils.isEmpty(orderType)){
            orderType = "A";
        }

        if (StringUtils.isEmpty(orderType)) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "订单所属分类为空");
        }
        List<ModelIdVO> result = new ArrayList<>();
        //查询模板顺序配置开关  模板顺序0通用类模板在前  1平台通用类在前
//        String templateOrder = systemConfigApi.getConfigByKey("model_sort_switch");
        //通过品目查询是否能查询到唯一的
        if (StringUtils.isNotEmpty(code)) {
            List<ModelCatalogDo> modelCatalogDos = modelCatalogMapper.selectList(new LambdaQueryWrapperX<ModelCatalogDo>()
                    .eq(ModelCatalogDo::getCatalogCode, code)
                    .in(ModelCatalogDo::getPlatform, Arrays.asList(clientId, "All"))
                    .eqIfPresent(ModelCatalogDo::getPurCatalogType, PurCatalogTypeEnums.getKeyByCode(orderType))
                    .eq(ModelCatalogDo::getOrgId, SecurityFrameworkUtils.getLoginUser().getOrgId())
                    .orderByDesc(ModelCatalogDo::getCreateTime)
            );
            if (ObjectUtil.isNotEmpty(modelCatalogDos)) {
                return ModelCategoryConverter.INSTANCE.toModelVO(modelCatalogDos);
            }
//            if (ObjectUtil.isNotNull(modelCatalogDos) && modelCatalogDos.size() == 1) {
//                ModelIdVO catalogModel = new ModelIdVO().setModelName(modelCatalogDos.get(0).getModelName()).setModelId(modelCatalogDos.get(0).getModelId());
//                result.add(catalogModel);
//                return result;
//            }
            //根据平台码
//            if (ObjectUtil.isNotNull(modelCatalogDos) && modelCatalogDos.size() > 1) {
//                //根据配置开关返回集合
//                if (templateOrder.equals("1")) {
//                    modelCatalogDos.sort(Comparator.comparingInt(item -> item.getPlatform().equals("ALL") ? 1 : 0));
//                } else {
//                    modelCatalogDos.sort(Comparator.comparingInt(item -> item.getPlatform().equals("ALL") ? 0 : 1));
//                }
//                return ModelCategoryConverter.INSTANCE.toModelVO(modelCatalogDos);
//            }
        }
        //确认模板分类
        //所属分类A:货物  B：工程 C：服务
        //获取所属机构
        //返回通用类模板
        String instanceByPlatform = PlatformTemplateCategoryEnums.getInstanceByPlatform(clientId, orderType);
//        String all = PlatformTemplateCategoryEnums.THW.getCode();
        String all = PlatformTemplateCategoryEnums.getInstanceByPlatform("ALL", orderType);
        ArrayList<String> CategoryCode = new ArrayList<>();
        //电子卖场  只有货物类
        if (StringUtils.isEmpty(purchaserOrgGuid) && ObjectUtil.isNotEmpty(order)) {
            purchaserOrgGuid = order.getPurchaserOrgGuid();
        } else if (StringUtils.isEmpty(purchaserOrgGuid) && ObjectUtil.isEmpty(order)) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "订单信息不存在");
        }
        CategoryCode.add(instanceByPlatform);
        CategoryCode.add(all);
        MPJLambdaWrapper<SimpleModel> wrapper = new MPJLambdaWrapper<SimpleModel>()
                .select(SimpleModel::getId, SimpleModel::getCode, SimpleModel::getName, SimpleModel::getCategoryId)
                .eq(SimpleModel::getApproveStatus, CommonFlowableReqVOResultStatusEnums.SUCCESS.getResultCode())
                .eq(SimpleModel::getOrgId, purchaserOrgGuid)
                .orderByDesc(SimpleModel::getCategoryId, SimpleModel::getApproveTime);
        wrapper.in(ModelCategory::getCode, CategoryCode)
                .selectAs(ClientModelCategory::getClientId, SimpleModel::getDescription)
                .leftJoin(ModelCategory.class, ModelCategory::getId, SimpleModel::getCategoryId)
                .leftJoin(ClientModelCategory.class, ClientModelCategory::getCategoryId, ModelCategory::getParentId);
        /**
         if (PlatformTemplateCategoryEnums.DZH.getClientId().equals(clientId)) {
         if (!orderType.equals(PlatformTemplateCategoryEnums.DZH.getKey())) {
         throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "订单所属分类错误,请检查分类！");
         }
         if (instanceByPlatform != null) {
         CategoryCode.add(instanceByPlatform);
         }
         CategoryCode.add(all);

         wrapper.in(ModelCategory::getCode, CategoryCode)
         .selectAs(ClientModelCategory::getClientId, SimpleModel::getDescription)
         .leftJoin(ModelCategory.class, ModelCategory::getId, SimpleModel::getCategoryId)
         .leftJoin(ClientModelCategory.class, ClientModelCategory::getCategoryId, ModelCategory::getParentId);
         }
         //电子交易
         if (PlatformTemplateCategoryEnums.DZJYH.getClientId().equals(clientId)) {
         wrapper.selectAs(ClientModelCategory::getClientId, SimpleModel::getDescription)
         .leftJoin(ModelCategory.class, ModelCategory::getId, SimpleModel::getCategoryId)
         .leftJoin(ClientModelCategory.class, ClientModelCategory::getCategoryId, ModelCategory::getParentId);
         if (orderType.equals(PlatformTemplateCategoryEnums.DZJYH.getKey())) {
         if (instanceByPlatform != null) {
         CategoryCode.add(instanceByPlatform);
         }
         CategoryCode.add(all);
         wrapper.in(ModelCategory::getCode, CategoryCode);
         } else {
         wrapper.eq(ModelCategory::getCode, instanceByPlatform);
         }
         }

         //框采  协议定点
         if (PlatformTemplateCategoryEnums.KJF.getClientId().equals(clientId) || PlatformTemplateCategoryEnums.XYF.getClientId().equals(clientId)) {
         //根据范本
         //            if (StringUtils.isEmpty(templateId)) {
         //                throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "参考文本id为空");
         //            }
         //            wrapper.eq(SimpleModel::getTemplateId, templateId);
         wrapper.selectAs(ClientModelCategory::getClientId, SimpleModel::getDescription)
         .leftJoin(ModelCategory.class, ModelCategory::getId, SimpleModel::getCategoryId)
         .leftJoin(ClientModelCategory.class, ClientModelCategory::getCategoryId, ModelCategory::getParentId);
         if (orderType.equals(PlatformTemplateCategoryEnums.XYH.getKey())) {
         if (instanceByPlatform != null) {
         CategoryCode.add(instanceByPlatform);
         }
         CategoryCode.add(all);
         wrapper.in(ModelCategory::getCode, CategoryCode);
         } else {
         wrapper.eq(ModelCategory::getCode, instanceByPlatform);
         }
         }
         //服务工程超市
         if (PlatformTemplateCategoryEnums.FWF.getClientId().equals(clientId)) {
         if (orderType.equals(PlatformTemplateCategoryEnums.FWH.getKey())) {
         throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "订单所属分类错误,请检查分类！");
         }
         wrapper.eq(ModelCategory::getCode, instanceByPlatform)
         .selectAs(ClientModelCategory::getClientId, SimpleModel::getDescription)
         .leftJoin(ModelCategory.class, ModelCategory::getId, SimpleModel::getCategoryId)
         .leftJoin(ClientModelCategory.class, ClientModelCategory::getCategoryId, ModelCategory::getParentId);
         }
         **/
        //通过code查询
        List<SimpleModel> simpleModel = simpleModelMapper.selectList(wrapper);
        if (ObjectUtil.isNotEmpty(simpleModel)) {
            return ModelCategoryConverter.INSTANCE.modelToModelVO(simpleModel);
        } else {
            //TODO 暂时让晓萌的起草电子合同能匹配到自己新建的通用模板
            String modelName = PlatformTemplateCategoryEnums.getInfoByPlatform("TEMP",orderType);
             simpleModel = simpleModelMapper.selectList(new LambdaQueryWrapperX<SimpleModel>().likeIfPresent(SimpleModel::getName,modelName).orderByDesc(SimpleModel::getApproveTime).orderByDesc(SimpleModel::getUpdateTime));
            if(ObjectUtil.isNotNull(simpleModel)){
                return ModelCategoryConverter.INSTANCE.modelToModelVO(simpleModel);
            }
            String orgName = StringUtils.isEmpty(purchaserOrg) ? ObjectUtil.isEmpty(order) ? "采购单位" : order.getPurchaserOrg() : purchaserOrg;
            throw exception(ErrorCodeConstants.MODEL_NOT_FIND_EMPTY, orgName);
        }
//        if (ObjectUtil.isNotEmpty(simpleModel)) {
//            if (templateOrder.equals("1")) {
//                simpleModel.sort(Comparator.comparingInt(item -> item.getDescription().equals("ALL") ? 1 : 0));
//            } else {
//                simpleModel.sort(Comparator.comparingInt(item -> item.getDescription().equals("ALL") ? 0 : 1));
//            }
//            return ModelCategoryConverter.INSTANCE.modelToModelVO(simpleModel);
//        }
//        return result;
        //查询所有模板分类并联查所属分类
//        List<SimpleModel> simpleModels = simpleModelMapper.selectList(new MPJLambdaWrapper<SimpleModel>()
//                .in(SimpleModel::getId, modelCatalogDos.stream().map(ModelCatalogDo::getModelId).collect(Collectors.toList())));
        //多条数据开始过滤
        //根据合同类型过滤  人事   工程类  服务类  货物类
//        List<SimpleModel> collect = simpleModels.stream().filter(l -> projectCategoryCode.equals(l.getContractType())).collect(Collectors.toList());
//        if (collect.size() == 1) {
//            return collect.get(0).getId();
//        } else {
//            return collect.get(0).getId();
//        }
////        modelCatalogDos
//
//
////        String tradingTypeName = order.getTradingTypeName();
//        //去除交易方式括号中内容
////        tradingTypeName = tradingTypeName.replaceAll("\\(.*?\\)", "");


        // 临时写死
//        ModelIdVO hw213512345346 = new ModelIdVO().setModelCode("hw213512345346").setModelId("173c14c2a50c364daef881f0345026ab").setModelName("政府采购货物类通用合同模板7.3");
//        List<ModelIdVO> modelIdVOS = new ArrayList<>();
//        modelIdVOS.add(hw213512345346);
//        return modelIdVOS;
    }
}


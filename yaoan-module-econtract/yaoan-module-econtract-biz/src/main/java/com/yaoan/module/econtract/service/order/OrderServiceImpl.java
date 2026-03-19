package com.yaoan.module.econtract.service.order;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.api.contract.dto.ContractDataDTO;
import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfo;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.order.vo.*;
import com.yaoan.module.econtract.convert.gcy.gpmall.GPMallOrderConverter;
import com.yaoan.module.econtract.dal.dataobject.category.backups.ContractInfoBackupsDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.*;
import com.yaoan.module.econtract.dal.mysql.category.backups.ContractInfoBackupsMapper;
import com.yaoan.module.econtract.dal.mysql.order.*;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.order.OrderSourceEnums;
import com.yaoan.module.econtract.enums.order.ProjectCategoryEnums;
import com.yaoan.module.econtract.service.contract.ContractService;
import com.yaoan.module.econtract.util.EcontractUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/15 15:07
 */
@Service
public class OrderServiceImpl implements OrderService {

    static final String BACK_ORDER_ID = "1";
    static final String BACK_PURCHASER_ORG = "深圳市工业和信息化局";
    static final String BACK_SUPPLIER = "金信科技有限公司";
    static final String BACK_DEAL_METHOD = "二次竞价";
    static final String BACK_GOODS_CLASS_NAME = "电子产品";
    @Resource
    private GPMallOrderAccessoryMapper gpMallOrderAccessoryMapper;
    @Resource
    private GPMallFileAttachmentMapper gpMallFileAttachmentMapper;
    @Resource
    private GPMallEngineeringProjectMapper gpMallEngineeringProjectMapper;
    @Resource
    private GPMallGoodsMapper gpMallGoodsMapper;
    @Resource
    private GPMallProjectMapper gpMallProjectMapper;
    @Resource
    private GPMallOrderOldMapper gpMallOrderMapper;
    @Resource
    private ContractService contractService;
    @Resource
    private ContractInfoBackupsMapper contractInfoBackupsMapper;
    /**
     * 接收订单接口
     */
    @Override
    public void receiveOrderInfos(List<DraftOrderInfo> draftOrderInfoList) {
        checkOrderGuid(draftOrderInfoList);
        //数据预处理
        List<DraftOrderInfoDO> finalDraftOrderInfoDOList = new ArrayList<DraftOrderInfoDO>();
        List<OrderAccessoryDO> finalOrderAccessoryDOList = new ArrayList<OrderAccessoryDO>();
        List<FileAttachmentDO> finalFileAttachmentDOList = new ArrayList<FileAttachmentDO>();
        List<GoodsDO> finalGoodsDOList = new ArrayList<GoodsDO>();
        List<EngineeringProjectDO> finalEngineeringProjectDOList = new ArrayList<EngineeringProjectDO>();
        List<ProjectDO> finalProjectDOList = new ArrayList<ProjectDO>();

        for (DraftOrderInfo info : draftOrderInfoList) {

            DraftOrderInfoDO draftOrderInfoDO = GPMallOrderConverter.INSTANCE.convertDTO2DO(info);
            //自动生成订单code
            draftOrderInfoDO.setOrderCode(EcontractUtil.getCodeAutoByTimestamp(draftOrderInfoDO.getSupplierName()));
//            draftOrderInfoDO.setPurchaserOrgGuid(DEFAULT_BUYER);
//            draftOrderInfoDO.setSupplierGuid(DEFAULT_SUPPLIER);
            gpMallOrderMapper.insert(draftOrderInfoDO);
            finalDraftOrderInfoDOList.add(draftOrderInfoDO);
            String orderGuId = draftOrderInfoDO.getOrderGuid();
            //配件信息
            if (CollectionUtil.isNotEmpty(info.getAllAccessoryList())) {
                List<OrderAccessoryDO> orderAccessoryDOList = GPMallOrderConverter.INSTANCE.accessoryVOList2DOList(info.getAllAccessoryList());
                List<OrderAccessoryDO> updatedList = orderAccessoryDOList.stream().map(order -> {
                    order.setOrderId(orderGuId);
                    return order;
                }).collect(Collectors.toList());
                finalOrderAccessoryDOList.addAll(updatedList);
            }
            //附件信息
            if (CollectionUtil.isNotEmpty(info.getFileAttachmentVOList())) {
                List<FileAttachmentDO> fileAttachmentDOList = GPMallOrderConverter.INSTANCE.fileAttachmentVOList2DOList(info.getFileAttachmentVOList());
                List<FileAttachmentDO> updatedList = fileAttachmentDOList.stream().map(order -> {
                    order.setOrderId(orderGuId);
                    return order;
                }).collect(Collectors.toList());
                finalFileAttachmentDOList.addAll(updatedList);
            }
            //货物信息
            if (CollectionUtil.isNotEmpty(info.getGoodsVOList())) {
                List<GoodsDO> goodsDOList = GPMallOrderConverter.INSTANCE.goodsVOList2DOList(info.getGoodsVOList());
                List<GoodsDO> updatedList = goodsDOList.stream().map(order -> {
                    order.setOrderId(orderGuId);
                    return order;
                }).collect(Collectors.toList());
                finalGoodsDOList.addAll(updatedList);
            }
            //工程信息
            if (ObjectUtil.isNotNull(info.getEngineeringProjectVO())) {
                EngineeringProjectDO engineeringProjectDO = GPMallOrderConverter.INSTANCE.engineeringProjectVO2DO(info.getEngineeringProjectVO());
                engineeringProjectDO.setOrderId(orderGuId);
                finalEngineeringProjectDOList.add(engineeringProjectDO);
            }
            //项目信息
            if (ObjectUtil.isNotNull(info.getProjectVO())) {
                ProjectDO projectDO = GPMallOrderConverter.INSTANCE.projectVO2DO(info.getProjectVO());
                projectDO.setOrderId(orderGuId);
                finalProjectDOList.add(projectDO);
            }

        }

        //批量持久化
        gpMallOrderAccessoryMapper.insertBatch(finalOrderAccessoryDOList);
        gpMallFileAttachmentMapper.insertBatch(finalFileAttachmentDOList);
        gpMallGoodsMapper.insertBatch(finalGoodsDOList);
        gpMallProjectMapper.insertBatch(finalProjectDOList);
        gpMallEngineeringProjectMapper.insertBatch(finalEngineeringProjectDOList);

    }

    /**
     * 校验数据库里是否有存在订单id
     */
    void checkOrderGuid(List<DraftOrderInfo> draftOrderInfoList) {
        List<String> orderGuidList = draftOrderInfoList.stream().map(DraftOrderInfo::getOrderGuid).collect(Collectors.toList());
        Long count = gpMallOrderMapper.selectCount(new LambdaQueryWrapperX<DraftOrderInfoDO>().in(DraftOrderInfoDO::getOrderGuid, orderGuidList));
        if (count > 0) {
            throw exception(ErrorCodeConstants.EXIST_ORDER_GUID);
        }
    }

    @Override
    public PageResult<GPMallPageRespVO> listGPMallOrder(GPMallPageReqVO vo) {

        PageResult<DraftOrderInfoDO> draftOrderInfoDOPageResult = gpMallOrderMapper.selectPage(vo);
        PageResult<GPMallPageRespVO> respVOPageResult = GPMallOrderConverter.INSTANCE.convertOrderPageDO2RespPage(draftOrderInfoDOPageResult);
        enhancePage(respVOPageResult);
        return respVOPageResult;
    }

    @Override
    public OrderBaseInfoRespVO getOrderBaseInfo(IdReqVO vo) {
        String contractCode = "";
        String contractName = "";
        OrderBaseInfoRespVO result = new OrderBaseInfoRespVO();
        DraftOrderInfoDO orderInfoDO = gpMallOrderMapper.selectById(vo.getId());
        if (ObjectUtil.isNotNull(orderInfoDO)) {
            OrderBaseInfoRespVO respVO = GPMallOrderConverter.INSTANCE.toBaseInfoRespVO(orderInfoDO);
            contractCode = EcontractUtil.getCodeAutoByTimestamp(orderInfoDO.getSupplierName());
            contractName = EcontractUtil.getNameAuto(orderInfoDO.getPurchaserOrg());
            result.setContractCode(contractCode).setContractName(contractName).setOrderId(vo.getId()).setOrderTotalAmount(orderInfoDO.getOrderTotalAmount());
        }

        return result;
    }

    private void enhancePage(PageResult<GPMallPageRespVO> respVOPageResult) {
        List<GPMallPageRespVO> respVOList = respVOPageResult.getList();

        if (CollectionUtil.isNotEmpty(respVOList)) {
            for (GPMallPageRespVO respVO : respVOList) {
                List<ProjectDO> projectDOList = gpMallProjectMapper.selectList(new LambdaQueryWrapperX<ProjectDO>().eq(ProjectDO::getOrderId, respVO.getOrderGuid()));
                ProjectDO projectDO = new ProjectDO();
                if (CollectionUtil.isNotEmpty(projectDOList)) {
                    projectDO = projectDOList.get(0);
                }
                ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(projectDO.getProjectCategoryCode());
                if (ObjectUtil.isNotNull(projectCategoryEnums)) {
                    respVO.setOrderTypeStr(projectCategoryEnums.getInfo());
                }
                //订单状态
                OrderSourceEnums orderSourceEnums = OrderSourceEnums.getInstance(Integer.valueOf(respVO.getOrderStatus()));
                if (ObjectUtil.isNotNull(orderSourceEnums)) {
                    respVO.setOrderStatusStr(orderSourceEnums.getValue());
                }
                //商品信息
                List<GoodsDO> goodsDOList = gpMallGoodsMapper.selectList(new LambdaQueryWrapperX<GoodsDO>().eq(GoodsDO::getOrderId, respVO.getOrderGuid()));
                List<GoodsRespVO> goodsRespVO = GPMallOrderConverter.INSTANCE.conertGoodsListDO2Resp(goodsDOList);
                respVO.setGoodsRespVOList(goodsRespVO);

            }
        }
    }

    /**
     * 根据订单id，自动生成数据
     */
    @Override
    public OrderAutoInfoRespVO getAutoInfo(IdReqVO vo) {
        String orderId = vo.getId();
        DraftOrderInfoDO orderInfoDO = gpMallOrderMapper.selectById(orderId);
        OrderAutoInfoRespVO respVO = new OrderAutoInfoRespVO();
        if (ObjectUtil.isNotNull(orderInfoDO)) {
            respVO = GPMallOrderConverter.INSTANCE.convert2Auto(orderInfoDO);
            List<GoodsDO> goodsDOList = gpMallGoodsMapper.selectList(new LambdaQueryWrapperX<GoodsDO>().eq(GoodsDO::getOrderId, orderInfoDO.getOrderGuid()));
            String firstGoodClassName = BACK_GOODS_CLASS_NAME;
            if (CollectionUtil.isNotEmpty(goodsDOList)) {
                firstGoodClassName = goodsDOList.get(0).getGoodsClassName();
            }
            String buyer = StringUtils.isNotEmpty(orderInfoDO.getPurchaserOrg()) ? orderInfoDO.getPurchaserOrg() : BACK_PURCHASER_ORG;
            String dealMethod = StringUtils.isNotEmpty(orderInfoDO.getPurchaserOrg()) ? orderInfoDO.getPurchaserOrg() : BACK_DEAL_METHOD;

            String contractName = EcontractUtil.getContractNameAuto(buyer, firstGoodClassName, dealMethod);
            String contractCode = EcontractUtil.getCodeAutoByTimestamp(buyer);
            respVO.setContractCode(contractCode).setContractName(contractName);
            respVO.setBuyPlanCode("无");
            enhanceOrderAuto(respVO, orderInfoDO, goodsDOList);
        }
        return respVO;
    }
    @Override
    public ContractDataDTO getOrderInfoAndTemplateInfo(String id) {
        ContractInfoBackupsDO contractInfoBackupsDO = contractInfoBackupsMapper.selectOne(new LambdaQueryWrapperX<ContractInfoBackupsDO>()
                .eq(ContractInfoBackupsDO::getOrderId, id).last(" limit 1"));
        if(ObjectUtil.isEmpty(contractInfoBackupsDO)){
            return null;
        }
        String contractInfo = contractInfoBackupsDO.getContractInfo();
        ContractDataDTO dataDTO = JSONObject.parseObject(contractInfo, ContractDataDTO.class);
        String code = EcontractUtil.getRandomCodeAutoByTimestamp();
        dataDTO.setCode("SXKJXY-"+code);
        return dataDTO;
    }



    private void enhanceOrderAuto(OrderAutoInfoRespVO respVO, DraftOrderInfoDO orderInfoDO, List<GoodsDO> goodsDOList) {
        if (CollectionUtil.isEmpty(goodsDOList)) {
            //如果没有商品，就使用托底数据
            goodsDOList = gpMallGoodsMapper.selectList(new LambdaQueryWrapperX<GoodsDO>().eq(GoodsDO::getOrderId, BACK_ORDER_ID));
            //订单的金额总额 = 所有商品总金额之和，商品总金额=商品单价x商品数量
            BigDecimal totalAmount = getSumAmountOfGoods(goodsDOList);
            respVO.setOrderTotalAmount(totalAmount);
        }
        List<GoodsRespVO> goodsRespVOList = GPMallOrderConverter.INSTANCE.conertGoodsListDO2Resp(goodsDOList);
        respVO.setGoodsRespVOList(goodsRespVOList);
        //托底填充数据
        respVO.setBuyerSeal(StringUtils.isNotEmpty(orderInfoDO.getPurchaserOrg()) ? orderInfoDO.getPurchaserOrg() : BACK_PURCHASER_ORG);
        respVO.setSupplierSeal(StringUtils.isNotEmpty(orderInfoDO.getSupplierName()) ? orderInfoDO.getPurchaserOrg() : BACK_SUPPLIER);
        respVO.setOrderId(orderInfoDO.getId());
    }

    /**
     * 获得所有相关商品的金额
     */
    private BigDecimal getSumAmountOfGoods(List<GoodsDO> goodsDOList) {
        BigDecimal sum = BigDecimal.ZERO;
        for (GoodsDO goodsDO : goodsDOList) {
            BigDecimal totalAmountOfThisGood = goodsDO.getGoodsOnePrice().multiply(new BigDecimal(goodsDO.getQty()));
            sum = sum.add(totalAmountOfThisGood);
        }
        return sum;
    }


    /**
     * 第三方数据列表（黑龙江迁移过来的）
     * 按照公司做数据隔离
     */
    @Override
    public PageResult<GPMallPageRespVO> listThirdData(GPMallPageReqVO vo) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        PageResult<DraftOrderInfoDO> draftOrderInfoDOPageResult = gpMallOrderMapper.listThirdData(vo);
        PageResult<GPMallPageRespVO> respVOPageResult = GPMallOrderConverter.INSTANCE.convertOrderPageDO2RespPage(draftOrderInfoDOPageResult);
        enhancePage(respVOPageResult);
        return respVOPageResult;
    }

    /** 第三方数据起草合同（黑龙江项目迁移来的） */

    /**
     * 黑龙江迁移来的
     */
    @Override
    public PageResult<GPMallPageRespVO> listGPMallOrder2(GPMallPageReqVO vo) {
        // 根据公司来展示列表
        Long companyId = SecurityFrameworkUtils.getLoginUser().getCompanyId();
        PageResult<DraftOrderInfoDO> draftOrderInfoDOPageResult = gpMallOrderMapper.selectPage2(vo, companyId);
        PageResult<GPMallPageRespVO> respVOPageResult = GPMallOrderConverter.INSTANCE.convertOrderPageDO2RespPage(draftOrderInfoDOPageResult);
        enhancePage(respVOPageResult);
        return respVOPageResult;
    }

    @Override
    public ThirdOrderAutoInfoRespVO autoCreateContractByOrderId(IdReqVO vo) {
        String orderId = vo.getId();
        DraftOrderInfoDO orderInfoDO = gpMallOrderMapper.selectById(orderId);
        ThirdOrderAutoInfoRespVO respVO = new ThirdOrderAutoInfoRespVO();
        if (ObjectUtil.isNotNull(orderInfoDO)) {
            respVO = GPMallOrderConverter.INSTANCE.convert2ThirdAuto(orderInfoDO);
            List<GoodsDO> goodsDOList = gpMallGoodsMapper.selectList(new LambdaQueryWrapperX<GoodsDO>().eq(GoodsDO::getOrderId, orderInfoDO.getOrderGuid()));
            String firstGoodClassName = BACK_GOODS_CLASS_NAME;
            if (CollectionUtil.isNotEmpty(goodsDOList)) {
                firstGoodClassName = goodsDOList.get(0).getGoodsClassName();
            }
            String buyer = StringUtils.isNotEmpty(orderInfoDO.getPurchaserOrg()) ? orderInfoDO.getPurchaserOrg() : BACK_PURCHASER_ORG;
            String dealMethod = StringUtils.isNotEmpty(orderInfoDO.getPurchaserOrg()) ? orderInfoDO.getPurchaserOrg() : BACK_DEAL_METHOD;

            String contractName = EcontractUtil.getContractNameAuto(buyer, firstGoodClassName, dealMethod);
            String contractCode = EcontractUtil.getCodeAutoByTimestamp(buyer);
            respVO.setContractCode(contractCode).setContractName(contractName);
            respVO.setBuyPlanCode("无");
            enhanceOrderAuto(respVO, orderInfoDO, goodsDOList);
        }
        return respVO;
    }

    @Override
    public PageResult<GPMallPageV2RespVO> listGPMallOrderV2(GPMallPageReqVO vo) {


        PageResult<DraftOrderInfoDO> draftOrderInfoDOPageResult = gpMallOrderMapper.selectPage(vo);
        PageResult<GPMallPageV2RespVO> respVOPageResult = GPMallOrderConverter.INSTANCE.convertOrderPageDO2RespPageV2(draftOrderInfoDOPageResult);
        enhancePageV2(respVOPageResult);
        return respVOPageResult;
    }

    @Override
    public OrderAutoInfoV2RespVO getAutoInfoV2(IdReqVO vo) {
        String orderId = vo.getId();
        DraftOrderInfoDO orderInfoDO = gpMallOrderMapper.selectById(orderId);
        OrderAutoInfoV2RespVO respVO = new OrderAutoInfoV2RespVO();
        if (ObjectUtil.isNotNull(orderInfoDO)) {
            respVO = GPMallOrderConverter.INSTANCE.convert2AutoV2(orderInfoDO);
            List<GoodsDO> goodsDOList = gpMallGoodsMapper.selectList(new LambdaQueryWrapperX<GoodsDO>().eq(GoodsDO::getOrderId, orderInfoDO.getOrderGuid()));
            String firstGoodClassName = BACK_GOODS_CLASS_NAME;
            if (CollectionUtil.isNotEmpty(goodsDOList)) {
                firstGoodClassName = goodsDOList.get(0).getGoodsClassName();
            }
            String buyer = StringUtils.isNotEmpty(orderInfoDO.getPurchaserOrg()) ? orderInfoDO.getPurchaserOrg() : BACK_PURCHASER_ORG;
            String dealMethod = StringUtils.isNotEmpty(orderInfoDO.getPurchaserOrg()) ? orderInfoDO.getPurchaserOrg() : BACK_DEAL_METHOD;

            String contractName = EcontractUtil.getContractNameAuto(buyer, firstGoodClassName, dealMethod);
            String contractCode = EcontractUtil.getCodeAutoByTimestamp(buyer);
            respVO.setContractCode(contractCode).setContractName(contractName);
            respVO.setBuyPlanCode("无");
            enhanceOrderAuto(respVO, orderInfoDO, goodsDOList);
        }
        return respVO;
    }

    private void enhancePageV2(PageResult<GPMallPageV2RespVO> respVOPageResult) {
        List<GPMallPageV2RespVO> respVOList = respVOPageResult.getList();

        if (CollectionUtil.isNotEmpty(respVOList)) {
            for (GPMallPageRespVO respVO : respVOList) {
                List<ProjectDO> projectDOList = gpMallProjectMapper.selectList(new LambdaQueryWrapperX<ProjectDO>().eq(ProjectDO::getOrderId, respVO.getOrderGuid()));
                ProjectDO projectDO = new ProjectDO();
                if (CollectionUtil.isNotEmpty(projectDOList)) {
                    projectDO = projectDOList.get(0);
                }
                ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(projectDO.getProjectCategoryCode());
                if (ObjectUtil.isNotNull(projectCategoryEnums)) {
                    respVO.setOrderTypeStr(projectCategoryEnums.getInfo());
                }
                //订单状态
                OrderSourceEnums orderSourceEnums = OrderSourceEnums.getInstance(Integer.valueOf(respVO.getOrderStatus()));
                if (ObjectUtil.isNotNull(orderSourceEnums)) {
                    respVO.setOrderStatusStr(orderSourceEnums.getValue());
                }
                //商品信息
                List<GoodsDO> goodsDOList = gpMallGoodsMapper.selectList(new LambdaQueryWrapperX<GoodsDO>().eq(GoodsDO::getOrderId, respVO.getOrderGuid()));
                List<GoodsRespVO> goodsRespVO = GPMallOrderConverter.INSTANCE.conertGoodsListDO2Resp(goodsDOList);
                respVO.setGoodsRespVOList(goodsRespVO);

            }
        }
    }

}

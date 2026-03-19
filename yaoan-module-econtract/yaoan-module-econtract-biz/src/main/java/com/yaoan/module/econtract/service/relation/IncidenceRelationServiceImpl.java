package com.yaoan.module.econtract.service.relation;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractListRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractRepVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.SignatoryRespVO;
import com.yaoan.module.econtract.controller.admin.relation.vo.IncidenceRelationCreatReqVO;
import com.yaoan.module.econtract.controller.admin.relation.vo.IncidenceRelationResplistVO;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.convert.relation.IncidenceRelationConverter;
import com.yaoan.module.econtract.dal.dataobject.agreement.PrefAgreementRelDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import com.yaoan.module.econtract.dal.dataobject.relation.IncidenceRelation;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.terminate.TerminateContractDO;
import com.yaoan.module.econtract.dal.mysql.agreement.PrefAgreementRelMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.dal.mysql.performance.perforTask.PerforTaskMapper;
import com.yaoan.module.econtract.dal.mysql.relation.IncidenceRelationMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.dal.mysql.terminate.TerminateContractMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.api.dept.dto.UserCompanyInfoRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 关联关系服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Service
public class IncidenceRelationServiceImpl implements IncidenceRelationService {
    @Resource
    private IncidenceRelationMapper relationMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractPerforMapper contractPerforMapper;
    @Resource
    private PerforTaskMapper perforTaskMapper;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private RelativeMapper relativeMapper;

    @Resource
    private CompanyApi companyApi;

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private FileApi fileApi;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private PrefAgreementRelMapper prefAgreementRelMapper;
    @Resource
    private TerminateContractMapper terminateContractMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createIncidenceRelation(IncidenceRelationCreatReqVO incidenceRelationCreatReqVO) {
        checkIncidenceRelation(incidenceRelationCreatReqVO.getContractId(), incidenceRelationCreatReqVO.getIncidenceRelation());
        IncidenceRelation entity = IncidenceRelationConverter.INSTANCE.toEntity(incidenceRelationCreatReqVO);
        //1.合同状态为合同终止不可变更
        if (ContractStatusEnums.TERMINATED.getCode().equals(incidenceRelationCreatReqVO.getContractStatus()
        ) && IncidenceRelationEnums.CONTRACT_CHANGE_AGREEMENT.getCode().equals(incidenceRelationCreatReqVO.getIncidenceRelation())) {
            throw exception(ErrorCodeConstants.CONTRACT_CHANGE_ERROR);
        }
        //2.合同状态为签署完成时将合同状态变为合同变更，原合同履约状态改为履约结束，履约任务为履约结束，
        if (ContractStatusEnums.SIGN_COMPLETED.getCode().equals(incidenceRelationCreatReqVO.getContractStatus()
        ) && IncidenceRelationEnums.CONTRACT_CHANGE_AGREEMENT.getCode().equals(incidenceRelationCreatReqVO.getIncidenceRelation())) {
            //2.1.修改合同状态
            contractMapper.updateById(new ContractDO().setId(incidenceRelationCreatReqVO.getContractId()).setStatus(ContractStatusEnums.CONTRACT_CHANGE.getCode()));
            //2.2.修改履约状态
            ContractPerformanceDO contractPerformanceDO = contractPerforMapper.selectOne(new LambdaQueryWrapperX<ContractPerformanceDO>()
                    .eq(ContractPerformanceDO::getContractId, incidenceRelationCreatReqVO.getContractId()));
            String id = contractPerformanceDO == null ? null : contractPerformanceDO.getId();
            contractPerforMapper.updateById(new ContractPerformanceDO().setId(id).setContractId(incidenceRelationCreatReqVO.getContractId())
                    .setContractStatus(ContractPerfEnums.PERFORMANCE_END.getCode()));
            //2.3.修改履约任务状态
            //2.3.1 查询履约任务状态为履约完成，待履约之外的履约任务
            List<Integer> excludeStatuses = Arrays.asList(PerfTaskEnums.PERFORMANCE_FINISH.getCode(), PerfTaskEnums.WAIT_PERFORMANCE.getCode());
            PerfTaskDO perfTaskDO = perforTaskMapper.selectOne(new LambdaQueryWrapperX<PerfTaskDO>().eq(PerfTaskDO::getContractPerfId, id)
                    .notIn(PerfTaskDO::getTaskStatus, excludeStatuses));

            //2.3.2 修改履约任务状态为履约结束
            if (BeanUtil.isNotEmpty(perfTaskDO)) {
                perforTaskMapper.updateById(perfTaskDO.setTaskStatus(PerfTaskEnums.PERFORMANCE_END.getCode()));
            }
        }
        //3.此合同为框架协议，不可设置框架协议
        if (IncidenceRelationEnums.OUTLINE_AGREEMENT.getCode().equals(incidenceRelationCreatReqVO.getIncidenceRelation())) {
            Long count = relationMapper.selectCount(new LambdaQueryWrapperX<IncidenceRelation>()
                    .eqIfPresent(IncidenceRelation::getRelationContractId, entity.getContractId())
                    .eqIfPresent(IncidenceRelation::getIncidenceRelation, IncidenceRelationEnums.OUTLINE_AGREEMENT.getCode()));
            if (count > 0) {
                throw exception(ErrorCodeConstants.OUTLINE_AGREEMENT_ERROR);
            }

        }
        //4.变更合同、续签合同不可为其他合同的框架协议
        if (IncidenceRelationEnums.OUTLINE_AGREEMENT.getCode().equals(incidenceRelationCreatReqVO.getIncidenceRelation())) {
            List<IncidenceRelation> incidenceRelations = getIncidenceRelationCreatReqVO(incidenceRelationCreatReqVO.getIncidenceRelation(), incidenceRelationCreatReqVO.getRelationContractId());
            if (CollectionUtil.isNotEmpty(incidenceRelations)) {
                throw exception(ErrorCodeConstants.CONTRACT_RENEWAL_CHANGE_ERROR);
            }
        }
        //5.变更合同、续签合同在关联合同时，如主合同框架协议已关联框架协议，则不可重复关联，--变更合同、续签合同只有在主合同没有框架协议时才可关联框架协议
        if (IncidenceRelationEnums.OUTLINE_AGREEMENT.getCode().equals(incidenceRelationCreatReqVO.getIncidenceRelation())) {
            //判断此合同是否为变更协议，续签协议
            List<IncidenceRelation> incidenceRelationCreatReqVO1 = getIncidenceRelationCreatReqVO(incidenceRelationCreatReqVO
                    .getIncidenceRelation(), incidenceRelationCreatReqVO.getContractId());
            if (CollectionUtil.isNotEmpty(incidenceRelationCreatReqVO1)) {
                List<String> contractIds = incidenceRelationCreatReqVO1.stream().map(IncidenceRelation::getContractId).collect(Collectors.toList());
                List<Map<String, Object>> maps = relationMapper.selectMaps(contractIds);
                Map<Object, Map<String, Object>> modelMap = CollectionUtils.convertMap(maps, (map) -> map.get("contraId"));
                //为变更协议或续签协议
                for (IncidenceRelation incidenceRelation : incidenceRelationCreatReqVO1) {
                    Long count = (Long) (modelMap.get(incidenceRelation.getContractId()) == null ? null : modelMap.get(incidenceRelation.getContractId()).get("count"));
                    if (count > 0) {
                        throw exception(ErrorCodeConstants.MASTER_CONTRACT_EXIST_OUTLINE_AGREEMENT);
                    }
                }
            }
        }
        relationMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void deleteIncidenceRelation(String id) {

        //1.根据关联关系id获取关联关系信息
        IncidenceRelation incidenceRelation = relationMapper.selectById(id);
        //2.校验此关联关系是否可取消，变更协议不可取消
        if (BeanUtil.isNotEmpty(incidenceRelation)) {
            if (IncidenceRelationEnums.CONTRACT_CHANGE_AGREEMENT.getCode().equals(incidenceRelation.getIncidenceRelation())) {
                throw exception(ErrorCodeConstants.CANCEL_TYPE_ERROR);
            }
            //3.删除此关联关系
            relationMapper.deleteById(id);
        }
    }


    @Override
    public void checkIncidenceRelation(String id, Integer incidenceRelation) {
        //1.查看该合同是否有其他框架协议，有不可关联，框架协议只能有一个
        //所有关联关系类型均只可单选，每类关联关系均只可关联一个合同，如已关联，再次点击时，提示：已有{关联关系名称}，不可重复关联。
        Long count = relationMapper.selectCount(id, incidenceRelation);
        if (count > 0) {
            IncidenceRelationEnums instance = IncidenceRelationEnums.getInstance(incidenceRelation);
            Assert.notNull(instance, "参数不正确");
            switch (Objects.requireNonNull(instance)) {
                case OUTLINE_AGREEMENT:
                    throw exception(ErrorCodeConstants.OUTLINE_AGREEMENT_REPETITION);
                case CONTRACT_CHANGE_AGREEMENT:
                    throw exception(ErrorCodeConstants.CONTRACT_CHANGE_AGREEMENT_REPETITION);
                case CONTRACT_RENEWAL_AGREEMENT:
                    throw exception(ErrorCodeConstants.CONTRACT_RENEWAL_AGREEMENT_REPETITION);
                case OTHER_AGREEMENT:
                    throw exception(ErrorCodeConstants.OTHER_AGREEMENT_REPETITION);
                default:
            }
        }
    }

    @Override
    public List<IncidenceRelationResplistVO> queryAllIncidenceRelation(ContractRepVO contractRepVO) throws Exception {
        //当该合同为正常签署的一份合同，非框架协议、非其他合同的变更协议及续签合同时有合同续签协议，框架协议，补充协议，合同终止（或合同变更），其他协议
        //1.根据合同id查看该合同 的所有关联合同信息
        List<IncidenceRelation> incidenceRelations = relationMapper.selectList(new LambdaQueryWrapperX<IncidenceRelation>().eq(IncidenceRelation::getContractId, contractRepVO.getId()));
        List<IncidenceRelationResplistVO> incidenceRelationResplistVOS = IncidenceRelationConverter.INSTANCE.tolistVo(incidenceRelations);
        List<String> contractIds = incidenceRelations.stream().map(IncidenceRelation::getRelationContractId).collect(Collectors.toList());
        //查询合同信息
        Map<String, ContractDO> contractMap = getContractMap(contractIds);
        //校验该合同是否为其他合同的框架协议、变更协议及续签协议
        Long count = relationMapper.selectCount2(contractRepVO.getId());
        incidenceRelationResplistVOS.forEach(item -> item.setContractName(contractMap.get(item.getRelationContractId()) == null ? null : contractMap.get(item.getRelationContractId()).getName()));
        incidenceRelationResplistVOS.forEach(item -> item.setContractCode(contractMap.get(item.getRelationContractId()) == null ? null : contractMap.get(item.getRelationContractId()).getCode()));
        if (count > 0) {
            //查看为其他合同的框架协议，变更协议，合同续签时的合同信息及关联关系
            List<IncidenceRelation> incidenceRelations1 = relationMapper.selectList(new LambdaQueryWrapperX<IncidenceRelation>().eq(IncidenceRelation::getRelationContractId, contractRepVO.getId()));
            List<IncidenceRelationResplistVO> incidenceRelationResplistVOS1 = IncidenceRelationConverter.INSTANCE.tolistVo(incidenceRelations1);
            List<String> contractIds1 = incidenceRelations1.stream().map(IncidenceRelation::getContractId).collect(Collectors.toList());
            Map<String, ContractDO> contractMap1 = getContractMap(contractIds1);
            for (IncidenceRelationResplistVO incidenceRelationvo : incidenceRelationResplistVOS1) {
                incidenceRelationvo.setContractName(contractMap1.get(incidenceRelationvo.getContractId()) == null ? null : contractMap1.get(incidenceRelationvo.getContractId()).getName());
                incidenceRelationvo.setContractCode(contractMap1.get(incidenceRelationvo.getContractId()) == null ? null : contractMap1.get(incidenceRelationvo.getContractId()).getCode());
                //为其他合同的框架协议,关系为子合同
                if (IncidenceRelationEnums.OUTLINE_AGREEMENT.getCode().equals(incidenceRelationvo.getIncidenceRelation())) {
                    incidenceRelationvo.setIncidenceRelation(IncidenceRelationEnums.SON_CONTRACT.getCode());
                }
                //为其他合同的续签合同或者变更合同
                if (IncidenceRelationEnums.CONTRACT_RENEWAL_AGREEMENT.getCode().equals(incidenceRelationvo.getIncidenceRelation()) || IncidenceRelationEnums.CONTRACT_CHANGE_AGREEMENT.getCode().equals(incidenceRelationvo.getIncidenceRelation())) {
                    incidenceRelationvo.setIncidenceRelation(IncidenceRelationEnums.PARENT_CONTRACT.getCode());
                }
                incidenceRelationResplistVOS.add(incidenceRelationvo);
            }
        }
        //查看此合同是否有履约
        ContractPerformanceDO contractPerformanceDO = contractPerforMapper.selectContractPerformance(contractRepVO.getId());
        if (BeanUtil.isNotEmpty(contractPerformanceDO)) {
            //有履约查看持履约是否有补充文件
            List<PrefAgreementRelDO> prefAgreementRelDOS = prefAgreementRelMapper.selectList(new LambdaQueryWrapperX<PrefAgreementRelDO>().eqIfPresent(PrefAgreementRelDO::getPrefId, contractPerformanceDO.getId()));
            if (CollectionUtil.isNotEmpty(prefAgreementRelDOS)) {
                //有合同补充协议，添加合同补充协议
                for (PrefAgreementRelDO prefAgreementRelDO : prefAgreementRelDOS) {
                    IncidenceRelationResplistVO incidenceRelationResplistVO = new IncidenceRelationResplistVO();
                    incidenceRelationResplistVO.setIncidenceRelation(IncidenceRelationEnums.CONTRACT_REPLENISH_AGREEMENT.getCode());
                    incidenceRelationResplistVO.setContractName("-");
                    String url = fileApi.getURL(prefAgreementRelDO.getInfraFileId());
                    incidenceRelationResplistVO.setFileId(prefAgreementRelDO.getInfraFileId());
                    incidenceRelationResplistVO.setFileName(prefAgreementRelDO.getFileName());
                    incidenceRelationResplistVO.setFileurl(url);
                    incidenceRelationResplistVOS.add(incidenceRelationResplistVO);
                }
            }
        }
        //新增合同终止协议，返回文件名和文件路径，文件id
        ContractDO contractDO = contractMapper.selectOne(new LambdaQueryWrapperX<ContractDO>().eqIfPresent(ContractDO::getId, contractRepVO.getId())
                .select(ContractDO::getId,ContractDO::getStatus));
        if (BeanUtil.isNotEmpty(contractDO) && ContractStatusEnums.TERMINATED.getCode().equals(contractDO.getStatus())) {
            TerminateContractDO terminateContractDO = terminateContractMapper.selectOne(new LambdaQueryWrapperX<TerminateContractDO>().eqIfPresent(TerminateContractDO::getContractId, contractRepVO.getId()));
            if (BeanUtil.isNotEmpty(terminateContractDO)) {
                IncidenceRelationResplistVO incidenceRelationResplistVO = new IncidenceRelationResplistVO();
                //文件ID
                incidenceRelationResplistVO.setIncidenceRelation(IncidenceRelationEnums.CONTRACT_STOP_AGREEMENT.getCode());
                incidenceRelationResplistVO.setContractName("-");
                String url = fileApi.getURL(terminateContractDO.getFileAddId());
                incidenceRelationResplistVO.setFileId(terminateContractDO.getFileAddId());
                incidenceRelationResplistVO.setFileName(terminateContractDO.getFileName());
                incidenceRelationResplistVO.setFileurl(url);
                incidenceRelationResplistVOS.add(incidenceRelationResplistVO);
            }
        }
        //根据搜索字符串模糊匹配\
        List<IncidenceRelationResplistVO> result = new ArrayList<>();
        if (StringUtils.isNotBlank(contractRepVO.getSearchText())) {
            for (IncidenceRelationResplistVO incidenceRelationResplistVO : incidenceRelationResplistVOS) {
                if (incidenceRelationResplistVO.getContractName().contains(contractRepVO.getSearchText()) || incidenceRelationResplistVO.getContractCode().contains(contractRepVO.getSearchText())) {
                    result.add(incidenceRelationResplistVO);
                }
            }
        } else {
            result = incidenceRelationResplistVOS;
        }
        return result;
    }

    @Override
    public List<ContractListRespVO> queryAllContractInfo(ContractRepVO contractRepVO) {
        //1.查看此合同的所有关联合同
        ArrayList<String> ids = new ArrayList<>();
        LambdaQueryWrapperX<IncidenceRelation> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.and((queryWrapper) -> {
            queryWrapper.eq(IncidenceRelation::getRelationContractId, contractRepVO.getId())
                    .or().eq(IncidenceRelation::getContractId, contractRepVO.getId());
        });
        List<IncidenceRelation> incidenceRelations = relationMapper.selectList(wrapperX);
        //加入自己的合同id
        ids.add(contractRepVO.getId());
        for (IncidenceRelation incidenceRelation : incidenceRelations) {
            if (incidenceRelation.getContractId().equals(contractRepVO.getId())) {
                // 为此合同的关联合同，加入关联合同id
                ids.add(incidenceRelation.getRelationContractId());
            } else {
                //此合同为其他合同的关联合同，加入其它合同id
                ids.add(incidenceRelation.getContractId());
            }
        }
        //2.如果有搜索字符串查询满足条件的数据，没有查看所有合同列表
        List<ContractDO> contractDOResult1 = contractMapper.selectList(contractRepVO);
        //3.删除与此合同有关联关系的合同，包括自己
        List<ContractDO> contractDOResult = contractDOResult1.stream().filter(contractDO -> !ids.contains(contractDO.getId()))
                .collect(Collectors.toList());
        //4.DO转换为vo
        List<ContractListRespVO> result = ContractConverter.INSTANCE.convertPage3(contractDOResult);
        //5.查询所有合同类型
        List<String> contractTypeIds = result.stream().map(ContractListRespVO::getContractType).collect(Collectors.toList());
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().inIfPresent(ContractType::getId, contractTypeIds));
        Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypes, ContractType::getId);

        // 6.获取签署方信息-企业，单位
            //6.1 获取合同ids
        List<String> contractIds = result.stream().map(ContractListRespVO::getId).collect(Collectors.toList());
           //6.2 获取签署方ids
        List<String> signatoryIds = signatoryRelMapper.selectList(new LambdaQueryWrapperX<SignatoryRelDO>().inIfPresent(SignatoryRelDO::getContractId, contractIds)).stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
           //6.3 获取相对方信息
        List<Relative> relativeList=new ArrayList<>();
        if(CollectionUtil.isNotEmpty(signatoryIds)){
             relativeList = relativeMapper.selectBatchIds(signatoryIds);
        }

        // 7.获取签署方信息-个人
        List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().inIfPresent(ContractDO::getId, contractIds).select(ContractDO::getId, ContractDO::getCreator));
        Map<String, ContractDO> contractMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
        List<String> creatorIdsStr = contractDOS.stream().map(ContractDO::getCreator).collect(Collectors.toList());
        List<Long> creatorIds = creatorIdsStr.stream().map(Long::valueOf).collect(Collectors.toList());
            //7.1 根据创建人ids获取公司信息
        List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(creatorIds);
        Map<Long, UserCompanyInfoRespDTO> userCompanyMap = CollectionUtils.convertMap(userCompanyInfoList, UserCompanyInfoRespDTO::getUserId);

          //7.2 发起方是个体，通过id 获取用户 nickname
        List<AdminUserRespDTO> users = adminUserApi.getUserList(userCompanyInfoList.stream().map(UserCompanyInfoRespDTO::getUserId).collect(Collectors.toList()));
        Map<Long, AdminUserRespDTO> userListByDeptMap = CollectionUtils.convertMap(users, AdminUserRespDTO::getId);

        for (ContractListRespVO contractRespVO : result) {
            //6.设置合同类型名称
            contractRespVO.setContractTypeName(contractTypeMap.get(contractRespVO.getContractType()) == null ? null : contractTypeMap.get(contractRespVO.getContractType()).getName());
            //7.设置签署方
            List<SignatoryRespVO> signatoryRespVOList = new ArrayList<>();
            if (contractRespVO.getUpload() == 0) {
                //7.1 通过草拟创建合同
                List<String> signatoryNameList = signatoryNameById(relativeList, userCompanyMap, contractRespVO.getId(), contractMap, userListByDeptMap);
                for (String s : signatoryNameList) {
                    signatoryRespVOList.add(new SignatoryRespVO().setSignatory(s));
                }
                contractRespVO.setSignatoryList(signatoryRespVOList);
            } else {
                //7.2 TODO 通过上传合同创建

            }
        }
        return result;
    }

    private Map<String, ContractDO> getContractMap(List<String> contractIds) {
        //查询合同信息
        List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().inIfPresent(ContractDO::getId, contractIds)
                .select(ContractDO::getId, ContractDO::getCode, ContractDO::getName));
        return CollectionUtils.convertMap(contractDOS, ContractDO::getId);
    }

    private List<IncidenceRelation> getIncidenceRelationCreatReqVO(Integer incidenceRelation, String id) {
        List<IncidenceRelation> incidenceRelations = new ArrayList<>();
        if (IncidenceRelationEnums.OUTLINE_AGREEMENT.getCode().equals(incidenceRelation)) {
            incidenceRelations = relationMapper.selectIncidenceRelationList(id);
//            incidenceRelations = relationMapper.selectList(new LambdaQueryWrapperX<IncidenceRelation>()
//                    .eq(IncidenceRelation::getRelationContractId, id)
//                    .eq(IncidenceRelation::getIncidenceRelation, IncidenceRelationEnums.CONTRACT_CHANGE_AGREEMENT.getCode())
//                    .or()
//                    .eq(IncidenceRelation::getIncidenceRelation, IncidenceRelationEnums.CONTRACT_RENEWAL_AGREEMENT.getCode()));
        }
        return incidenceRelations;
    }

    //根据合同id 获取签署方名称
    private List<String> signatoryNameById(List<Relative> relativeList, Map<Long, UserCompanyInfoRespDTO> userCompanyMap, String id,
                                           Map<String, ContractDO> contractMap, Map<Long, AdminUserRespDTO> userListByDeptMap) {
        List<String> signatoryNameList = new ArrayList<>();

        if (StringUtils.isNotBlank(userCompanyMap == null ? null : (userCompanyMap.get(Long.valueOf(contractMap.get(id).getCreator()))).getName())) {
            signatoryNameList.add(userCompanyMap == null ? null : (userCompanyMap.get(Long.valueOf(contractMap.get(id).getCreator()))).getName());
        } else {
            //发起方是个体，通过id 获取nickname
            signatoryNameList.add(userListByDeptMap.get(contractMap.get(id).getCreator()).getNickname());
        }
        for (Relative relative : relativeList) {
            if (!relative.getCompanyName().isEmpty()) {
                signatoryNameList.add(relative.getCompanyName());
            } else {
                signatoryNameList.add(relative.getName());
            }
        }
        return signatoryNameList;
    }
}

package com.yaoan.module.econtract.service.contractarchives;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.SerializeUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.ArchiveBpmReqVO;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.*;
import com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow.ContractBorrowBpmDO;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.mysql.bpm.contractborrow.ContractBorrowBpmMapper;
import com.yaoan.module.econtract.dal.mysql.businessfile.BusinessFileMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contractarchives.ContractArchivesMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.service.bpm.archive.ArchiveBpmService;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.system.api.dept.DeptApi;
import com.yaoan.module.system.api.dept.dto.DeptRespDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyDeptRespDTO;
import com.yaoan.module.system.api.dict.DictDataApi;
import com.yaoan.module.system.api.dict.dto.DictDataRespDTO;
import com.yaoan.module.system.api.permission.RoleApi;
import com.yaoan.module.system.api.permission.dto.RoleRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yaoan.module.bpm.enums.ErrorCodeConstants.PROCESS_INSTANCE_START_USER_SELECT_ASSIGNEES_NOT_EXISTS;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.CONTRACT_ARCHIVES_EXISTS;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.CONTRACT_ARCHIVES_NOT_EXISTS;

/**
 * 合同档案 Service 实现类
 *
 * @author lls
 */
@Service
@Slf4j
public class ContractArchivesServiceImpl implements ContractArchivesService {

    //todo （备用）复杂情况，启用编程式事务
    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private ContractArchivesMapper contractArchivesMapper;

    @Resource
    private ArchiveBpmService archiveBpmService;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private BusinessFileMapper businessFileMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private DictDataApi dictDataApi;
    @Resource
    private FileApi fileApi;
    @Resource
    private ContractBorrowBpmMapper borrowBpmMapper;
    @Resource
    private DeptApi deptApi;
    @Resource
    private RoleApi roleApi;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createContractArchives(ContractArchivesSaveReqVO createReqVO) {
        log.info("ContractArchivesService-归档申请时创建合同档案,入参为{}", JSONObject.toJSONString(createReqVO));

        //--------档号查重-------
        // 最新设计不要档号 2025-03-12 lls
//        ContractArchivesDO code = contractArchivesMapper.selectOne(new QueryWrapper<ContractArchivesDO>().eq("code", getString(createReqVO)));
//
//        if (code != null) {
//            throw exception(CONTRACT_ARCHIVES_EXISTS);
//        }

        // 1. 发起审批流程,获取processInstanceId
        Long loginUserId = getLoginUserId();
        AdminUserRespDTO adminUserRespDTO = userApi.getUser(loginUserId);
        ArchiveBpmReqVO archiveBpmReqVO = ArchiveBpmReqVO.builder()
                .reason(null)
                .type(0).build();
        String generateUUID = IdUtil.fastSimpleUUID();
        if (StringUtils.isEmpty(createReqVO.getId())){
            archiveBpmReqVO.setArchiveId(generateUUID);
        }else {
            archiveBpmReqVO.setArchiveId(createReqVO.getId());
        }
        String process = archiveBpmService.createProcess(loginUserId, archiveBpmReqVO);
        log.info("档号为{}的归档审批发起完成，审批号为{}", createReqVO.getCode(), process);

        //获取档案载体list 2 string
        List<Integer> medium = createReqVO.getMedium();
        String mediumString = medium.stream().map(String::valueOf).collect(Collectors.joining(","));

        // 2. 落库档案表
        ContractArchivesDO contractArchives = BeanUtils.toBean(createReqVO, ContractArchivesDO.class);
        //.setCode(getString(createReqVO))
        contractArchives.setMedium(mediumString)
                .setStatus(0).setCreatorName(adminUserRespDTO.getNickname()).setArchiveTime(DateUtil.toLocalDateTime(new Date()));
        //获取合同部门
        ContractDO contractDO1 = contractMapper.selectOne(ContractDO::getId, createReqVO.getContractId());
        if(contractDO1 != null){
            contractArchives.setDeptId(contractDO1.getDeptId());
        }
        if(StringUtils.isEmpty(contractArchives.getId())){
            contractArchives.setId(generateUUID);
            contractArchivesMapper.insert(contractArchives);
        } else {
            contractArchivesMapper.updateById(contractArchives);
        }

        // 3. 落库附件表
        List<BusinessFileDO> businessFileDOS = getBusinessFileDOS(createReqVO, contractArchives);
        businessFileMapper.insertBatch(businessFileDOS);
        //修改归档状态为归档中
        ContractDO contractDO = new ContractDO().setDocument(2).setId(createReqVO.getContractId());
        contractMapper.updateById(contractDO);
        return contractArchives.getId();
    }

    private String getString(ContractArchivesSaveReqVO contractArchivesRespVO) {
        String archiveStorageYear = null;
        if(contractArchivesRespVO.getArchiveStorageYear().equals("Y")){
            archiveStorageYear = "Y";
        }else{
            archiveStorageYear = "D"+ contractArchivesRespVO.getArchiveStorageYear();
        }
        return contractArchivesRespVO.getFondsNo()+"-" + contractArchivesRespVO.getFirstLevelNo() + "●"+contractArchivesRespVO.getSecondLevelNo()+"●"
                + archiveStorageYear+"●" + contractArchivesRespVO.getYear()+"-" + contractArchivesRespVO.getVolumeNo() +"-"+ contractArchivesRespVO.getCode();

    }

    private List<BusinessFileDO> getBusinessFileDOS(ContractArchivesSaveReqVO createReqVO, ContractArchivesDO contractArchives) {
        List<BusinessFileDO> businessFileDOS = new ArrayList<>();
        List<AttachmentVO> attachmentIds = createReqVO.getAttachmentIds();
        attachmentIds.forEach(item -> {
            BusinessFileDO businessFileDO = new BusinessFileDO();
            businessFileDO.setBusinessId(contractArchives.getId()).setFileId(item.getFileId()).setFileName(item.getName());
            businessFileDOS.add(businessFileDO);
        });
        List<AttachmentVO> contractFiles = createReqVO.getContractFiles();
        contractFiles.forEach(item -> {
            BusinessFileDO businessFileDO = new BusinessFileDO();
            businessFileDO.setBusinessId(contractArchives.getAttachmentId()).setFileId(item.getFileId()).setFileName(item.getName());
            businessFileDOS.add(businessFileDO);
        });
        return businessFileDOS;
    }

    @Override
    public void updateContractArchives(ContractArchivesSaveReqVO updateReqVO) {
        // 校验存在
        validateContractArchivesExists(updateReqVO.getId());
        // 更新
        ContractArchivesDO updateObj = BeanUtils.toBean(updateReqVO, ContractArchivesDO.class);
        contractArchivesMapper.updateById(updateObj);
    }

    @Override
    public void deleteContractArchives(String id) {
        // 校验存在
        validateContractArchivesExists(id);
        // 删除
        contractArchivesMapper.deleteById(id);
    }

    private void validateContractArchivesExists(String id) {
        if (contractArchivesMapper.selectById(id) == null) {
            throw exception(CONTRACT_ARCHIVES_NOT_EXISTS);
        }
    }

    @Override
    public ContractArchivesRespVO getContractArchives(ContractArchivesReqVO vo) throws Exception {
        // 判断如果id 不为空则按id查询
        if (ObjectUtil.isNotEmpty(vo.getId())){
            ContractArchivesDO contractArchivesDO = contractArchivesMapper.selectById(vo.getId());
            if(ObjectUtil.isEmpty(contractArchivesDO)){
                throw exception(CONTRACT_ARCHIVES_NOT_EXISTS);
            }
            ContractArchivesRespVO contractArchivesRespVO = BeanUtils.toBean(contractArchivesDO, ContractArchivesRespVO.class);
            Result result = getResult();
            contractArchivesRespVO.setArchiveStorageYearName(result.map1.get(contractArchivesRespVO.getArchiveStorageYear()).getLabel());
            contractArchivesRespVO.setOpenStatusName(result.map2.get(contractArchivesRespVO.getOpenStatus()).getLabel());
            if(contractArchivesRespVO.getMedium().contains(",")){
                Map<String, DictDataRespDTO> map3 = result.map3;
                String concatenatedValues = map3.values().stream()
                        .map(DictDataRespDTO::getLabel)  // 获取每个 DictDataRespDTO 对象的 value 字段
                        .collect(Collectors.joining(","));
                contractArchivesRespVO.setMediumName(concatenatedValues);
            }else{
                contractArchivesRespVO.setMediumName(result.map3.get(contractArchivesRespVO.getMedium()).getLabel());
            }

            //获取附件
            List<BusinessFileDO> businessFileDOS = businessFileMapper.selectByBusinessId(contractArchivesRespVO.getId());
            if (ObjectUtil.isNotEmpty(businessFileDOS)) {
                List<AttachmentVO> attachmentIds = convertToAttachmentVOList(businessFileDOS);
                contractArchivesRespVO.setAttachmentIds(attachmentIds);
            }

            List<BusinessFileDO> contractContentFileDOS = businessFileMapper.selectByBusinessId(contractArchivesRespVO.getAttachmentId());
            if (ObjectUtil.isNotEmpty(contractContentFileDOS)) {
                List<AttachmentVO> contractContentIds = convertToAttachmentVOList(contractContentFileDOS);
                contractArchivesRespVO.setContractContentIds(contractContentIds);
            }
            //获取档案借阅记录
            DataPermissionUtils.executeIgnore(()->{
                List<Long> deptIdList = getDeptIdList();
                List<ContractBorrowBpmDO> contractBorrowBpmDOS = borrowBpmMapper.selectList(new LambdaQueryWrapperX<ContractBorrowBpmDO>()
                        .eqIfPresent(ContractBorrowBpmDO::getArchiveId, vo.getId()).inIfPresent(ContractBorrowBpmDO::getDeptId, deptIdList));
                if(ObjectUtil.isNotEmpty(contractBorrowBpmDOS)){
                    List<Long> deptIds = contractBorrowBpmDOS.stream().map(ContractBorrowBpmDO::getDeptId).collect(Collectors.toList());
                    List<DeptRespDTO> deptList = deptApi.getDeptList(deptIds);
                    Map<Long, DeptRespDTO> deptRespDTOMap = CollectionUtils.convertMap(deptList, DeptRespDTO::getId);
                    List<AttachmentBorrowVO> borrowRecords = new ArrayList<>();
                    getBorrow(contractBorrowBpmDOS, deptRespDTOMap, borrowRecords);
                    contractArchivesRespVO.setBorrowRecords(borrowRecords);
                }
            });

            DeptRespDTO dept = deptApi.getDept(contractArchivesRespVO.getDeptId());
            if(ObjectUtil.isNotEmpty(dept)){
                contractArchivesRespVO.setDeptName(dept.getName());
            }
            ContractDO contractDO = contractMapper.selectById(contractArchivesDO.getContractId());
            if (ObjectUtil.isNotEmpty(contractDO)){
                contractArchivesRespVO.setContractName(contractDO.getName());
            }
            return contractArchivesRespVO;
        }

        if(ObjectUtil.isNotEmpty(vo.getContractId())){
            ContractArchivesRespVO contractArchives = new ContractArchivesRespVO();
            //获取当前用户单位
            AdminUserRespDTO adminUserRespDTO = userApi.getUser(getLoginUserId());
            //获取全宗号
            if(ObjectUtil.isNotEmpty(adminUserRespDTO.getCompanyId())){
                contractArchives.setFondsNo(String.valueOf(adminUserRespDTO.getCompanyId()));
            }else{
                contractArchives.setFondsNo("MR");
            }
            //获取一级类别号
            contractArchives.setFirstLevelNo("HT");
            //获取二级类别号
            if(ObjectUtil.isNotEmpty(adminUserRespDTO.getDeptId())){
                contractArchives.setSecondLevelNo(String.valueOf(adminUserRespDTO.getDeptId()));
            }else{
                contractArchives.setSecondLevelNo("MR");
            }
            //获取案卷号
            ContractDO contractDO = contractMapper.selectById(vo.getContractId());
            if(ObjectUtil.isEmpty(contractDO)){
                throw exception(ErrorCodeConstants.CONTRACT_NAME_NOT_EXISTS);
            }
            ContractType contractType = contractTypeMapper.selectById(contractDO.getContractType());
            if(ObjectUtil.isNotEmpty(contractType)){
                contractArchives.setVolumeNo(contractType.getCode().substring(contractType.getCode().length() - 2));
            }else{
                contractArchives.setVolumeNo("MR");
            }
            //获取归档年度
            contractArchives.setYear(DateUtil.format(new Date(), "yyyy"));
            //获取合同正文文件
            String name = fileApi.getName(contractDO.getPdfFileId());
            List<AttachmentVO> contractContentIds = new ArrayList<>();
            contractContentIds.add(new AttachmentVO().setFileId(contractDO.getPdfFileId()).setName(name));
            contractArchives.setContractContentIds(contractContentIds);

            DeptRespDTO dept = deptApi.getDept(contractArchives.getDeptId());
            if(ObjectUtil.isNotEmpty(dept)){
                contractArchives.setDeptName(dept.getName());
            }
            contractArchives.setContractName(contractDO.getName());
            return contractArchives;
        }
        return null;
    }

    private List<Long> getDeptIdList() {
        //获取当前用户
        UserCompanyDeptRespDTO userCompanyDeptRespDTO = userApi.selectUserCompanyDept(SecurityFrameworkUtils.getLoginUserId());
        //获取用户公司下的所有部门
        if(ObjectUtil.isEmpty(userCompanyDeptRespDTO.getCompanyInfo())){
            return null;
        }
        List<DeptRespDTO> deptRespDTOList = deptApi.getDeptListByCompanyId(userCompanyDeptRespDTO.getCompanyInfo().getId());
        List<Long> deptIdList = deptRespDTOList.stream().map(DeptRespDTO::getId).collect(Collectors.toList());
        return deptIdList;
    }

    private void getBorrow(List<ContractBorrowBpmDO> contractBorrowBpmDOS, Map<Long, DeptRespDTO> deptRespDTOMap, List<AttachmentBorrowVO> borrowRecords) {
        contractBorrowBpmDOS.forEach(item -> {
            AttachmentBorrowVO attachmentBorrowVO = new AttachmentBorrowVO();
            attachmentBorrowVO.setId(item.getId());
            attachmentBorrowVO.setCreator(item.getSubmitterName());
            attachmentBorrowVO.setDeptName(deptRespDTOMap.get(item.getDeptId())!= null ? deptRespDTOMap.get(item.getDeptId()).getName():null);
            attachmentBorrowVO.setSubmitTime(item.getSubmitTime());
            attachmentBorrowVO.setReturnTime(item.getActualReturnTime() != null ? item.getActualReturnTime() : null);
            // 获取当前时间
            Date date = new Date();
            Date returnTime = item.getReturnTime();
            Date submitTime = item.getSubmitTime();
            if (submitTime != null && submitTime.after(date)) {
                if(item.getBorrowType().contains("1")){
                    attachmentBorrowVO.setBorrowStatusName("借阅中");
                }else {
                    attachmentBorrowVO.setBorrowStatusName("待取档");
                }

            } else if (returnTime != null && returnTime.after(date)) {
                if(ObjectUtil.isNotEmpty(item.getIsReturn())){
                    if(item.getIsReturn()==0){
                        // 计算剩余时间
                        Instant returnInstant = returnTime.toInstant();
                        Instant submitInstant = date.toInstant();
                        Duration duration = Duration.between(submitInstant, returnInstant);
                        long remainTimeInSeconds = duration.getSeconds();
                        long days = remainTimeInSeconds / (24 * 3600);
                        long hours = (remainTimeInSeconds % (24 * 3600)) / 3600;
                        attachmentBorrowVO.setRemainTime(days + "天" + hours + "小时");
                        attachmentBorrowVO.setBorrowStatusName("借阅中");
                    } else if(item.getIsReturn()==1){
                        attachmentBorrowVO.setReturnTime(item.getActualReturnTime()!=null?item.getActualReturnTime():null);
                        attachmentBorrowVO.setBorrowStatusName("已归还");
                    }
                } else {
                    // 计算剩余时间
                    Instant returnInstant = returnTime.toInstant();
                    Instant submitInstant = date.toInstant();
                    Duration duration = Duration.between(submitInstant, returnInstant);
                    long remainTimeInSeconds = duration.getSeconds();
                    long days = remainTimeInSeconds / (24 * 3600);
                    long hours = (remainTimeInSeconds % (24 * 3600)) / 3600;
                    attachmentBorrowVO.setRemainTime(days + "天" + hours + "小时");
                    attachmentBorrowVO.setBorrowStatusName("借阅中");
                }

            } else {
                if(ObjectUtil.isNotEmpty(item.getIsReturn())){
                    if(item.getIsReturn()==1){
                        attachmentBorrowVO.setReturnTime(item.getActualReturnTime()!=null?item.getActualReturnTime():null);
                        attachmentBorrowVO.setBorrowStatusName("已归还");
                    }else if(item.getBorrowType().contains("1") && item.getIsReturn()==0){
                        attachmentBorrowVO.setBorrowStatusName("借阅中");
                    }
                }
                if(item.getBorrowType().equals("0")){
                    attachmentBorrowVO.setReturnTime(item.getReturnTime()!=null?item.getReturnTime():null);
                    attachmentBorrowVO.setBorrowStatusName("已归还");
                }
            }
            borrowRecords.add(attachmentBorrowVO);
        });
    }


    private List<AttachmentVO> convertToAttachmentVOList(List<BusinessFileDO> businessFileDOS) {
        List<AttachmentVO> attachmentList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(businessFileDOS)) {
            businessFileDOS.forEach(item -> {
                attachmentList.add(new AttachmentVO()
                        .setFileId(item.getFileId())
                        .setName(item.getFileName())
                        .setIsAdd(0));
            });
        }
        return attachmentList;
    }

    @Override
    @DataPermission(enable = false)
    public PageResult<ContractArchivesRespVO> getContractArchivesPage(ContractArchivesPageReqVO pageReqVO) {
        PageResult<ContractArchivesRespVO> contractArchivesRespVO = new PageResult<>();
        //获取当前用户
        UserCompanyDeptRespDTO userCompanyDeptRespDTO = userApi.selectUserCompanyDept(SecurityFrameworkUtils.getLoginUserId());
        //获取用户公司下的所有部门
        if(ObjectUtil.isEmpty(userCompanyDeptRespDTO.getCompanyInfo())){
            return null;
        }
        List<DeptRespDTO> deptRespDTOList = deptApi.getDeptListByCompanyId(userCompanyDeptRespDTO.getCompanyInfo().getId());
        List<Long> deptIds = deptRespDTOList.stream().map(DeptRespDTO::getId).collect(Collectors.toList());
        Result result = getResult();
        //档案检索
        if(pageReqVO.getFlag()==0){
//            //判断角色
//            RoleRespDTO role = roleApi.getRole(SecurityFrameworkUtils.getLoginUserId());
//            if (role!=null) {
//                if(role.getName().equals("经办人")){
//                    pageReqVO.setCreator(String.valueOf(SecurityFrameworkUtils.getLoginUserId()));
//                }
//            }
            PageResult<ContractArchivesDO> contractArchivesDOPageResult = contractArchivesMapper.selectContractArchivesPage(pageReqVO,deptIds);
            if(ObjectUtil.isEmpty(contractArchivesDOPageResult)){
                return null;
            }
            contractArchivesRespVO  = BeanUtils.toBean(contractArchivesDOPageResult, ContractArchivesRespVO.class);
            if(ObjectUtil.isNotEmpty(contractArchivesRespVO)){
                contractArchivesRespVO.getList().forEach(item -> {
                    item.setArchiveStorageYearName(result.map1.get(item.getArchiveStorageYear()).getLabel());
                    item.setOpenStatusName(result.map2.get(item.getOpenStatus()).getLabel());
                    if(item.getDeptId().equals(userCompanyDeptRespDTO.getDeptId())){
                        item.setIsDept(1);
                    }else{
                        item.setIsDept(0);
                    }
                });
            }
            return contractArchivesRespVO;
        }else{
            //借阅申请，不包括本部门的档案
            List<Long> deptIdList = deptRespDTOList.stream()
                    .map(DeptRespDTO::getId)
                    .filter(id -> !id.equals(userCompanyDeptRespDTO.getDeptId()))
                    .collect(Collectors.toList());
            PageResult<ContractArchivesDO> contractArchivesDOPageResult = contractArchivesMapper.selectContractArchivesPage(pageReqVO,deptIdList);
            if(ObjectUtil.isEmpty(contractArchivesDOPageResult)){
                return null;
            }
            contractArchivesRespVO  = BeanUtils.toBean(contractArchivesDOPageResult, ContractArchivesRespVO.class);
            if(ObjectUtil.isNotEmpty(contractArchivesRespVO)){
                contractArchivesRespVO.getList().forEach(item -> {
                    item.setArchiveStorageYearName(result.map1.get(item.getArchiveStorageYear()).getLabel());
                    item.setOpenStatusName(result.map2.get(item.getOpenStatus()).getLabel());
                });
            }
        }

        return contractArchivesRespVO;
    }

    private Result getResult() {
        List<DictDataRespDTO> archiveStorageYearList = dictDataApi.getDictDataList("storage_life");
        Map<String, DictDataRespDTO> map1 = CollectionUtils.convertMap(archiveStorageYearList, DictDataRespDTO::getValue);
        List<DictDataRespDTO> openStatusNameList = dictDataApi.getDictDataList("opening_situation");
        Map<String, DictDataRespDTO> map2 = CollectionUtils.convertMap(openStatusNameList, DictDataRespDTO::getValue);
        List<DictDataRespDTO> archiveCarrierList = dictDataApi.getDictDataList("archive_carrier");
        Map<String, DictDataRespDTO> map3 = CollectionUtils.convertMap(archiveCarrierList, DictDataRespDTO::getValue);
        return new Result(map1, map2, map3);
    }

    private static class Result {
        public final Map<String, DictDataRespDTO> map1;
        public final Map<String, DictDataRespDTO> map2;
        public final Map<String, DictDataRespDTO> map3;

        public Result(Map<String, DictDataRespDTO> map1, Map<String, DictDataRespDTO> map2, Map<String, DictDataRespDTO> map3) {
            this.map1 = map1;
            this.map2 = map2;
            this.map3 = map3;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String supplementContractArchives(ContractArchivesSupplementReqVO supplementReqVO) {

        log.info("ContractArchivesService-补录申请时创建合同档案,入参为{}", JSONObject.toJSONString(supplementReqVO));
        //获取登录用户
        Long loginUserId = getLoginUserId();

        //1.校验档案是否存在？不存在则进行提示
        ContractArchivesDO contractArchivesDO = contractArchivesMapper.selectById(supplementReqVO.getId());
        if (contractArchivesDO == null) {
            throw exception(CONTRACT_ARCHIVES_NOT_EXISTS);
        }

        // 发起审批
        ArchiveBpmReqVO archiveBpmReqVO = ArchiveBpmReqVO.builder()
                .archiveId(supplementReqVO.getId())
                .reason(supplementReqVO.getSupplementRemark())
                .type(1).build();
        String process = archiveBpmService.createProcess(loginUserId, archiveBpmReqVO);
        log.info("发起审批完成，审批号为{}", process);
        //修改档案审批状态为待审批

        //2.更新档案表的数据
        ContractArchivesDO contractArchives = BeanUtils.toBean(supplementReqVO, ContractArchivesDO.class);
        //获取档案载体list 2 string
        List<Integer> medium = supplementReqVO.getMedium();
        String mediumString = medium.stream().map(String::valueOf).collect(Collectors.joining(","));
        contractArchives.setMedium(mediumString);
        String archiveStorageYear = null;
        if(supplementReqVO.getArchiveStorageYear().equals("Y")){
            archiveStorageYear = "Y";
        }else{
            archiveStorageYear = "D"+ supplementReqVO.getArchiveStorageYear();
        }
        String code =  supplementReqVO.getFondsNo()+"-" + supplementReqVO.getFirstLevelNo() + "●"+supplementReqVO.getSecondLevelNo()+"●"
                + archiveStorageYear+"●" + supplementReqVO.getYear()+"-" + supplementReqVO.getVolumeNo() +"-"+ supplementReqVO.getCode();
        contractArchives.setCode(code).setStatus(0);
        contractArchivesMapper.updateById(contractArchives);

        //3.更新附件表的数据(档案表:附件表=1：N)

        // 3. 落库附件表
        if (ObjectUtil.isNotEmpty(supplementReqVO.getAttachmentIds())) {
            processAttachments(supplementReqVO.getAttachmentIds(), contractArchivesDO.getId());
        }

        if (ObjectUtil.isNotEmpty(supplementReqVO.getContractFiles())) {
            processAttachments(supplementReqVO.getContractFiles(), supplementReqVO.getAttachmentId());
        }
        return contractArchivesDO.getId();
    }

    @Override
    public PageResult<ContractArchivesRespVO> archivePage(ContractArchivesPageReqVO pageReqVO) {
        PageResult<ContractArchivesDO> contractArchivesDOPageResult = contractArchivesMapper.selectContractArchivesPage(pageReqVO, null);
        if(ObjectUtil.isEmpty(contractArchivesDOPageResult)){
            return null;
        }
        PageResult<ContractArchivesRespVO> contractArchivesRespVO = BeanUtils.toBean(contractArchivesDOPageResult, ContractArchivesRespVO.class);
        Result result = getResult();
        if(ObjectUtil.isNotEmpty(contractArchivesRespVO)){
            contractArchivesRespVO.getList().forEach(item -> {
                item.setArchiveStorageYearName(result.map1.get(item.getArchiveStorageYear()).getLabel());
                item.setOpenStatusName(result.map2.get(item.getOpenStatus()).getLabel());
            });
        }
        return contractArchivesRespVO;
    }

    private void processAttachments(List<AttachmentVO> attachments, String businessId) {
        List<BusinessFileDO> businessFileDOS = attachments.stream()
                .map(item -> {
                    BusinessFileDO businessFileDO = new BusinessFileDO();
                    businessFileDO.setBusinessId(businessId)
                            .setFileId(item.getFileId())
                            .setFileName(item.getName());
                    return businessFileDO;
                })
                .collect(Collectors.toList());

        businessFileMapper.insertBatch(businessFileDOS);
    }
    /**
     * 附件表落库
     *
     * @param attachmentIds   附件id
     * @param attachmentNames 附件名称
     * @param businessId      业务id
     * @return 1 成功  0失败
     */
    private int insert2BusinessFile(List<Long> attachmentIds, List<String> attachmentNames, String businessId) {
        log.info("ContractArchivesService-附件表落库,入参为{},{},{}", attachmentIds, attachmentNames, businessId);
        if (attachmentIds == null || attachmentNames == null) {
            return 0;
        }
        for (int i = 0; i < attachmentIds.size(); i++) {
            BusinessFileDO businessFileDO = BusinessFileDO.builder()
                    //附件id
                    .fileId(attachmentIds.get(i))
                    //附件名称
                    .fileName(attachmentNames.get(i))
                    //业务id
                    .businessId(businessId).build();
            businessFileMapper.insert(businessFileDO);
        }
        return 1;
    }
}
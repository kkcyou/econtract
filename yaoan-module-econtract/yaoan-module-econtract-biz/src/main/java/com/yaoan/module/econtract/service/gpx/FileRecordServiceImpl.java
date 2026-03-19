package com.yaoan.module.econtract.service.gpx;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.econtract.controller.admin.gpx.vo.file.PermissionRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.file.SaveFileAndCompanyReqVO;
import com.yaoan.module.econtract.convert.filerecord.FileRecordConvert;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.filerecord.FileRecordDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.filerecord.FileRecordMapper;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import com.yaoan.module.system.api.config.SystemConfigApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.EMPTY_DATA_ERROR_V2;
import static com.yaoan.module.system.enums.config.SystemConfigKeyEnums.IF_PASS_COUNT;

/**
 * @description:
 * @author: Pele
 * @date: 2024/7/4 16:26
 */
@Service
public class FileRecordServiceImpl implements FileRecordService {
    @Resource
    private FileRecordMapper fileRecordMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private ContractOrderExtMapper contractOrderExtMapper;
    @Resource
    private ContractMapper contractMapper;

    /**
     * 保存文件签章记录
     */
    @Override
    @DataPermission(enable = false)
    public String saveFileAndCompanyInfo(SaveFileAndCompanyReqVO contractPageReqVO) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        AtomicReference<ContractOrderExtDO> atomicOrderContractDO = new AtomicReference<>();
        TenantUtils.executeIgnore(()->{
            atomicOrderContractDO.set(contractOrderExtMapper.selectOne(new LambdaQueryWrapperX<ContractOrderExtDO>()
                    .eqIfPresent(ContractOrderExtDO::getId, contractPageReqVO.getContractId())
                    .select(ContractOrderExtDO::getPdfFileId)
            ));
        });
        ContractOrderExtDO contractDO = atomicOrderContractDO.get();
        FileRecordDO fileRecordDO = FileRecordConvert.INSTANCE.r2d(contractPageReqVO);
        if (ObjectUtil.isNotEmpty(contractDO)) {
            if (StringUtils.isNotBlank(loginUser.getOrgId())) {
                fileRecordDO.setOrgId(loginUser.getOrgId());
            }
            if (ObjectUtil.isNotNull(contractDO.getPdfFileId())) {
                fileRecordDO.setFileId(contractDO.getPdfFileId());
            }
            if (StringUtils.isNotBlank(loginUser.getSupplyId())) {
                fileRecordDO.setSupplierId(loginUser.getSupplyId());
            }
            fileRecordMapper.insert(fileRecordDO);
            return String.valueOf(fileRecordDO.getId());
        }
        TenantUtils.executeIgnore(()->{
            ContractDO contractDO1 = contractMapper.selectById(contractPageReqVO.getContractId());
            if(ObjectUtil.isNotEmpty(contractDO1)){
                if (ObjectUtil.isNotNull(contractDO1.getPdfFileId())) {
                    fileRecordDO.setFileId(contractDO1.getPdfFileId());
                }
                if (ObjectUtil.isNotEmpty(loginUser.getId())) {
                    fileRecordDO.setOrgId(String.valueOf(loginUser.getId()));
                }
                fileRecordMapper.insert(fileRecordDO);
               
            }
        });
        if(fileRecordDO.getId() !=null){
            return String.valueOf(fileRecordDO.getId());
        }
        throw exception(EMPTY_DATA_ERROR_V2, "合同");
    }

    /**
     * 删除文件签章记录(删除最近的一次签章记录)
     */
    @Override
    public String deleteFileAndCompanyInfo(String contractId) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        fileRecordMapper.delete(new LambdaQueryWrapperX<FileRecordDO>()
                .eqIfPresent(FileRecordDO::getSupplierId, loginUser.getSupplyId())
                .eqIfPresent(FileRecordDO::getOrgId, loginUser.getOrgId())
                .eq(FileRecordDO::getContractId, contractId)
                .orderByDesc(FileRecordDO::getCreateTime)
                .last("LIMIT  1")
        );
        return "success";
    }

    /**
     * 返回这个是当前人单位是否对合同有没撤销的签章+供应商id+采购人id
     */
    @Override
    @DataPermission(enable = false)
    public PermissionRespVO getPermission(String contractId) {
        PermissionRespVO result = new PermissionRespVO().setIfPermission(false);
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        AtomicReference<ContractDO> contractDO = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(()->{
            TenantUtils.executeIgnore(()->{
                 contractDO.set(contractMapper.selectById(contractId));
            });
        });
        final List<FileRecordDO>[] fileRecordDOList = new List[]{new ArrayList<>()};
        TenantUtils.executeIgnore(()->{
            if(ObjectUtil.isNotEmpty(contractDO)){
                if(ContractUploadTypeEnums.ORDER_DRAFT.getCode().equals(contractDO.get().getUpload())||ContractUploadTypeEnums.THIRD_PARTY.getCode().equals(contractDO.get().getUpload())){
                    fileRecordDOList[0] = fileRecordMapper.selectList(new LambdaQueryWrapperX<FileRecordDO>()
                            .eqIfPresent(FileRecordDO::getSupplierId, loginUser.getSupplyId())
                            .eqIfPresent(FileRecordDO::getOrgId, loginUser.getOrgId())
                            .eq(FileRecordDO::getContractId, contractId)
                    );
                }else{
                    LambdaQueryWrapperX<FileRecordDO> wrapper = new LambdaQueryWrapperX<FileRecordDO>()
                            .eq(FileRecordDO::getContractId, contractId);
                    if (ObjectUtil.isNotEmpty(loginUser.getId())) {
                        wrapper.eq(FileRecordDO::getOrgId, loginUser.getId());
                    }

                    fileRecordDOList[0] = fileRecordMapper.selectList(wrapper);
                }
                if (CollectionUtil.isNotEmpty(fileRecordDOList[0])) {
                    FileRecordDO fileRecordDO = fileRecordDOList[0].get(0);
                    result.setIfPermission(true);
                    if (StringUtils.isNotBlank(fileRecordDO.getOrgId())) {
                        result.setOrgId(fileRecordDO.getOrgId());
                    }
                }
            }

        });
        
        return result;
    }


    /**
     * 是否通过：签章公司数是否达到通过要求
     * 同一个公司多次签章，也只算一次
     */
    @Override
    public Boolean ifPass(String contractId) {
        String value = systemConfigApi.getConfigByKey(IF_PASS_COUNT.getKey());
        //如果没配置，则为true
        if (StringUtils.isBlank(value)) {
            return true;
        }
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        List<FileRecordDO> fileRecordDOList = fileRecordMapper.selectList(new LambdaQueryWrapperX<FileRecordDO>()
                .eqIfPresent(FileRecordDO::getSupplierId, loginUser.getSupplyId())
                .eqIfPresent(FileRecordDO::getOrgId, loginUser.getOrgId())
                .eq(FileRecordDO::getContractId, contractId)
        );
        if (CollectionUtil.isNotEmpty(fileRecordDOList)) {
            List<String> resultIds = fileRecordDOList.stream()
                    .flatMap(fileRecordDO -> Stream.of(fileRecordDO.getSupplierId(), fileRecordDO.getOrgId()))
                    .filter(id -> id != null)  // 过滤掉为null的id
                    .collect(Collectors.toList());
            if (Integer.valueOf(value) <= resultIds.size()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean ifPass1(String id) {
        fileRecordMapper.delete(new LambdaQueryWrapperX<FileRecordDO>().eqIfPresent(FileRecordDO::getContractId, id));
        return null;
    }
}

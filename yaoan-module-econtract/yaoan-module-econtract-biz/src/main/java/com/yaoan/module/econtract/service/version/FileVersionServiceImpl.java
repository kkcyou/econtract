package com.yaoan.module.econtract.service.version;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.econtract.controller.admin.version.vo.FileVersionSaveReqVO;
import com.yaoan.module.econtract.controller.admin.version.vo.list.FileVersionPageReqVO;
import com.yaoan.module.econtract.controller.admin.version.vo.list.FileVersionPageRespVO;
import com.yaoan.module.econtract.convert.version.FileVersionConvert;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.version.FileVersionDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.version.FileVersionMapper;
import com.yaoan.module.econtract.enums.FileVersionEnums;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.MinioUtils;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import liquibase.pro.packaged.F;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;

/**
 * @description:
 * @author: Pele
 * @date: 2024/8/29 11:44
 */
@Slf4j
@Service
public class FileVersionServiceImpl implements FileVersionService {
    @Resource
    private FileVersionMapper fileVersionMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private MinioUtils minioUtils;

    @Override
    public Long save(FileVersionSaveReqVO reqVO) throws Exception {
        String json= JSONObject.toJSONString(reqVO);
        log.info("文件留痕操作参数："+json);
        FileVersionDO fileVersionDO = FileVersionConvert.INSTANCE.r2d(reqVO);
        FileVersionEnums enums = FileVersionEnums.getInstance(reqVO.getBusinessType());
        if (ObjectUtil.isNull(enums)) {
            throw exception(SYSTEM_ERROR, "版本类型异常");
        }
        ContractDO contractDO = contractMapper.selectOne(new LambdaQueryWrapperX<ContractDO>()
                .eq(ContractDO::getId, reqVO.getBusinessId()).select(ContractDO::getId,ContractDO::getEditType,ContractDO::getFileAddId,ContractDO::getName,ContractDO::getTemplateId));
        if (ObjectUtil.isNull(contractDO)) {
            throw exception(SYSTEM_ERROR, "此合同不存在");
        }
        //合同文件
        if (FileVersionEnums.CONTRACT == enums) {
            //如果该合同不是office的编辑类型
            if (ObjectUtil.isEmpty(contractDO.getEditType()) || 1 != contractDO.getEditType()) {
                return null;
            }
            //拷贝文件
            FileDTO fileDTO = fileApi.selectById(contractDO.getFileAddId());
            byte[] fileContentById = fileApi.getFileContentById(contractDO.getFileAddId());
            String suffix = EcontractUtil.getFileSuffix(fileDTO.getName());
            String fileName = IdUtil.fastSimpleUUID() + suffix;
            Long copyFileId = fileApi.uploadFile(fileName, FileUploadPathEnum.FILE_VERSION_PATH.getPath() + EcontractUtil.getTimeFolderPath() + fileName, fileContentById);

            FileDTO copyFileDTO = fileApi.selectById(copyFileId);
            fileVersionDO.setCopyFileId(copyFileId).setCopyFileName(contractDO.getName());
            fileVersionDO.setBusinessName(contractDO.getName());
            fileVersionDO.setCopyFileUrl(copyFileDTO.getUrl());
            fileVersionMapper.insert(fileVersionDO);
        }
        if (FileVersionEnums.MODEL == enums) {


            //模板的文件留痕
            Model model = modelMapper.selectOne(new LambdaQueryWrapperX<Model>()
                    .eq(Model::getId, contractDO.getTemplateId()).select(Model::getId,Model::getRemoteFileId,Model::getName));
            if(ObjectUtil.isNotNull(model)){
                //拷贝文件
                FileDTO fileDTO = fileApi.selectById(model.getRemoteFileId());
                byte[] fileContentById = fileApi.getFileContentById(model.getRemoteFileId());
                String suffix = EcontractUtil.getFileSuffix(fileDTO.getName());
                String fileName = IdUtil.fastSimpleUUID() + suffix;
                Long copyFileId = fileApi.uploadFile(fileName, FileUploadPathEnum.FILE_VERSION_PATH.getPath() + EcontractUtil.getTimeFolderPath() + fileName, fileContentById);

                FileDTO copyFileDTO = fileApi.selectById(copyFileId);
                fileVersionDO.setCopyFileId(copyFileId).setCopyFileName(model.getName());
                fileVersionDO.setBusinessName(model.getName());
                fileVersionDO.setCopyFileUrl(copyFileDTO.getUrl());
                fileVersionMapper.insert(fileVersionDO);
            }

        }


        return fileVersionDO.getId();
    }

    @Override
    public PageResult<FileVersionPageRespVO> page(FileVersionPageReqVO reqVO) {
        PageResult<FileVersionDO> doPageResult = fileVersionMapper.selectPage(reqVO, new LambdaQueryWrapperX<FileVersionDO>()
                .eq(FileVersionDO::getBusinessId, reqVO.getBusinessId())
                .orderByDesc(FileVersionDO::getCreateTime)
        );
        PageResult<FileVersionPageRespVO> respVOPageResult = FileVersionConvert.INSTANCE.pageR2D(doPageResult);
        if (CollectionUtil.isEmpty(respVOPageResult.getList())) {
            return new PageResult<FileVersionPageRespVO>().setTotal(0L).setList(Collections.EMPTY_LIST);
        }
        List<FileVersionDO> versionDOS = fileVersionMapper.selectList(FileVersionDO::getBusinessId, reqVO.getBusinessId());
        Map<Long, FileVersionDO> versionDOSMap = new HashMap<Long, FileVersionDO>();
        List<Long> userIds = versionDOS.stream().map(FileVersionDO::getCreator).map(Long::parseLong).collect(Collectors.toList());
        List<AdminUserRespDTO> userRespDTOList = adminUserApi.getUserList(userIds);
        Map<Long, AdminUserRespDTO> userRespDTOMap = new HashMap<Long, AdminUserRespDTO>();
        if (CollectionUtil.isNotEmpty(userRespDTOList)) {
            userRespDTOMap = CollectionUtils.convertMap(userRespDTOList, AdminUserRespDTO::getId);
        }
        if (CollectionUtil.isNotEmpty(versionDOS)) {
            versionDOSMap = CollectionUtils.convertMap(versionDOS, FileVersionDO::getId);
        }
        List<Long> fileIds = respVOPageResult.getList().stream().map(FileVersionPageRespVO::getCopyFileId).collect(Collectors.toList());
        List<FileDTO> fileDTOS = fileApi.selectBatchIds(fileIds);
        Map<Long, FileDTO> longFileDTOMap = CollectionUtils.convertMap(fileDTOS, FileDTO::getId);
        for (FileVersionPageRespVO respVO : respVOPageResult.getList()) {
            FileVersionDO fileVersionDO = versionDOSMap.get(respVO.getId());
            if (ObjectUtil.isNotNull(fileVersionDO)) {
                AdminUserRespDTO user = userRespDTOMap.get(Long.valueOf(fileVersionDO.getCreator()));
                if (ObjectUtil.isNotNull(user)) {
                    respVO.setCreatorName(user.getNickname());
                }
                FileDTO fileDTO = longFileDTOMap.get(fileVersionDO.getCopyFileId());
                try {
                    respVO.setCopyFileUrl(minioUtils.generatePresignedUrl(fileDTO.getBucketName(), fileDTO.getPath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return respVOPageResult;
    }


}

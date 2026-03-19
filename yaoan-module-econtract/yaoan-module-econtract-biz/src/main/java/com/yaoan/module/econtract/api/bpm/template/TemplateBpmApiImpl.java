package com.yaoan.module.econtract.api.bpm.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.dal.dataobject.bpm.template.TemplateBpmDO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;
import com.yaoan.module.econtract.dal.mysql.bpm.template.TemplateBpmMapper;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.ContractTemplateMapper;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.YhAgentUtil;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;

import static com.yaoan.module.econtract.enums.StatusConstants.READY_TO_UPLOAD_PATH;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/14 18:16
 */
@Slf4j
@Service
public class TemplateBpmApiImpl implements TemplateBpmApi {
    @Resource
    private TemplateBpmMapper templateBpmMapper;
    @Resource
    private ContractTemplateMapper templateMapper;
    @Resource
    private FileApi fileApi;

    @Override
    @DataPermission(enable = false)
    @Transactional(rollbackFor = Exception.class)
    public void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) throws Exception {
        TemplateBpmDO templateBpmDO = templateBpmMapper.selectById(businessKey);
        if (templateBpmDO != null) {
            //1.更新范本审批流表状态
            templateBpmDO.setResult(statusEnums.getResult());
            templateBpmMapper.updateById(templateBpmDO);
            //2.更新范本审批状态
            ContractTemplate templateDO = templateMapper.selectById(templateBpmDO.getTemplateId());
            if (templateDO != null) {
                if (BpmProcessInstanceResultEnum.APPROVE == statusEnums) {
                    //审批通过，将范本文件转成ofd格式
                    if (templateDO.getRemoteFileId() != null) {
                        FileDTO fileDTO = fileApi.selectById(templateDO.getRemoteFileId());
                        if (fileDTO != null) {
                            //将pdf文件下载
                            String fileName = fileDTO.getName();
                            String ofdName = EcontractUtil.exchangeName2Ofd(fileName);
                            String uuid = IdUtil.fastSimpleUUID();
                            Long fileId = fileDTO.getId();
                            String ready2UploadFolderPath = READY_TO_UPLOAD_PATH + "/" + uuid;
                            FileUtil.mkdir(ready2UploadFolderPath);
                            String pdfFilePath = ready2UploadFolderPath + "/" + fileDTO.getName();
                            String ofdFilePath = ready2UploadFolderPath + "/" + ofdName;
                            ByteArrayInputStream inputStream = IoUtil.toStream(fileApi.getFileContentById(fileId));
                            File pdfFile = new File(pdfFilePath);
                            // 将流的内容写入文件
                            FileUtil.writeFromStream(inputStream, pdfFile);

                            //审批通过后，将pdf文件转成ofd
//                            YhAgentUtil.officeToOFD(pdfFilePath, ofdFilePath);
                            //将ofd上传
//                            File ofdFile = new File(ofdFilePath);
//                            FileInputStream fileInputStream = new FileInputStream(ofdFile);
//                            MultipartFile multipartPdfFile = new MockMultipartFile(ofdFile.getName(), ofdFile.getName(), null, fileInputStream);

                            //存入转成ofd的文件id
//                            Long uploadId = fileApi.uploadFile(ofdName, ofdFilePath, IoUtil.readBytes(multipartPdfFile.getInputStream()), 0);
//                            templateDO.setOfdFileId(uploadId);

                            //删除临时目录
                            FileUtil.del(ready2UploadFolderPath);
                        }
                    }
                }
                templateMapper.updateById(templateDO.setApproveStatus(statusEnums.getResult()).setApproveTime(LocalDateTime.now()));
            }
        }
    }


}

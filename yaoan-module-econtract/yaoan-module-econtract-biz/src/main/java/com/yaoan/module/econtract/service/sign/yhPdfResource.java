package com.yaoan.module.econtract.service.sign;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.reach.pdf.sign.base.resource.TempPdfFileResource;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * @author doujiale
 */
@Slf4j
public class yhPdfResource extends TempPdfFileResource {

    private final String tempPath = "/data/doujiale/";


    @Override
    public String getOESDir() {
        return "/data/doujiale/";
    }

    @Override
    public String templateDocuments() {
        return tempPath + UUID.randomUUID().toString() + ".pdf";
    }

    @Override
    @SneakyThrows(value = {Exception.class})
    public void success(String tmpFilePath) {

        FileApi fileApi = SpringUtil.getBean(FileApi.class);
        FileDTO fileDTO = fileApi.selectById(documentId);
        Long signFieId = fileApi.uploadFile(fileDTO.getName(), fileDTO.getPath(), IoUtil.readBytes(FileUtil.getInputStream(tmpFilePath)));
        FileDTO signFile = fileApi.selectById(signFieId);
        fileApi.updateFileInfo(new FileDTO().setId(fileDTO.getId()).setName(fileDTO.getName()).setPath(signFile.getPath()).setSize(signFile.getSize()).setType(signFile.getType()).setUrl(signFile.getUrl()));
        String localPath = "/data/doujiale/temp/" + DateUtil.today() + File.separator + fileDTO.getPath();

        FileOutputStream fileOutputStream = new FileOutputStream(localPath);
        /** 覆写原文件 */
        IOUtils.copy(new FileInputStream(tmpFilePath), fileOutputStream);

        FileUtil.del(tmpFilePath);
    }

    @Override
    public void fail(String s) {

    }

    @Override
    @SneakyThrows(value = {Exception.class})
    public String getPdfFile() {
        FileApi fileApi = SpringUtil.getBean(FileApi.class);
        FileDTO fileDTO = fileApi.selectById(documentId);
        String localPath = "/data/doujiale/temp/" + DateUtil.today() + File.separator + fileDTO.getPath();
        FileUtil.writeFromStream(IoUtil.toStream(fileApi.getFileContent(18L, fileDTO.getPath())), localPath);
        return localPath;
    }


}

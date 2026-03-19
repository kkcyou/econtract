package com.yaoan.module.econtract.service.sign;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.reach.pdf.sign.base.resource.TempPdfFileResource;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.FileConfigApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * @author doujiale
 */
@Slf4j
public class KingGridFileResourceV2 extends TempPdfFileResource {

    private final String tempPath = "/Users/doujiale/Desktop/";

    @Override
    public String templateDocuments() {
        FileApi fileApi = SpringUtil.getBean(FileApi.class);
        FileDTO fileDTO = fileApi.selectById(documentId);

        String tempDirPath = tempPath + fileDTO.getPath();
        FileUtil.mkdir(FileUtil.getParent(tempDirPath, 1));
        return tempPath + fileDTO.getPath();
    }

    @Override
    @SneakyThrows(value = {Exception.class})
    public void success(String tmpFilePath) {
        FileApi fileApi = SpringUtil.getBean(FileApi.class);
        FileDTO fileDTO = fileApi.selectById(documentId);
        Long signFieId = fileApi.uploadFile(fileDTO.getName(), "sign/" + fileDTO.getPath(), IoUtil.readBytes(FileUtil.getInputStream(tmpFilePath)), fileDTO.getBucketName());
        FileDTO signFile = fileApi.selectById(signFieId);
        fileApi.updateFileInfo(new FileDTO().setId(fileDTO.getId()).setName(fileDTO.getName()).setPath(signFile.getPath()).setSize(signFile.getSize()).setType(signFile.getType()).setUrl(signFile.getUrl()));
        FileUtil.del(tmpFilePath);
    }

    @Override
    public void fail(String s) {

    }

    @Override
    public String getDocumentName() {
        return null;
    }

    @Override
    public String getPdfFile() {
        return null;
    }

    @Override
    @SneakyThrows(value = {Exception.class})
    public InputStream getPdfFileStream() {
        FileConfigApi fileConfigApi = SpringUtil.getBean(FileConfigApi.class);
        Long configId = fileConfigApi.selectTrueConfigId();

        FileApi fileApi = SpringUtil.getBean(FileApi.class);
        FileDTO fileDTO = fileApi.selectById(documentId);
        return IoUtil.toStream(fileApi.getFileContent(configId, fileDTO.getPath(), fileDTO.getBucketName()));
    }

}

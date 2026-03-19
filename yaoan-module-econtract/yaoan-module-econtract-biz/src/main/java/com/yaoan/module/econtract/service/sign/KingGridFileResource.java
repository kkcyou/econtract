package com.yaoan.module.econtract.service.sign;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.reach.pdf.sign.base.resource.TempPdfFileResource;
import com.yaoan.module.infra.api.file.FileApi;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * @author doujiale
 */
@Slf4j
public class KingGridFileResource extends TempPdfFileResource {

    @Override
    public String templateDocuments() {
        String tempPath = "/Users/doujiale/Desktop/";
        String tempDirPath = tempPath + documentId;
        FileUtil.mkdir(FileUtil.getParent(tempDirPath, 1));
        return tempPath + documentId;
    }

    @Override
    @SneakyThrows(value = {Exception.class})
    public void success(String tmpFilePath) {
        FileApi fileApi = SpringUtil.getBean(FileApi.class);
        fileApi.createFile("sign/" + documentId, IoUtil.readBytes(FileUtil.getInputStream(tmpFilePath)));
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
        FileApi fileApi = SpringUtil.getBean(FileApi.class);
        return IoUtil.toStream(fileApi.getFileContent(18L, documentId));
    }

    @Override
    @SneakyThrows(value = {Exception.class})
    public void preSuccess() {
        FileApi fileApi = SpringUtil.getBean(FileApi.class);
        fileApi.createFile("sign/" + documentId, IoUtil.readBytes(FileUtil.getInputStream(getPrePdfFile())));
        FileUtil.del(getPrePdfFile());
    }
}

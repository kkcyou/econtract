package com.yaoan.module.econtract.service.sign;

import com.reach.pdf.sign.base.resource.TempPdfFileResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.UUID;

@Slf4j
public class KinsecSiguature extends TempPdfFileResource {

//    private String tempPath = "E:/other_project/reach-cloud-reader-v1.0/src/main/resources/static/files/";
    private String tempPath = "/Users/doujiale/Documents/曜安科技/电子合同/签章对接文件/pdfjs去客户端签章Demo集成/reach-cloud-reader-v1.0/src/main/resources/static/files/";

    @Override
    public String templateDocuments() {
        return tempPath + UUID.randomUUID() + ".pdf";
    }

    @Override
    public void success(String tmpFile) {
        /** 原文档路径 */
        String fileName = tempPath + "/" + getDocumentId();
        /** 临时文件 */
        File file = new File(tmpFile);
        try (FileInputStream in = new FileInputStream(tmpFile);
             FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {

            /** 获取临时文件上印章数量 */
            int tmpFileSealNumber = getSealNumber(tmpFile);
            if (tmpFileSealNumber > 0) {
                System.out.println("临时文件有[" + tmpFileSealNumber + "]印章");
            } else {
                System.out.println("临时文件没有印章");
            }

            /** 覆写原文件 */
            IOUtils.copy(in, fileOutputStream);

            /** 获取最终文件上的印章数量 */
            int endFileSealNumber = getSealNumber(fileName);
            if (endFileSealNumber > 0) {
                System.out.println("最终文件有[" + endFileSealNumber + "]印章");
            } else {
                System.out.println("最终文件没有印章");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /** 删除临时文件 */
        if (file.exists()) {
            file.delete();
        }
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
        String path = tempPath + documentId;
        log.info("path = {}",path);
        return path;
    }

    @Override
    public InputStream getPdfFileStream() {
        return null;
    }

    @Override
    public void preSuccess() {

    }
}

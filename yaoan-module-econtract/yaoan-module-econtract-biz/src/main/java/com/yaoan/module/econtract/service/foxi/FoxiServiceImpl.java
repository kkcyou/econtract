package com.yaoan.module.econtract.service.foxi;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.foxit.sdk.addon.conversion.office2pdf.Office2PDF;
import com.foxit.sdk.addon.conversion.office2pdf.Office2PDFSettingData;
import com.yaoan.module.econtract.controller.admin.foxi.vo.pdfcompare.PdfCompareReqVO;
import com.yaoan.module.econtract.controller.admin.foxi.vo.pdfcompare.PdfCompareRespVO;
import com.yaoan.module.econtract.controller.admin.foxi.vo.save.FoxiSaveReqVO;
import com.yaoan.module.econtract.controller.admin.foxi.vo.save.FoxiSaveRespVO;
import com.yaoan.module.econtract.controller.admin.foxi.vo.word2pdf.Word2PdfRespVO;
import com.yaoan.module.econtract.util.foxi.PDFCompare_Utils;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DATA_ERROR;
import static com.yaoan.module.infra.enums.FileUploadPathEnum.FOXI_PDF;

/**
 * @description:
 * @author: Pele
 * @date: 2025-9-5 16:24
 */
@Slf4j
@Service
public class FoxiServiceImpl implements FoxiService {

    @Resource
    private FileApi fileApi;

    @Value(value = "${foxi.url}")
    private String foxiUrl;

    @Value(value = "${foxi.tmp}")
    private String tmpUrl;
    @Value(value = "${foxi.diff}")
    private String diffUrl;
    @Value(value = "${foxi.resource_folder_path}")
    private String resource_folder_path;

    @Override
    public FoxiSaveRespVO saveBackTest(FoxiSaveReqVO reqVO) {
        log.info("==========【Foxi服务 回调 开始】============");
        FoxiSaveRespVO respVO = new FoxiSaveRespVO();
        try {
            String finalUrl = foxiUrl + reqVO.getData().getDocURL();
            URL url = new URL(finalUrl);
            InputStream inputStream = url.openStream();
            byte[] bytes = inputStreamToBytes(inputStream);
            fileApi.createFile4Foxi(null, null, bytes, finalUrl);
        } catch (Exception e) {
            log.error("Foxi服务 回调异常");
            log.error(e.getMessage());
            return respVO.setStatus(false);
        }
        return respVO.setStatus(true);
    }

    @Override
    public FoxiSaveRespVO saveBack(FoxiSaveReqVO reqVO) {
        log.info("==========【Foxi服务 回调同步 开始】============");
        log.info("参数：" + reqVO.toString());
        FoxiSaveRespVO respVO = new FoxiSaveRespVO();
        Long docId = Long.valueOf(reqVO.getDocId());
        try {
            String finalUrl = foxiUrl + reqVO.getData().getDocURL();
            URL url = new URL(finalUrl);
            InputStream inputStream = url.openStream();
            byte[] bytes = inputStreamToBytes(inputStream);

            fileApi.updateFile4Foxi(null, null, bytes, finalUrl, docId);
            log.info("Foxi服务 回存成功");
        } catch (Exception e) {
            log.error("Foxi服务 回调同步异常");
            log.error(e.getMessage());
            e.printStackTrace();
            return respVO.setStatus(false);
        }
        return respVO.setStatus(true);
    }
//
//    @Resource
//    private PDFCompare_Utils pdfCompareUtils;

    @Override
    public PdfCompareRespVO pdfCompare(PdfCompareReqVO reqVO) {
        Word2PdfRespVO firstPdf = getPdfFromWord(reqVO.getFileId0());
        Word2PdfRespVO secondPdf = getPdfFromWord(reqVO.getFileId1());
        String output_base = "";
        String output_new = "";
        String minioPath_base = "";
        String minioPath_new = "";
        try {
//            //2.两个PDF文件对比
            String input_base = firstPdf.getUrl();
            String input_new = secondPdf.getUrl();
            //结果保存路径
            String base = diffUrl;
            String output_base_name = "diff01_" + IdUtil.fastSimpleUUID() + ".pdf";
            output_base = base + output_base_name;
            String output_new_name = "diff02_" + IdUtil.fastSimpleUUID() + ".pdf";
            output_new = base + output_new_name;
            //开始对比
            log.info("开始比对：input_new：" + input_new + ",input_base:" + input_base);
            log.info("开始比对：output_base：" + output_base + ",output_new:" + output_new);

            PDFCompare_Utils.pdfCompare(input_new, input_base, output_base, output_new);
            log.info("鲲鹏PDF比对完成");
            minioPath_base = getMinioUrl(output_base, output_base_name);
            minioPath_new = getMinioUrl(output_new, output_new_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new PdfCompareRespVO().setFileUrl0(minioPath_base).setFileUrl1(minioPath_new);

    }

    private String getMinioUrl(String outputPath, String output_base_name) {
        //将本地文件上传至minio
        File pdfFile = new File(outputPath);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(pdfFile);

            MultipartFile multipartPdfFile = new MockMultipartFile(pdfFile.getName(), pdfFile.getName(), null, fileInputStream);
            Long fileId = fileApi.uploadFile(pdfFile.getName(), FOXI_PDF.getPath() + pdfFile.getName(), IoUtil.readBytes(multipartPdfFile.getInputStream()));
            return fileApi.getURL(fileId);
        } catch (Exception e) {
            log.error("getMinioUrl异常：" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    static String key = "8f3g1sFttW8NAgfSNv+BtaJq6WX3+7iZs/PdPWkHlLz/s5ukbPWH69OXP4Na4EBsYIs8hVDsyzacr9E2PgCxGWk1bMXQUXHM7g9DivZhQpWv8pZithEUGPoIZVsu8GLSK1/6PzqNdGITP2hZfUJn4JUiazbGLXo+afiyEZR17Y3Gu9QaOIACWNFBGBhGt8CW43ui4D5kd2cXVjGYy4uf3KN6qb6sUU/4t2NBfTlJMMVwLofkED70Nmgb2jn04YDUBAUbUCY2Ut/Jg35PHQClO9U3IWzJHMrcrSdfCOAVX6NvuZit3a/laUvRoYXxbkowFdkbed3M1KPzlpSVrYV5/yoU9VTbCdV1NGzO8K+rfuOkjOfs1E+cUcN1gnFzYxcljmuPW7bvBGEigp5acH+n0BLiWtU5lcEhTh4Ew5MRfmEbjxiVjL5u10zd+f2zsIJbDaJjDsm1rKddx2//oSLKpV6pu0DkzVEKDKAAPOwbJNQh7CN+xxMmdSnnC1FKQd0N8pXA08JJYOmd142pND5V7RXknMEQcEwezBcooVPHOCz3Iv0xKrMlyKne2uPvsF8SP8bDNKma21h5VUdpsCpd2Etwh1pbQpxqc9W4G/rKVWihKY4HZ8lH2Se8Uxe9TxuIVbRbCBHI8lA8Nr853qIiGqfIHsr75m+TNtPrEQLTNbFKQSaZ/BU8/RHphJK/SqqNz1WWVqWRI2SX3dpJqvpg0gkZRjPfTou6shfLw16O8ISbHT1FRm7HP6VRrJJYJHZ3rvWdFqvF9e1puTB+vizC6Vk+/pIce2sDByXVvOKRYbjTQ4b7VzWATC8HJtYclz3BIadxdAShlcabn7Ur2jY1Dc+ioqKo8eoNBwTA1Ds9dws5YtL4P1Nv0VdCzmrRtpOUQroKtMXakP7VDTi49LnaU/XruKG04j4JSmo/1+Ze0+15vQvxVLy+iVPBSAjlkFDTdrpGoiTBLgPBhCenECwY2WtN8cS19njQEjEEUOeNg1/ldlTNAFA/7IQQ65Y1lOZutBdwn3oBS551pCcp48NcgUZChzs3t71QBAEvmmUJyWJbmV+S9e19wmR90NiAf8GI3okpWkLCMhEESsDI/0D3IU/m+mCMelwUNFIOirpomVzdh+1yUMWajyg9q+iaLJFjXNn6gvQqtTdKT8i/poRupolzHy7VeA7TstuZ1kUTHp3pt5sBXA7JXWjpLU+KypryGSe0EeAvUdItB4ywcRO7muyzGqWi4GYbntrLGHkFv1x3U240B3k0udpOge/zI1BVvurMGAbGSpOiR+NxQWIfKcHjDixhobq7vPXPW3hkywYhrtKQJL10sHLy2JsOA6QCBhs9kTxTccZ99VMdB+1wvYIxdCT/6DEUw++HhY/ygMO8gwi1+9E5fGjH84PpRZL/8DczKdtiNv3lpz3t8abBc5pX7tFGY/s3Qg==";
    static String sn = "SoYYRfoFW6eUuePKRWQhdux9MyUpGUOXf3mHZNtKfCV+otpcG1q5iw==";

    @Override
    public void initial() {
    }

    private Word2PdfRespVO getPdfFromWord(Long fileId) {
        FileDTO fileDTO = fileApi.selectById(fileId);
        if (ObjectUtil.isNull(fileDTO)) {
            throw exception(DATA_ERROR);
        }
        if (fileDTO.getUrl().contains(".pdf")) {
            //将minio地址拉到本地磁盘
            //下载文件
            String ready2UploadFolderPath = tmpUrl;
            FileUtil.mkdir(ready2UploadFolderPath);
            String tmpFileName = IdUtil.fastSimpleUUID() + "_" + fileDTO.getName();
            String path = tmpUrl + tmpFileName;
            try {
                ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(fileApi.getFileContentById(fileId));
                FileUtil.writeFromStream(byteArrayInputStream, path);
            } catch (Exception e) {
                log.error("getPdfFromWord异常：" + e.getMessage());
            }

            return new Word2PdfRespVO().setName(tmpFileName).setUrl(path);
        } else {
            // 转pdf
            return convertWord2Pdf(fileDTO);
        }

    }

    public Word2PdfRespVO convertWord2Pdf(FileDTO wordFileDTO) {
        //1.word转pdf
//        Word2PDFSettingData wordSettingsData = new Word2PDFSettingData();
//        //待转换的word路径
//        //PDF保存路径
        String pdfName = IdUtil.fastSimpleUUID() + ".pdf";
        String pdfSavePath = tmpUrl + pdfName;
        try {
            //下载到磁盘
            //下载文件
            String ready2UploadFolderPath = tmpUrl;
            FileUtil.mkdir(ready2UploadFolderPath);
            String wordFilePath = tmpUrl + IdUtil.fastSimpleUUID() + "_" + wordFileDTO.getName();
            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(fileApi.getFileContentById(Long.valueOf(wordFileDTO.getId())));
            File tmpFile = new File(wordFilePath);
            // 将流的内容写入文件
            FileUtil.writeFromStream(byteArrayInputStream, tmpFile);
//            log.info("pdfFilePath：" + pdfFilePath + ",pdfSavePath:" + pdfSavePath + ",wordSettingsData" + wordSettingsData);
//            Convert.fromWord(pdfFilePath, "", pdfSavePath, wordSettingsData);
            String word_file_path = wordFilePath;
            String saved_pdf_path = pdfSavePath;

            //TODO 鲲鹏自研工具，目前仅支持DOCX格式（预计2025年年底支持DOC）
            Office2PDFSettingData office2pdf_setting_data = new Office2PDFSettingData();
            office2pdf_setting_data.setResource_folder_path(resource_folder_path);
            Office2PDF.convertFromWord(word_file_path, "", saved_pdf_path, office2pdf_setting_data);
        } catch (Exception e) {
            log.error("pdf转换异常");
            log.error(e.getMessage());
            e.printStackTrace();
            throw exception(DATA_ERROR);
        }
        log.info("pdf转换完成");
        return new Word2PdfRespVO().setName(pdfName).setUrl(pdfSavePath);
    }


    // 你已实现的 inputStreamToBytes 方法（此处仅为示例，保持与你的代码一致即可）
    private static byte[] inputStreamToBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.flush();
        return bos.toByteArray();
    }


}

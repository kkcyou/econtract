package com.yaoan.module.econtract.service.breakpointUpload;



import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import com.yaoan.module.econtract.controller.admin.breakpointUpload.vo.FileUploadCheckVO;
import com.yaoan.module.econtract.controller.admin.breakpointUpload.vo.FileUploadInfo;
import com.yaoan.module.econtract.controller.admin.breakpointUpload.vo.UploadFileParamVO;
import com.yaoan.module.econtract.enums.BreakpoinUploadEnums;
import com.yaoan.module.infra.api.file.dto.FilesDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface BreakpoinUploadService {
    /**
     * 分片上传初始化
     *
     * @param fileUploadInfo
     * @return Map<String, Object>
     */
    Map<String, Object> initMultiPartUpload(FileUploadInfo fileUploadInfo);

    /**
     * 完成分片上传
     *
     * @param  fileUploadInfo
     * @return String
     */
    Map<String, Object> mergeMultipartUpload(FileUploadInfo fileUploadInfo);

    /**
     *  通过 md5 获取已上传的数据
     * @param md5 String
     * @return Mono<Map<String, Object>>
     */
    FileUploadCheckVO getByFileMD5(String md5);

    /**
     *  获取文件地址
     * @param bucketName
     * @param fileName
     *
     */
    String getFliePath(String bucketName, String fileName);


    /**
     * 单文件上传
     * @param file
     * @param bucketName
     * @return
     */
    String upload(MultipartFile file, String bucketName);

    Object checkFileMd5(String md5, String fileName);

    Object breakpointResumeUpload(UploadFileParamVO param, HttpServletRequest request);

    Map<String, Object> addFile(UploadFileParamVO dto);
}
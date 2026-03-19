package com.yaoan.module.econtract.service.breakpointUpload.impl;


import cn.hutool.core.util.ObjectUtil;
import cn.novelweb.tool.upload.local.LocalUpload;
import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import cn.novelweb.tool.upload.local.LocalUpload;
import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.module.econtract.controller.admin.breakpointUpload.vo.FileUploadCheckVO;
import com.yaoan.module.econtract.controller.admin.breakpointUpload.vo.FileUploadInfo;
import com.yaoan.module.econtract.controller.admin.breakpointUpload.vo.UploadFileParamVO;
import com.yaoan.module.econtract.enums.BreakpoinUploadEnums;
import com.yaoan.module.econtract.service.breakpointUpload.BreakpoinUploadService;
import com.yaoan.module.econtract.util.MinioUtils;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FilesDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;

@Slf4j
@Service
public class BreakpoinUploadServiceImpl implements BreakpoinUploadService {

    @Resource
    private FileApi fileApi;

    @Resource
    private MinioUtils minioUtils;

    @Resource
    private RedisUtils redisUtil;

    @Value("${minio.breakpoint-time}")
    private Integer breakpointTime;

    @Value("${file.save-path}")
    private String savePath;
    @Value("${file.conf-path}")
    private String confFilePath;


    /**
     * 通过 md5 获取已上传的数据（断点续传）
     *
     * @param md5 String
     * @return Mono<Map < String, Object>>
     */
    @Override
    public FileUploadCheckVO getByFileMD5(String md5) {

        log.info("tip message: 通过 <{}> 查询redis是否存在", md5);
        // 从redis获取文件名称和id
        FileUploadInfo fileUploadInfo = (FileUploadInfo) redisUtil.get(md5);
        if (fileUploadInfo != null) {
            // 正在上传，查询上传后的分片数据
            List<Integer> chunkList = minioUtils.getChunkByFileMD5(fileUploadInfo.getFileName(), fileUploadInfo.getUploadId(), minioUtils.getBucketNameByTenantId());
            fileUploadInfo.setChunkUploadedList(chunkList);
            return new FileUploadCheckVO().setCode(BreakpoinUploadEnums.UPLOADING.getCode()).setMessage(BreakpoinUploadEnums.UPLOADING.getMessage())
                    .setChunkUploadedList(chunkList)
                    ;

        }
        log.info("tip message: 通过 <{}> 查询mysql是否存在", md5);
        // 查询数据库是否上传成功
        FilesDTO file = fileApi.getfileInfoByMd5(md5);
        if (ObjectUtil.isNotEmpty(file)) {
//            FileUploadInfo mysqlsFileUploadInfo = new FileUploadInfo();
//            BeanUtils.copyProperties(one, mysqlsFileUploadInfo);
            return new FileUploadCheckVO().setCode(BreakpoinUploadEnums.UPLOADSUCCESSFUL.getCode())
                    .setMessage(BreakpoinUploadEnums.UPLOADSUCCESSFUL.getMessage())
                    .setPdfFileId(file.getId()).setUrl(file.getUrl());
        }
        return new FileUploadCheckVO().setCode(BreakpoinUploadEnums.NOT_UPLOADED.getCode()).setMessage(BreakpoinUploadEnums.NOT_UPLOADED.getMessage());
    }


    /**
     * 文件分片上传
     *
     * @param fileUploadInfo
     * @return Mono<Map < String, Object>>
     */
    @Override
    public Map<String, Object> initMultiPartUpload(FileUploadInfo fileUploadInfo) {

        FileUploadInfo redisFileUploadInfo = (FileUploadInfo) redisUtil.get(fileUploadInfo.getFileMd5());
        if (redisFileUploadInfo != null) {
            fileUploadInfo = redisFileUploadInfo;
        }
        log.info("tip message: 通过 <{}> 开始初始化<分片上传>任务", fileUploadInfo);
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();
        String path = year + "/" + month + "/" + day + "/";
        String url = "part/"+path+fileUploadInfo.getFileMd5() +fileUploadInfo.getFileName();

        // 单文件上传
        if (fileUploadInfo.getChunkNum() == 1) {
            log.info("tip message: 当前分片数量 <{}> 进行单文件上传", fileUploadInfo.getChunkNum());
            FilesDTO files = saveFileToDB(url,fileUploadInfo);
//            String fileName = files.getUrl().substring(files.getUrl().lastIndexOf("/") + 1);
            String bucketNameByTenantId = minioUtils.getBucketNameByTenantId();
            Map<String, Object> uploadObjectUrl = minioUtils.getUploadObjectUrl(url, bucketNameByTenantId);
            uploadObjectUrl.put("pdfFileId", files.getId());
            try {
                uploadObjectUrl.put("url", minioUtils.generatePresignedUrl(bucketNameByTenantId, files.getPath()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return uploadObjectUrl;
        }
        // 分片上传
        else {
            log.info("tip message: 当前分片数量 <{}> 进行分片上传", fileUploadInfo.getChunkNum());
            Map<String, Object> map = minioUtils.initMultiPartUpload(fileUploadInfo, url, fileUploadInfo.getChunkNum(), fileUploadInfo.getContentType(), minioUtils.getBucketNameByTenantId());
            String uploadId = (String) map.get("uploadId");
            fileUploadInfo.setUploadId(uploadId);
            redisUtil.set(fileUploadInfo.getFileMd5(),fileUploadInfo,breakpointTime*60*60*24);
            return map;
        }
    }


    /**
     * 文件合并
     *
     * @param
     * @return String
     */
    @Override
    public Map<String, Object> mergeMultipartUpload(FileUploadInfo fileUploadInfo) {
        log.info("tip message: 通过 <{}> 开始合并<分片上传>任务", fileUploadInfo);
        FileUploadInfo redisFileUploadInfo = (FileUploadInfo) redisUtil.get(fileUploadInfo.getFileMd5());
        if(redisFileUploadInfo!=null){
            fileUploadInfo.setFileName(redisFileUploadInfo.getFileName());
        }
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();
        String path = year + "/" + month + "/" + day + "/";
        String url = "part" +"/"+ path+fileUploadInfo.getFileMd5() +fileUploadInfo.getFileName();
        minioUtils.createBucket(fileUploadInfo.getBucketName());
        boolean result = minioUtils.mergeMultipartUpload(url, fileUploadInfo.getUploadId(), fileUploadInfo.getBucketName());

        //合并成功
        if (result) {
            //存入数据库
            FilesDTO files = saveFileToDB(url,fileUploadInfo);
            redisUtil.del(fileUploadInfo.getFileMd5());
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("pdfFileId", files.getId());
            hashMap.put("url",  files.getUrl());
            return hashMap;
        }
        return null;
    }

    @Override
    public String getFliePath(String bucketName, String fileName) {
        return minioUtils.getFliePath(bucketName, fileName);
    }

    @Override
    public String upload(MultipartFile file, String bucketName) {
        minioUtils.upload(file, bucketName);
        return getFliePath(bucketName, file.getName());
    }

    @Override
    public Object checkFileMd5(String md5, String fileName) {
        try {
            fileName = md5 + "_" + fileName;
            cn.novelweb.tool.http.Result result = LocalUpload.checkFileMd5(md5, fileName, confFilePath, savePath);
            // 上传成功 200 上传一部分 206 未上传 404
            if ("200".equals(result.getCode())) {
                // 查询数据库是否上传成功
                FilesDTO file = fileApi.getfileInfoByMd5(md5);
                if(ObjectUtil.isNotNull(file)){
                    return new FileUploadCheckVO().setCode(BreakpoinUploadEnums.UPLOADSUCCESSFUL.getCode())
                            .setMessage(BreakpoinUploadEnums.UPLOADSUCCESSFUL.getMessage())
                            .setPdfFileId(file.getId()).setUrl(file.getUrl());
                }
            } else if ("206".equals(result.getCode())) {
                return new FileUploadCheckVO().setCode(BreakpoinUploadEnums.UPLOADING.getCode()).setMessage(BreakpoinUploadEnums.UPLOADING.getMessage()).setChunkUploadedList((List<Integer>) result.getData());
            } else if ("404".equals(result.getCode())) {
                return new FileUploadCheckVO().setCode(BreakpoinUploadEnums.NOT_UPLOADED.getCode()).setMessage(BreakpoinUploadEnums.NOT_UPLOADED.getMessage());
            }
            return result.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw exception(SYSTEM_ERROR, "上传失败" + e.getMessage());
        }
    }

    @Override
    public Object breakpointResumeUpload(UploadFileParamVO param, HttpServletRequest request) {
        try {
            log.info("tip message: 通过 <{}> 断点续传上传", param);

            log.info("tip message: 通过 <{}> 记录上传地址", savePath);
            // 这里的 chunkSize(分片大小) 要与前端传过来的大小一致
            param.setName(param.getMd5() + "_" + param.getName());
            log.info("breakpointResumeUpload请求参数："+ param.toString()+","+confFilePath+","+savePath+","+param.getChunkSize()+","+request.toString());
            //
            cn.novelweb.tool.http.Result result = LocalUpload.fragmentFileUploader(param, confFilePath, savePath, param.getChunkSize(), request);
            return result.getMessage();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw exception(SYSTEM_ERROR, "上传失败" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> addFile(UploadFileParamVO param) {
        String url = fileApi.getFileUrlByPath("manager") + "/" + param.getMd5() + "_" + param.getName();
        //存入数据库
        FilesDTO files = new FilesDTO();
        files.setUrl(url);
        files.setPath("manager/" + "/" + param.getMd5() + "_" + param.getName());
        files.setName(param.getName());
        files.setSize(param.getSize());
        files.setFileMd5(param.getMd5());
        files.setConfigId("5");
        files.setSize(param.getSize());
        files.setChunkSize(param.getChunkSize());
        files.setChunkNum(param.getChunks());
        files.setType(param.getType());
        Map<String, Object> map = new HashMap<>();
        String  fileId =  String.valueOf(fileApi.insertFileInfo(files));
        map.put("pdfFileId", fileId);
        map.put("url", url);
        return map;
    }

    private FilesDTO saveFileToDB(String url,FileUploadInfo fileUploadInfo) {
        String allUrl = this.getFliePath(minioUtils.getBucketNameByTenantId(), url);
        //存入数据库
        FilesDTO files = new FilesDTO();
        BeanUtils.copyProperties(fileUploadInfo, files);
        files.setUrl(allUrl);
        files.setPath(url);
        files.setName(fileUploadInfo.getFileName());
        files.setType(fileUploadInfo.getType());
        files.setSize(fileUploadInfo.getFileSize());
        files.setConfigId("18");
        String  fileId =  String.valueOf(fileApi.insertFileInfo(files));
        files.setId(Integer.valueOf(fileId));
        return files;
    }
}
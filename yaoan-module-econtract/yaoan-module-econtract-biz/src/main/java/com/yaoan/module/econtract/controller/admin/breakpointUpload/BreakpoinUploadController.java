package com.yaoan.module.econtract.controller.admin.breakpointUpload;



import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.module.econtract.controller.admin.breakpointUpload.vo.FileUploadCheckVO;
import com.yaoan.module.econtract.controller.admin.breakpointUpload.vo.FileUploadInfo;
import com.yaoan.module.econtract.controller.admin.breakpointUpload.vo.UploadFileParamVO;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.breakpointtype.BreakpointTypeEnums;
import com.yaoan.module.econtract.service.breakpointUpload.BreakpoinUploadService;
import com.yaoan.module.econtract.util.MinioUtils;
import com.yaoan.module.infra.api.file.dto.FilesDTO;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.dal.dataobject.config.SystemConfigDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.common.pojo.CommonResult.error;
import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DATA_ERROR;
import static com.yaoan.module.system.enums.config.SystemConfigKeyEnums.BREAKPOINT_TYPE;

/**：校验文件是否存在，再调用接口：分片初始化，最后调用：完成上传，合并分片
 * minio上传流程
 *
 * 1.检查数据库中是否存在上传文件
 *
 * 2.根据文件信息初始化，获取分片预签名url地址，前端根据url地址上传文件
 *
 * 3.上传完成后，将分片上传的文件进行合并
 *
 * 4.保存文件信息到数据库
 */
@RestController
@Slf4j
@RequestMapping("/upload")
public class BreakpoinUploadController {

    @Resource
    private BreakpoinUploadService uploadService;

    @Resource
    private RedisUtils redisUtil;

    @Resource
    private MinioUtils minioUtils;

    @Resource
    private SystemConfigApi systemConfigApi;

    /**
     * @description 获取上传文件
     * @param fileMD5 文件md5
     * @return {@link  }
     * @author LGY
     * @date 2023/04/26 16:00
     */
    @GetMapping("/getUploadingFile/{fileMD5}")
    public CommonResult getUploadingFile(@PathVariable String fileMD5) {
        if (StringUtils.isBlank(fileMD5)) {
            return error(ErrorCodeConstants.DIY_ERROR.getCode(),"未查询到该文件");
        }
        FileUploadInfo fileUploadInfo = (FileUploadInfo) redisUtil.get(fileMD5);
        if (fileUploadInfo != null) {
            // 查询上传后的分片数据
            fileUploadInfo.setChunkUploadedList(minioUtils.getChunkByFileMD5(fileUploadInfo.getFileName(), fileUploadInfo.getUploadId(), fileUploadInfo.getBucketName()));
            minioUtils.createBucket(fileUploadInfo.getBucketName());
            return  success(fileUploadInfo);
        }
        return error(ErrorCodeConstants.DIY_ERROR.getCode(),"未查询到该文件");
    }

    /**
     *
     * Minio分片上传
     * */
    /**
     * 校验文件是否存在（Minio分片）
     *
     * @param md5 String
     * @return ResponseResult<Object>
     */
    @GetMapping("/multipart/check")
    public CommonResult<FileUploadCheckVO> checkFileUploadedByMd5(@RequestParam("md5") String md5) {
        log.info("REST: 通过查询 <{}> 文件是否存在、是否进行断点续传", md5);
        if (StringUtils.isEmpty(md5)) {
            log.error("查询文件是否存在、入参无效");
            return error(ErrorCodeConstants.DIY_ERROR.getCode(), "请检查文件md5是否正确。");
        }
        return success(uploadService.getByFileMD5(md5));
    }

    /**
     * 分片初始化（Minio分片）
     *
     * @param fileUploadInfo 文件信息
     * @return ResponseResult<Object>
     */
    @PostMapping("/multipart/init")
    public CommonResult<Map<String, Object>> initMultiPartUpload(@RequestBody FileUploadInfo fileUploadInfo) {
        log.info("REST: 通过 <{}> 初始化上传任务", fileUploadInfo);
        return  success(uploadService.initMultiPartUpload(fileUploadInfo));
    }

    /**
     * 完成上传（Minio分片）
     *
     * @param fileUploadInfo  文件信息
     * @return ResponseResult<Object>
     */
    @PostMapping("/multipart/merge")
    public CommonResult<Map<String, Object>> completeMultiPartUpload(@RequestBody FileUploadInfo fileUploadInfo) {
        log.info("REST: 通过 {} 合并上传任务", fileUploadInfo);
        //合并文件
        Map<String, Object> url = uploadService.mergeMultipartUpload(fileUploadInfo);
        //获取上传文件地址

            return success(url);

//        return error(ErrorCodeConstants.DIY_ERROR.getCode(),"合并文件失败");
    }

    @PostMapping("/multipart/uploadScreenshot")
    public CommonResult<Boolean> uploaduploadScreenshot(@RequestPart("photos") MultipartFile[] photos,
                                    @RequestParam("buckName") String buckName) {
        log.info("REST: 上传文件信息 <{}> ", photos);

        for (MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                uploadService.upload(photo, buckName);
            }
        }
        return success(true);
    }


    @RequestMapping("/createBucket")
    public void createBucket(@RequestParam("bucketName") String bucketName) {
        String bucket = minioUtils.createBucket(bucketName);
    }

    // 上传文件到本地磁盘
    /**
     * 检查文件MD5（文件MD5若已存在进行秒传）
     *
     * @param md5      md5
     * @param fileName 文件名称
     * @return {@link CommonResult}<{@link Object}>
     */
    @GetMapping(value = "/check-file")
    public CommonResult<Object> checkFileMd5(String md5, String fileName) {
        return success(uploadService.checkFileMd5(md5, fileName));
    }

    /**
     * 断点续传方式上传文件：用于大文件上传
     *
     * @param param   参数
     * @param request 请求
     * @return {@link CommonResult}<{@link Object}>
     */
    @PostMapping(value = "/breakpoint-upload", consumes = "multipart/*", headers = "content-type=multipart/form-data", produces = "application/json;charset=UTF-8")
    public CommonResult<Object> breakpointResumeUpload(UploadFileParamVO param, HttpServletRequest request) {
        return success(uploadService.breakpointResumeUpload(param, request));
    }

    /**
     * 断点续传完成后上传文件信息进行入库操作
     *
     * @param dto           dto
     * @return {@link CommonResult}<{@link String}>
     */
    @PostMapping("/add")
    public CommonResult<Map<String, Object>> addFile(@RequestBody UploadFileParamVO dto) {

        return success(uploadService.addFile(dto));
    }

}

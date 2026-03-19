package com.yaoan.module.infra.controller.admin.file;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.servlet.ServletUtils;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.infra.controller.admin.file.vo.copy.FileCopyReqVO;
import com.yaoan.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import com.yaoan.module.infra.controller.admin.file.vo.file.FileRespVO;
import com.yaoan.module.infra.controller.admin.file.vo.file.FileUploadExtReqVO;
import com.yaoan.module.infra.controller.admin.file.vo.file.FileUploadReqVO;
import com.yaoan.module.infra.convert.file.FileConvert;
import com.yaoan.module.infra.dal.dataobject.file.FileDO;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.infra.service.file.FileService;
import com.yaoan.module.infra.util.TwoTuple;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLDecoder;
import java.util.Map;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 文件存储")
@RestController
@RequestMapping("/infra/file")
@Validated
@Slf4j
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    @OperateLog(logArgs = false) //TODO 上传文件，没有记录操作日志的必要
    public CommonResult<String> uploadFile(FileUploadReqVO uploadReqVO) throws Exception {
//        MultipartFile file = uploadReqVO.getFile();
//        String path = uploadReqVO.getPath();
//        return success(fileService.createFile(file.getOriginalFilename(), path, IoUtil.readBytes(file.getInputStream())));
        return success("http://test.yudao.iocoder.cn/8e08dd08d99badd61f69cb848afc5b902502640c07684b0a4b525be46bc81b9d.png");

    }

    @PostMapping("/v2/upload")
    @Operation(summary = "上传文件")
    @OperateLog(logArgs = false) // 上传文件，没有记录操作日志的必要
    public CommonResult<Map<String, Object>> uploadFileV2(FileUploadReqVO uploadReqVO) throws Exception {
        MultipartFile file = uploadReqVO.getFile();
        String path = uploadReqVO.getPath();
        return success(fileService.createFileV2(file.getOriginalFilename(), path, IoUtil.readBytes(file.getInputStream())));
    }

    @PostMapping("/v3/upload")
    @Operation(summary = "上传文件")
    @OperateLog(logArgs = false) // 上传文件，没有记录操作日志的必要
    public CommonResult<Map<String, Object>> uploadFileV3(FileUploadExtReqVO uploadReqVO) throws Exception {
        MultipartFile file = uploadReqVO.getFile();
        String path = uploadReqVO.getPath();
        FileUploadPathEnum uploadPathEnum = FileUploadPathEnum.getInstance(uploadReqVO.getCode());
        return success(fileService.createFileV3(file.getOriginalFilename(), uploadPathEnum, path, IoUtil.readBytes(file.getInputStream())));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除文件")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('infra:file:delete')")
    public CommonResult<Boolean> deleteFile(@RequestParam("id") Long id) throws Exception {
        fileService.deleteFile(id);
        return success(true);
    }

    @GetMapping(value = "/{configId}/get/**", produces = "text/plain;charset=UTF-8")
    @PermitAll
    @Operation(summary = "下载文件")
    @Parameter(name = "configId", description = "配置编号", required = true)
    public void getFileContent(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable("configId") Long configId) throws Exception {
        // 获取请求的路径
        String decodedStr = URLDecoder.decode(request.getRequestURI(), "UTF-8");
        String path = StrUtil.subAfter(decodedStr, "/get/", false);
        if (StrUtil.isEmpty(path)) {
            throw new IllegalArgumentException("结尾的 path 路径必须传递");
        }

        // 读取内容
        byte[] content = fileService.getFileContent(configId, path);
        if (content == null) {
            log.warn("[getFileContent][configId({}) path({}) 文件不存在]", configId, path);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        ServletUtils.writeAttachment(response, path, content);
    }

    /**
     * 根据文件id下载文件
     */
    @GetMapping(value = "/get/{fileId}")
    @PermitAll
    @Operation(summary = "根据文件id下载文件")
    @Parameter(name = "fileId", description = "文件id", required = true)
    public void getFileContentByFileId(HttpServletResponse response,
                                       @PathVariable("fileId") Long fileId) throws Exception {
        // 获取文件路径信息
        TwoTuple<Long, String> targetInfo = fileService.getFileTargetInfo(fileId);

        // 读取内容
        byte[] content = fileService.getFileContent(targetInfo.getFirst(), targetInfo.getSecond());
        if (content == null) {
            log.warn("[getFileContent][configId({}) path({}) 文件不存在]", targetInfo.getFirst(), targetInfo.getSecond());
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        ServletUtils.writeAttachment(response, targetInfo.getSecond(), content);
    }

    /**
     * 根据文件id获取文件url路径
     */
    @GetMapping(value = "/url/{fileId}")
    @PermitAll
    @Operation(summary = "根据文件id获取文件url")
    @Parameter(name = "fileId", description = "文件id", required = true)
    public CommonResult<String> getFileUrlByFileId(@PathVariable("fileId") Long fileId) throws Exception {
        // 获取文件路径url
        return success(fileService.getFileUrl(fileId));
    }
    /**
     * 根据文件id获取文件信息
     */
    @GetMapping(value = "/info/{fileId}")
    @PermitAll
    @Operation(summary = "根据文件id获取文件信息")
    @Parameter(name = "fileId", description = "文件id", required = true)
    public CommonResult<FileRespVO> getFileInfoByFileId(@PathVariable("fileId") Long fileId) throws Exception {
        // 根据文件id获取文件url路径和名字
        return success(fileService.getFileInfoByFileId(fileId));
    }

    @GetMapping("/page")
    @Operation(summary = "获得文件分页")
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<PageResult<FileRespVO>> getFilePage(@Valid FilePageReqVO pageVO) {
        PageResult<FileDO> pageResult = fileService.getFilePage(pageVO);
        return success(FileConvert.INSTANCE.convertPage(pageResult));
    }

    /**
     * 复制文件
     * @return FileRespVO 文件信息
     */
    @PostMapping(value = "/copyFile")
    @PermitAll
    @Operation(summary = "复制文件")
    @Parameter(name = "fileId", description = "文件id", required = true)
    public CommonResult<FileRespVO> copyFile(@RequestBody FileCopyReqVO vo) throws Exception {
        return success(fileService.copyFile(vo));
    }
}

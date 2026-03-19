package com.yaoan.module.infra.api.file;

import com.yaoan.framework.common.util.servlet.ServletUtils;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.infra.api.file.dto.FileUploadRespDTO;
import com.yaoan.module.infra.api.file.dto.FileWpsDTO;
import com.yaoan.module.infra.api.file.dto.FilesDTO;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.infra.service.file.FileService;
import com.yaoan.module.infra.util.TwoTuple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;

/**
 * 文件 API 实现类
 *
 * @author 芋道源码
 */
@Slf4j
@Service
@Validated
public class FileApiImpl implements FileApi {

    @Resource
    private FileService fileService;

    @Override
    public String createFile(String name, String path, byte[] content) {
        return fileService.createFile(name, path, content);
    }
    @Override
    public String createFile4Foxi(String name, String path, byte[] content, String url) {
        return fileService.createFile4Foxi(name, path, content,url);
    }
    @Override
    public Long uploadFile(String name, String path, byte[] content) {
        return fileService.uploadFile(name, path, content);
    }
    @Override
    public Long uploadFile(String name, String path, byte[] content, String bucketName) {
        return fileService.uploadFile(name, path, content, bucketName);
    }
    @Override
    public FileDTO uploadFileV2(String name, String path, byte[] content) {
        return fileService.uploadFileV2(name, path, content);
    }

    @Override
    public Long uploadFile(String name, String path, byte[] content, int pageCount) {
        return fileService.uploadFile(name, path, content, pageCount);
    }


    @Override
    public byte[] getFileContent(Long configId, String path) throws Exception {
        return fileService.getFileContent(configId, path);
    }
    @Override
    public byte[] getFileContent(Long configId, String path, String bucketName) throws Exception {
        return fileService.getFileContent(configId, path, bucketName);
    }

    @Override
    public String getPath(Long fileId) throws Exception {
        return fileService.getPath(18L, fileId);
    }

    @Override
    public String getURL(Long fileId) throws Exception {
        return fileService.getURL(18L, fileId);
    }

    @Override
    public String getName(Long fileId) throws Exception {
        return fileService.getName(18L, fileId);
    }

    @Override
    public int getPageCount(Long fileId) throws Exception {
        return fileService.getPageCount(18L, fileId);
    }

    @Override
    public byte[] getFileContentById(Long fileId) throws Exception {
        return fileService.getFileContentById(18L, fileId);
    }

    @Override
    public List<FileDTO> selectBatchIds(List<Long> pathIds) {
        return fileService.selectBatchIds(pathIds);
    }

    @Override
    public FileDTO selectById(Serializable id) {
        return fileService.selectById(id);
    }

    @Override
    public FileWpsDTO selectWpsDTOById(Serializable id) {
        return fileService.selectWpsDTOById(id);
    }

    @Override
    public Boolean updateFileInfo(FileDTO fileDTO) {
        return fileService.updateFileInfo(fileDTO);
    }

    @Override
    public List<FileDTO> getContractFileByUserList(List<Long> userIds) {
        return fileService.getContractFileList(userIds);
    }

    @Override
    public Boolean updateFileByWps(FileWpsDTO fileWpsDTO) {
        return  fileService.updateFileByWps(fileWpsDTO);
    }

    @Override
    public void deleteFile(String path) throws Exception {
        fileService.deleteFile(18L, path);
    }

    @Override
    public void deleteFile(Long id) throws Exception {
        fileService.deleteFile(id);
    }

    /**
     * 畅写生成的文件的信息更新
     */
    @Override
    public Boolean updateCXFile(Long fileId, String fileName, String cxUrl, byte[] bytes, FileDTO fileDTO) {
        return fileService.updateCXFile(fileId, fileName, cxUrl, bytes, fileDTO);
    }

    @Override
    public void updateFile4Foxi(Object o, Object o1, byte[] bytes, String finalUrl, Long docId) {
        fileService.updateFoxiFile(bytes, finalUrl, docId);
    }
    @Override
    public Map<String, Object> uploadFileV3(String originalFilename, FileUploadPathEnum uploadPathEnum, String path, byte[] readBytes) {
        return fileService.createFileV3(originalFilename, uploadPathEnum, path,readBytes);
    }

    @Override
    public FileUploadRespDTO uploadFileVP(String originalFilename, FileUploadPathEnum uploadPathEnum,  byte[] readBytes) {
        return fileService.uploadFileVP(originalFilename, uploadPathEnum, readBytes);

    }

    @Override
    public Long createFileInfo(FileDTO fileDTO) {
        return fileService.createFileInfo(fileDTO);
    }

    /**
     * 下载文件
     *
     * @param response
     * @param fileId
     */
    @Override
    public void getFileContentByFileId(HttpServletResponse response, Long fileId) {
        try {
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
        }catch (Exception e){
            throw  exception(SYSTEM_ERROR,"下载失败");
        }

    }

    /**
     * 获取拼接域名的文件地址
     * @param fileId
     * @return
     */
    @Override
    public String getFileUrlV2Api(Long fileId) {
        return fileService.getFileUrlV2(fileId);
    }

    @Override
    public FilesDTO getfileInfoByMd5(String md5) {
        return fileService.getfileInfoByMd5(md5);
    }

    @Override
    public Long insertFileInfo(FilesDTO files) {
        return fileService.insertFileInfo(files);
    }

    @Override
    public String getFileUrlByPath(String savePath) {
        return fileService.getFileUrlByPath(savePath);
    }

}

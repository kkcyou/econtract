package com.yaoan.module.infra.api.file;

import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.infra.api.file.dto.FileUploadRespDTO;
import com.yaoan.module.infra.api.file.dto.FileWpsDTO;
import com.yaoan.module.infra.api.file.dto.FilesDTO;
import com.yaoan.module.infra.enums.FileUploadPathEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 文件 API 接口
 *
 * @author 芋道源码
 */
public interface FileApi {

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param content 文件内容
     * @return 文件路径
     */
    default String createFile(byte[] content) {
        return createFile(null, null, content);
    }

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param path    文件路径
     * @param content 文件内容
     * @return 文件路径
     */
    default String createFile(String path, byte[] content) {
        return createFile(null, path, content);
    }

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param name    文件名称
     * @param path    文件路径
     * @param content 文件内容
     * @return 文件路径
     */
    String createFile(String name, String path, byte[] content);
    String createFile4Foxi(String name, String path, byte[] content, String url);

    /**
     * 保存文件，并返回文件表中对应保存信息id
     *
     * @param name    文件名称
     * @param path    文件路径
     * @param content 文件内容
     * @return 文件表中对应保存信息id
     */
    Long uploadFile(String name, String path, byte[] content);
    Long uploadFile(String name, String path, byte[] content, String bucketName);

    /**
     * 保存文件，并返回文件表中对应保存信息id
     *
     * @param name    文件名称
     * @param path    文件路径
     * @param content 文件内容
     * @return 文件表中对应保存信息id
     */
    FileDTO uploadFileV2(String name, String path, byte[] content);

    /**
     * 保存文件，并返回文件表中对应保存信息id
     *
     * @param name      文件名称
     * @param path      文件路径
     * @param content   文件内容
     * @param pageCount 文件页数
     * @return 文件表中对应保存信息id
     */
    Long uploadFile(String name, String path, byte[] content, int pageCount);


    /**
     * 根据path删除文件
     */
    void deleteFile(String path) throws Exception;


    /**
     * 根据id删除文件
     */
    void deleteFile(Long id) throws Exception;

    /**
     * 获得文件内容
     *
     * @param configId 配置编号
     * @param path     文件路径
     * @return 文件内容
     */
    byte[] getFileContent(Long configId, String path) throws Exception;
    byte[] getFileContent(Long configId, String path, String bucketName) throws Exception;

    /**
     * 通过文件ID获得文件内容
     *
     * @param fileId 文件id
     * @return 文件的地址
     */
    String getPath(Long fileId) throws Exception;

    String getURL(Long fileId) throws Exception;

    String getName(Long fileId) throws Exception;

    int getPageCount(Long fileId) throws Exception;

    /**
     * 获得文件内容
     *
     * @param fileId 文件编号
     * @return 文件内容
     */
    byte[] getFileContentById(Long fileId) throws Exception;

    /**
     * 根据文件id查询文件信息
     */
    List<FileDTO> selectBatchIds(List<Long> pathIds);

    /**
     * 根据文件id查询文件信息
     */
    FileDTO selectById(Serializable id);

    /**
     * 根据文件id查询文件信息
     */
    FileWpsDTO selectWpsDTOById(Serializable id);

    /**
     * 更新文件信息
     */
    Boolean updateFileInfo(FileDTO fileDTO);

    List<FileDTO> getContractFileByUserList(List<Long> userIds);

    Boolean updateFileByWps(FileWpsDTO fileWpsDTO);

    /**
     * 保留文件id，url会变
     */
    Boolean updateCXFile(Long fileId, String fileName, String savePath, byte[] bytes, FileDTO fileDTO );

    Map<String, Object> uploadFileV3(String originalFilename, FileUploadPathEnum uploadPathEnum, String path, byte[] readBytes);

    FileUploadRespDTO uploadFileVP(String originalFilename, FileUploadPathEnum uploadPathEnum,   byte[] readBytes);

    Long createFileInfo(FileDTO fileDTO);


    /**
     * 下载文件
     */
    void getFileContentByFileId(HttpServletResponse response, Long fileId);

    String getFileUrlV2Api(Long pdfFileId);

    FilesDTO getfileInfoByMd5(String md5);

    Long  insertFileInfo(FilesDTO files);

    void updateFile4Foxi(Object o, Object o1, byte[] bytes, String finalUrl, Long docId);


    String getFileUrlByPath(String savePath);}

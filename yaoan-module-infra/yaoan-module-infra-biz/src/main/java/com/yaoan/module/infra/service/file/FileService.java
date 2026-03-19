package com.yaoan.module.infra.service.file;

import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.infra.api.file.dto.FileUploadRespDTO;
import com.yaoan.module.infra.api.file.dto.FileWpsDTO;
import com.yaoan.module.infra.api.file.dto.FilesDTO;
import com.yaoan.module.infra.controller.admin.file.vo.copy.FileCopyReqVO;
import com.yaoan.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.infra.controller.admin.file.vo.file.FileRespVO;
import com.yaoan.module.infra.dal.dataobject.file.FileDO;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.infra.util.TwoTuple;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 文件 Service 接口
 *
 * @author 芋道源码
 */
public interface FileService {

    /**
     * 获得文件分页
     *
     * @param pageReqVO 分页查询
     * @return 文件分页
     */
    PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO);

    /**
     * 保存文件，并返回文件的访问路径
     *
     * @param name    文件名称
     * @param path    文件路径
     * @param content 文件内容
     * @return 文件路径
     */
    String createFile(String name, String path, byte[] content);

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
     * 删除文件
     *
     * @param id 编号
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
     * 根据path删除文件
     */
    void deleteFile(Long configId, String path) throws Exception;


    public String getPath(Long configId, Long fileId) throws Exception;

    public String getURL(Long configId, Long fileId) throws Exception;

    byte[] getFileContentById(long l, Long fileId) throws Exception;

    String getName(long l, Long fileId);

    int getPageCount(long l, Long fileId);

    /**
     * 根据文件id查询文件信息
     */
    List<FileDTO> selectBatchIds(List<Long> pathIds);

    /**
     * @param fileId 文件ID
     * @return 文件服务ID 文件path
     */
    TwoTuple<Long, String> getFileTargetInfo(Long fileId);


    Map<String, Object> createFileV2(String originalFilename, String path, byte[] readBytes);

    String getFileUrl(Long fileId);

    FileDTO selectById(Serializable id);

    FileWpsDTO selectWpsDTOById(Serializable id);

    Boolean updateFileInfo(FileDTO fileDTO);

    Map<String, Object>  createFileV3(String originalFilename, FileUploadPathEnum uploadPathEnum, String path, byte[] readBytes);

    List<FileDTO> getContractFileList(List<Long> userIds);

    Boolean updateFileByWps(FileWpsDTO fileWpsDTO);

    FileRespVO getFileInfoByFileId(Long fileId);

    Boolean updateCXFile(Long fileId, String fileName, String cxUrl, byte[] bytes, FileDTO fileDTO );
    void updateFoxiFile(byte[] bytes, String finalUrl, Long docId);

    Long createFileInfo(FileDTO fileDTO);

    String getFileUrlV2(Long fileId);

    /**
     * @param vo
     * @return {@link FileRespVO }
     */
    FileRespVO copyFile(FileCopyReqVO vo);

    FileUploadRespDTO uploadFileVP(String originalFilename, FileUploadPathEnum uploadPathEnum,   byte[] readBytes);

    FilesDTO getfileInfoByMd5(String md5);

    Long insertFileInfo(FilesDTO files);

    String createFile4Foxi(String name, String path, byte[] content,String url);
    String getFileUrlByPath(String savePath);

}

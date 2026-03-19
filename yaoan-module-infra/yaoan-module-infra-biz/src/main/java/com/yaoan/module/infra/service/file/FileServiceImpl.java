package com.yaoan.module.infra.service.file;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.io.FileUtils;
import com.yaoan.framework.file.core.client.FileClient;
import com.yaoan.framework.file.core.client.local.LocalFileClientConfig;
import com.yaoan.framework.file.core.utils.FileTypeUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.api.minio.MinioApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.infra.api.file.dto.FileUploadRespDTO;
import com.yaoan.module.infra.api.file.dto.FileWpsDTO;
import com.yaoan.module.infra.api.file.dto.FilesDTO;
import com.yaoan.module.infra.controller.admin.file.vo.copy.FileCopyReqVO;
import com.yaoan.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import com.yaoan.module.infra.controller.admin.file.vo.file.FileRespVO;
import com.yaoan.module.infra.convert.file.FileConvert;
import com.yaoan.module.infra.dal.dataobject.file.FileConfigDO;
import com.yaoan.module.infra.dal.dataobject.file.FileDO;
import com.yaoan.module.infra.dal.dataobject.file.FileFastDO;
import com.yaoan.module.infra.dal.mysql.file.FileConfigMapper;
import com.yaoan.module.infra.dal.mysql.file.FileFastMapper;
import com.yaoan.module.infra.dal.mysql.file.FileMapper;
import com.yaoan.module.infra.enums.FileConfigEnum;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.infra.util.TwoTuple;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.config.dto.SystemConfigRespDTO;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DATA_ERROR;
import static com.yaoan.module.infra.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.infra.enums.ErrorCodeConstants.FILE_NOT_EXISTS;
import static com.yaoan.module.infra.enums.FileUploadPathEnum.FOXI_SAVE_PATH;

/**
 * 文件 Service 实现类
 *
 * @author 芋道源码
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileConfigService fileConfigService;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private FileFastMapper fileFastMapper;

    @Resource
    private SystemConfigApi systemConfigApi;

    @Override
    public PageResult<FileDO> getFilePage(FilePageReqVO pageReqVO) {
        return fileMapper.selectPage(pageReqVO);
    }

    @Override
    @SneakyThrows
    public String createFile(String name, String path, byte[] content) {
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);

        MinioApi minioApi = SpringUtil.getBean(MinioApi.class);
        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);
        file.setBucketName(minioApi.getBucketNameByTenantId());
        fileMapper.insert(file);
        return url;
    }
    @Override
    @SneakyThrows
    public String createFile4Foxi(String name, String path, byte[] content,String docUrl) {
        // 计算默认的 path 名
        String type = "docx";
        name = DigestUtil.sha256Hex(content) +"."+type;
        if (StrUtil.isEmpty(path)) {
            path = FOXI_SAVE_PATH + FileUtils.generatePath(content, name);
        }
        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);

        MinioApi minioApi = SpringUtil.getBean(MinioApi.class);
        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(type);
        file.setBucketName(minioApi.getBucketNameByTenantId());
        file.setSize(content.length);
        fileMapper.insert(file);
        return url;
    }

    @Override
    @SneakyThrows
    public void updateFoxiFile(byte[] content, String cxUrl, Long fileId) {
        // 福昕默认格式

        FileDO fileDO = fileMapper.selectById(fileId);
        if(ObjectUtil.isNull(fileDO)){
            throw exception(FILE_NOT_EXISTS);
        }
        String type = getFileType(fileDO.getName());
        String name = DigestUtil.sha256Hex(content) + "." + type;
        String path = fileDO.getPath();
        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
//        String newUrl = createFile(name, FOXI_SAVE_PATH.getPath()+name, content);
//        log.info("上传文件："+newUrl);
        try{
            log.info("删除文件");
            client.delete(path);
            String url = client.upload(content, path, type);
            String url2 = client.upload(content, "/contract/appendix/123.docx", type);
            log.info("上传文件1：" + url);
            log.info("上传文件2：" + url2);

            // 保存到数据库
            FileDO file = new FileDO();
            file.setId(fileId);
            file.setConfigId(client.getId());
            file.setName(name);
            file.setPath(path);
            file.setUrl(url);
            file.setType(type);
            file.setSize(content.length);
            fileMapper.updateById(file);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private String getFileType(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex != -1 && lastIndex < fileName.length() - 1) {
            String result = fileName.substring(lastIndex + 1);
            return result;
        } else {
           return "";
        }
    }

    @Override
    @SneakyThrows
    public Map<String, Object> createFileV2(String name, String path, byte[] content) {
        MinioApi minioApi = SpringUtil.getBean(MinioApi.class);
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);
        file.setBucketName(minioApi.getBucketNameByTenantId());
        fileMapper.insert(file);

        Map<String, Object> result = new HashMap();
        result.put("url", url);
        result.put("fileId", file.getId());
        return result;
    }

    @Override
    public String getFileUrl(Long fileId) {

        FileDO fileDO = fileMapper.selectById(fileId);

        Assert.notNull(fileDO, "数据检测为空");

        // 如果fileDO.getUrl==null, 说明此时在进行畅写转pdf处理。捕获控制正异常
        if (fileDO.getUrl() == null){
            throw exception(DIY_ERROR,"文件转换生成中...请稍后刷新重试.");
        }
        //如果是mino的文件
        if(FileConfigEnum.CONTRACT_SIGNING.getId().equals(fileDO.getConfigId())){
            MinioApi minioApi = SpringUtil.getBean(MinioApi.class);
            // 如果minio的桶是private的，则返回临时访问地址
            String bucketName = fileDO.getBucketName();
            if (StringUtils.isEmpty(bucketName)){
                bucketName = minioApi.getBucketNameByTenantId();
            }
            return minioApi.generatePresignedUrl(bucketName, fileDO.getPath());
            // 如果minio的桶是public的，则直接返回文件地址
//            return fileDO.getUrl();
        }

        String path = fileDO.getPath();
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(SystemConfigKeyEnums.FILE_SERVER_DOMAIN.getKey());
        List<SystemConfigRespDTO> configsByCKeys = systemConfigApi.getConfigsByCKeys(keyList);
        if (configsByCKeys.size() > 0) {
            String prefix = configsByCKeys.get(0).getCValue();
            String result = prefix + path;
            return result;
        }

        return fileDO.getUrl();
    }


    @Override
    public FileDTO selectById(Serializable id) {
        FileDO fileDO = fileMapper.selectById(id);
        if (fileDO == null) {
            throw exception(FILE_NOT_EXISTS);
        }
        return FileConvert.INSTANCE.toDTO(fileDO);
    }

    @Override
    public FileWpsDTO selectWpsDTOById(Serializable id) {
        FileDO fileDO = fileMapper.selectById(id);
        if (fileDO == null) {
            throw exception(FILE_NOT_EXISTS);
        }
        return FileConvert.INSTANCE.toWpsDTO(fileDO);
    }

    @Override
    public Boolean updateFileInfo(FileDTO fileDTO) {
        FileDO fileDO = FileConvert.INSTANCE.toDO(fileDTO);
        return fileMapper.updateById(fileDO) > 0;
    }

    @Override
    @SneakyThrows
    public Map<String, Object> createFileV3(String name, FileUploadPathEnum uploadPathEnum, String path, byte[] content) {
        MinioApi minioApi = SpringUtil.getBean(MinioApi.class);

        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (uploadPathEnum != null) {
            path = uploadPathEnum.getPath() + FileUtils.generatePath(content, name);
        }
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type, minioApi.getBucketNameByTenantId());

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setBucketName(minioApi.getBucketNameByTenantId());
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(SystemConfigKeyEnums.FILE_SERVER_DOMAIN.getKey());
        List<SystemConfigRespDTO> configsByCKeys = systemConfigApi.getConfigsByCKeys(keyList);
        String prefix = configsByCKeys.get(0).getCValue();
        String resultPath = prefix + path;
        //如果是mino的文件
        if(FileConfigEnum.CONTRACT_SIGNING.getId().equals(file.getConfigId())){
            // 如果minio的桶是private的，则返回临时访问地址
            resultPath = url;
        }
        file.setUrl(resultPath);
        file.setType(type);
        file.setSize(content.length);
        file.setBucketName(minioApi.getBucketNameByTenantId());
        fileMapper.insert(file);

        Map<String, Object> result = new HashMap();
        if(FileConfigEnum.CONTRACT_SIGNING.getId().equals(file.getConfigId())){
            // 如果minio的桶是private的，则返回临时访问地址
            String bucketName = file.getBucketName();
            if (StringUtils.isEmpty(bucketName)){
                bucketName = minioApi.getBucketNameByTenantId();
            }
            resultPath =  minioApi.generatePresignedUrl(bucketName, file.getPath());
            // 如果minio的桶是public的，则直接返回文件地址
//            resultPath =  fileDO.getUrl();
        }
        result.put("url", resultPath);
        result.put("fileId", file.getId());
        return result;
    }

    @SneakyThrows
    public Map<String, Object> createFileCX(String name, FileUploadPathEnum uploadPathEnum, String path, byte[] content, String bucketName) {
        MinioApi minioApi = SpringUtil.getBean(MinioApi.class);

        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (uploadPathEnum != null) {
            path = uploadPathEnum.getPath() + FileUtils.generatePath(content, name);
        }
        if (StrUtil.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type, bucketName);

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setBucketName(bucketName);
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(SystemConfigKeyEnums.FILE_SERVER_DOMAIN.getKey());
        List<SystemConfigRespDTO> configsByCKeys = systemConfigApi.getConfigsByCKeys(keyList);
        String prefix = configsByCKeys.get(0).getCValue();
        String resultPath = prefix + path;
        //如果是mino的文件
        if(FileConfigEnum.CONTRACT_SIGNING.getId().equals(file.getConfigId())){
            // 如果minio的桶是private的，则返回临时访问地址
            resultPath = url;
        }
        file.setUrl(resultPath);
        file.setType(type);
        file.setSize(content.length);
        file.setBucketName(bucketName);
        fileMapper.insert(file);

        Map<String, Object> result = new HashMap();
        if(FileConfigEnum.CONTRACT_SIGNING.getId().equals(file.getConfigId())){
            // 如果minio的桶是private的，则返回临时访问地址
            resultPath =  minioApi.generatePresignedUrl(bucketName, file.getPath());
            // 如果minio的桶是public的，则直接返回文件地址
//          resultPath = fileDO.getUrl();
        }
        result.put("url", resultPath);
        result.put("fileId", file.getId());
        return result;
    }
    /**
     * 根据用户id查到相关文件（仅查转换后的pdf文件）
     */
    @Override
    public List<FileDTO> getContractFileList(List<Long> userIds) {
        List<FileFastDO> fileDOList = fileFastMapper.selectList(new LambdaQueryWrapperX<FileFastDO>().in(FileFastDO::getCreator, userIds).isNotNull(FileFastDO::getPageCount));
        return FileConvert.INSTANCE.convertListFast(fileDOList);
    }

    @Override
    public Boolean updateFileByWps(FileWpsDTO fileWpsDTO) {
        FileDO fileDO = FileConvert.INSTANCE.wps2FileDO(fileWpsDTO);
        return 1 == fileMapper.updateById(fileDO);

    }

    @Override
    public FileRespVO getFileInfoByFileId(Long fileId) {
        FileDO fileDO = fileMapper.selectById(fileId);
        Assert.notNull(fileDO, "数据检测为空");
        FileRespVO fileRespVO = FileConvert.INSTANCE.convert(fileDO);
        String path = fileRespVO.getPath();
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(SystemConfigKeyEnums.FILE_SERVER_DOMAIN.getKey());
        List<SystemConfigRespDTO> configsByCKeys = systemConfigApi.getConfigsByCKeys(keyList);
        if (configsByCKeys.size() > 0) {
            String prefix = configsByCKeys.get(0).getCValue();
            String result = prefix + path;
            fileRespVO.setUrl(result);
            if(FileConfigEnum.CONTRACT_SIGNING.getId().equals(fileDO.getConfigId())){
                MinioApi minioApi = SpringUtil.getBean(MinioApi.class);
                // 如果minio的桶是private的，则返回临时访问地址
                String bucketName = fileDO.getBucketName();
                if (StringUtils.isEmpty(bucketName)){
                    bucketName = minioApi.getBucketNameByTenantId();
                }
                fileRespVO.setUrl(minioApi.generatePresignedUrl(bucketName, fileDO.getPath()));
                // 如果minio的桶是public的，则直接返回文件地址
//            return fileDO.getUrl();
            }
            return fileRespVO;
        }
        return FileConvert.INSTANCE.convert(fileDO);
    }

    @Override
    @SneakyThrows
    public Long uploadFile(String name, String path, byte[] content) {
        MinioApi minioApi = SpringUtil.getBean(MinioApi.class);
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StringUtils.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StringUtils.isEmpty(name)) {
            name = path;
        }
        if (path.indexOf("\\") > -1) {
            path = path.replaceAll("\\\\", "/");
        }
        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type, minioApi.getBucketNameByTenantId());

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setBucketName(minioApi.getBucketNameByTenantId());
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);

        fileMapper.insert(file);
        return file.getId();
    }
    @Override
    @SneakyThrows
    public Long uploadFile(String name, String path, byte[] content, String bucketName) {
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StringUtils.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StringUtils.isEmpty(name)) {
            name = path;
        }
        if (path.indexOf("\\") > -1) {
            path = path.replaceAll("\\\\", "/");
        }
        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type, bucketName);

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setBucketName(bucketName);
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);

        fileMapper.insert(file);
        return file.getId();
    }

    @Override
    @SneakyThrows
    public FileDTO uploadFileV2(String name, String path, byte[] content) {
        FileDTO result = new FileDTO();

        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StringUtils.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StringUtils.isEmpty(name)) {
            name = path;
        }
        if (path.indexOf("\\") > -1) {
            path = path.replaceAll("\\\\", "/");
        }
        MinioApi minioApi = SpringUtil.getBean(MinioApi.class);
        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type, minioApi.getBucketNameByTenantId());

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);

        file.setBucketName(minioApi.getBucketNameByTenantId());
        fileMapper.insert(file);

        result.setId(file.getId());
        result.setName(name);
        result.setPath(path);
        result.setUrl(url);
        result.setType(type);
        result.setSize(content.length);
        result.setConfigId(client.getId());

        return result;
    }

    @Override
    public void deleteFile(Long id) throws Exception {
        // 校验存在
        FileDO file = validateFileExists(id);

        // 从文件存储器中删除
        FileClient client = fileConfigService.getFileClient(file.getConfigId());
        Assert.notNull(client, "客户端({}) 不能为空", file.getConfigId());
        client.delete(file.getPath());

        // 删除记录
        fileMapper.deleteById(id);
    }

    private FileDO validateFileExists(Long id) {
        FileDO fileDO = fileMapper.selectById(id);
        if (fileDO == null) {
            throw exception(FILE_NOT_EXISTS);
        }
        return fileDO;
    }

    @Override
    public byte[] getFileContent(Long configId, String path) throws Exception {
        FileClient client = fileConfigService.getFileClient(configId);
        Assert.notNull(client, "客户端({}) 不能为空", configId);
        MinioApi minioApi = SpringUtil.getBean(MinioApi.class);
        return client.getContentWithBucket(path, minioApi.getBucketNameByTenantId());
    }

    @Override
    public byte[] getFileContent(Long configId, String path, String bucketName) throws Exception {
        FileClient client = fileConfigService.getFileClient(configId);
        Assert.notNull(client, "客户端({}) 不能为空", configId);
        MinioApi minioApi = SpringUtil.getBean(MinioApi.class);
        return client.getContentWithBucket(path, bucketName);
    }

    @Override
    public void deleteFile(Long configId, String path) throws Exception {
        FileClient client = fileConfigService.getFileClient(configId);
        Assert.notNull(client, "客户端({}) 不能为空", configId);
        client.delete(path);
    }

    @Override
    public String getPath(Long configId, Long fileId) throws Exception {
        FileDO fileDo = fileMapper.selectById(fileId);
        return fileDo.getPath();
    }

    @Override
    public String getURL(Long configId, Long fileId) throws Exception {
        FileDO fileDo = fileMapper.selectById(fileId);
        if (fileDo == null) {
            fileDo = new FileDO().setUrl("");
        }
        return fileDo.getUrl();
    }

    @Override
    public byte[] getFileContentById(long l, Long fileId) throws Exception {
        String path = getPath(l, fileId);
        return getFileContent(l, path);
    }

    @Override
    public String getName(long l, Long fileId) {
        FileDO fileDo = fileMapper.selectById(fileId);
        if (fileDo == null) {
            return null;
        }
        return fileDo.getName();
    }

    @Override
    public int getPageCount(long l, Long fileId) {
        FileDO fileDo = fileMapper.selectById(fileId);
        //为空，则返回22
        if (fileDo == null) {
            return 0;
        }
        return fileDo.getPageCount();
    }

    @Override
    public List<FileDTO> selectBatchIds(List<Long> pathIds) {
        if(CollectionUtil.isEmpty(pathIds)) {
            return null;
        }
        List<FileDO> fileDOS = fileMapper.selectBatchIds(pathIds);
        return FileConvert.INSTANCE.getDTO(fileDOS);
    }

    @Override
    public TwoTuple<Long, String> getFileTargetInfo(Long fileId) {

        TwoTuple<Long, String> result = new TwoTuple<>();
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");

        result.setFirst(client.getId());
        FileDO fileDo = fileMapper.selectById(fileId);
        result.setSecond(fileDo.getPath());

        return result;
    }

    @Override
    @SneakyThrows
    public Long uploadFile(String name, String path, byte[] content, int pageCount) {
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        if (StringUtils.isEmpty(path)) {
            path = FileUtils.generatePath(content, name);
        }
        // 如果 name 为空，则使用 path 填充
        if (StringUtils.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        String url = client.upload(content, path, type);

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        file.setUrl(url);
        file.setType(type);
        file.setSize(content.length);
        file.setPageCount(pageCount);

        MinioApi minioApi = SpringUtil.getBean(MinioApi.class);
        file.setBucketName(minioApi.getBucketNameByTenantId());
        fileMapper.insert(file);
        return file.getId();
    }

    /**
     * 畅写
     */
    @Override
    public Boolean updateCXFile(Long fileId, String fileName, String cxUrl, byte[] bytes, FileDTO fileDTO ) {
        Map<String, Object> map = new HashMap<>();
        if (ObjectUtil.isEmpty(fileDTO.getBucketName())){
            map = createFileV3(fileName, FileUploadPathEnum.CX_SAVE_PATH, cxUrl, bytes);
        } else {
            map = createFileCX(fileName, FileUploadPathEnum.CX_SAVE_PATH, cxUrl, bytes, fileDTO.getBucketName());
        }
        Long newFileId = Long.valueOf(String.valueOf(map.get("fileId")));
        FileDO uploadFile = fileMapper.selectById(newFileId);
        if (ObjectUtil.isNull(uploadFile)) {
            return false;
        }
        uploadFile.setId(fileId);
        return 1 == fileMapper.updateById(uploadFile);
    }

    @Override
    public Long createFileInfo(FileDTO fileDTO) {
        FileDO fileDO = FileConvert.INSTANCE.toDO(fileDTO);
        MinioApi minioApi = SpringUtil.getBean(MinioApi.class);
        fileDO.setBucketName(minioApi.getBucketNameByTenantId());
        fileMapper.insert(fileDO);
        return fileDO.getId();
    }


    @Override
    public String getFileUrlV2(Long fileId) {

        FileDO fileDO = fileMapper.selectById(fileId);
//        Assert.notNull(fileDO, "数据检测为空");
        if (ObjectUtil.isEmpty(fileDO)) {
            throw exception(FILE_NOT_EXISTS);
        }
        String path = fileDO.getPath();
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(SystemConfigKeyEnums.FILE_SERVER_DOMAIN.getKey());
        List<SystemConfigRespDTO> configsByCKeys = systemConfigApi.getConfigsByCKeys(keyList);
        String prefix = configsByCKeys.get(0).getCValue();
        String result = prefix + path;
        return result;
    }

    /**
     * @param vo
     * @return {@link FileRespVO }
     */
    @Override
    public FileRespVO copyFile(FileCopyReqVO vo) {
        try {
            Long fileId = vo.getFileId();
            FileDO fileDO = fileMapper.selectById(fileId);
            if (ObjectUtil.isNull(fileDO)) {
                log.error("编号 " + fileId + " 文件不存在");
                throw exception(DIY_ERROR, "请检查文件编号是否正确。");
            }
            byte[] fileContentById = getFileContentById(18L, fileId);
            FileUploadPathEnum uploadPathEnum = FileUploadPathEnum.getInstance(vo.getPathCode());
            if (ObjectUtil.isNull(uploadPathEnum)) {
                uploadPathEnum = FileUploadPathEnum.FILE_COPY_PATH;
            }
            String path = DateUtil.today() + "/" + IdUtil.fastSimpleUUID() + "/" + fileDO.getName();
            Long newFileId = uploadFile(fileDO.getName(), uploadPathEnum.getPath() + path, fileContentById);
            FileDO result = fileMapper.selectById(newFileId);
            MinioApi minioApi = SpringUtil.getBean(MinioApi.class);
            result.setUrl(minioApi.generatePresignedUrl(fileDO.getBucketName(), fileDO.getPath()));
            return FileConvert.INSTANCE.convert(result);
        } catch (Exception e) {
            throw exception(DIY_ERROR, "文件拷贝失败，请稍后重试或联系技术支持。");
        }

    }

    @Override
    public FileUploadRespDTO uploadFileVP(String name, FileUploadPathEnum uploadPathEnum, byte[] content) {
        String path = "";
        // 计算默认的 path 名
        String type = FileTypeUtils.getMineType(content, name);
        name = enhanceName(type,name);
        if (uploadPathEnum != null) {
            path = uploadPathEnum.getPath() + getTimeFolderPath()+ IdUtil.fastSimpleUUID()+ name ;
        }
        // 如果 name 为空，则使用 path 填充
        if (StrUtil.isEmpty(name)) {
            name = path;
        }

        // 上传到文件存储器
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");

        // 保存到数据库
        FileDO file = new FileDO();
        file.setConfigId(client.getId());
        file.setName(name);
        file.setPath(path);
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(SystemConfigKeyEnums.FILE_SERVER_DOMAIN.getKey());
        List<SystemConfigRespDTO> configsByCKeys = systemConfigApi.getConfigsByCKeys(keyList);
        String prefix = configsByCKeys.get(0).getCValue();
        String resultPath = prefix + path;
        file.setUrl(resultPath);
        file.setType(type);
        file.setSize(content.length);

        MinioApi minioApi = SpringUtil.getBean(MinioApi.class);
        file.setBucketName(minioApi.getBucketNameByTenantId());
        fileMapper.insert(file);

        FileUploadRespDTO result = new FileUploadRespDTO();
        result.setFileId(file.getId());
        result.setFileUrl(resultPath);
        return result;
    }

    @Override
    public FilesDTO getfileInfoByMd5(String md5) {
        List<FileDO> fileDOs = fileMapper.selectList(new LambdaQueryWrapperX<FileDO>().eq(FileDO::getFileMd5, md5).orderByDesc(FileDO::getId));
        FileDO fileDO = null;
        if(CollectionUtil.isNotEmpty(fileDOs)){
            fileDO = fileDOs.get(0);
        }
        return FileConvert.INSTANCE.toFilesDTO(fileDO);
    }

    @Override
    public Long insertFileInfo(FilesDTO files) {
        FileDO  file = FileConvert.INSTANCE.toFiles2DO(files.setFileMd5(null));
        MinioApi minioApi = SpringUtil.getBean(MinioApi.class);
        file.setBucketName(minioApi.getBucketNameByTenantId());
        int insert = fileMapper.insert(file);
        return file.getId();
    }



    private String enhanceName(String type, String name) {
        if(type.contains("html")){
            name = name + ".html";
        }
        if(type.contains("doc")){
            name = name + ".doc";
        }
        if(type.contains("docx")){
            name = name + ".docx";
        }
        if(type.contains("pdf")){
            name = name + ".pdf";
        }
        return name;
    }

    private static String getTimeFolderPath() {
        LocalDateTime date = LocalDateTime.now();
        Integer year = date.getYear();
        Integer month = date.getMonthValue();
        Integer day = date.getDayOfMonth();

        return year + "/" + month + "/" + day + "/";
    }
@Resource
private FileConfigMapper fileConfigMapper;
    @Override
    @SneakyThrows
    public String getFileUrlByPath(String savePath) {
        FileClient client = fileConfigService.getMasterFileClient();
        Assert.notNull(client, "客户端(master) 不能为空");
        FileConfigDO fileConfig = fileConfigMapper.selectById(client.getId());
        if(ObjectUtil.isNull(fileConfig)){
            log.error("文件配置异常");
            throw exception(DATA_ERROR);
        }
        LocalFileClientConfig config = (LocalFileClientConfig)fileConfig.getConfig() ;
        return config.getGateway()+"/"+ savePath;
    }
}

package com.yaoan.framework.file.core.client.local;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.file.core.client.AbstractFileClient;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 本地文件客户端
 *
 * @author 芋道源码
 */
@Slf4j
public class LocalFileClient extends AbstractFileClient<LocalFileClientConfig> {

    public LocalFileClient(Long id, LocalFileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {
        // 补全风格。例如说 Linux 是 /，Windows 是 \
        if (!config.getBasePath().endsWith(File.separator)) {
            config.setBasePath(config.getBasePath() + File.separator);
        }
    }

    @Override
    public String upload(byte[] content, String path, String type) {
        log.info("upload参数："+",path:"+path+",type:"+type);
        // 执行写入
        String filePath = getFilePath(path);
        String replace = filePath.replace("\\", "/");
        FileUtil.writeBytes(content, replace);
        // 拼接返回路径
        return super.formatFileUrl(config.getDomain(), path);
    }

    @Override
    public String upload(byte[] content, String path, String type, String bucketName) throws Exception {
        log.info("upload参数："+",path:"+path+",type:"+type);
        // 执行写入
        String filePath = getFilePath(path);
        String replace = filePath.replace("\\", "/");
        FileUtil.writeBytes(content, replace);
        // 拼接返回路径
        return super.formatFileUrl(config.getDomain(), path);
    }

    @Override
    public void delete(String path) {
        String filePath = getFilePath(path);
        FileUtil.del(filePath);
    }

    @Override
    public byte[] getContent(String path) {
        String filePath = getFilePath(path);
        return FileUtil.readBytes(filePath);
    }

    @Override
    public byte[] getContentWithBucket(String path, String bucketName) throws Exception {
        String filePath = getFilePath(path);
        return FileUtil.readBytes(filePath);
    }

    private String getFilePath(String path) {
        return config.getBasePath() + path;
    }

}

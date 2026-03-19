package com.yaoan.module.econtract.api.minio;

import com.yaoan.module.econtract.util.MinioUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class MinioApiImpl implements MinioApi{

    @Resource
    private MinioUtils minioUtils;
    @Override
    public String generatePresignedUrl(String bucketName, String objectName) {
        try {
            return minioUtils.generatePresignedUrl(bucketName, objectName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getBucketNameByTenantId() {
        return minioUtils.getBucketNameByTenantId();
    }
}

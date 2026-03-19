package com.yaoan.module.econtract.api.minio;

import com.yaoan.module.econtract.api.model.dto.ModelDTO;

import java.util.List;

public interface MinioApi {
    // 为system模块提供一个批量插入模板的接口
    String generatePresignedUrl(String bucketName, String objectName);

    public String getBucketNameByTenantId();
}

package com.yaoan.module.econtract.api.changxie.dto.watermark;

import lombok.Data;

/**
 * @description: 水印响应
 * @author: Pele
 * @date: 2024/4/25 19:11
 */
@Data
public class CXWaterMarkRespDTO {

    private String key;
    private String resultUrl;
    private Boolean end;


}

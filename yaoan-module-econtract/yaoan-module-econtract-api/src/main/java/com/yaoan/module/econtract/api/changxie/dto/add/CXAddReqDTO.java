package com.yaoan.module.econtract.api.changxie.dto.add;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/25 17:53
 */
@Data
public class CXAddReqDTO {
    /**
     * 文档路径，必填
     */
    private String fileUrl;
    /**
     * json内容,可同时插入多个，必填
     */
    private String jsonArr;
}

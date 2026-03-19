package com.yaoan.module.econtract.api.changxie.dto.getcontent;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/28 15:47
 */
@Data
public class GetDocContentReqDTO {
    /**
     * 待处理的文档路径必填
     */
    private String fileUrl;

    /**
     * 回调Url，选填项:
     *
     * 1、填写后，异步生成包含数据的txt文地址以post请求方式发送到该接口,参数为包含txt文件地址的对象
     * 异步成功（只代表提交任务成功，最终是否生成文件以回调为准）：
     * { code: 1, msg:'xxxxxxxxxxxxxxx' }
     *
     * 2、不填写，等待txt文件同步生成后，返回txt文件路径，文件过大可能超时
     * 失败：
     * { code: 0, msg:'xxxxxxxxxxxxxxx'}
     */
    private String callbackUrl;
}

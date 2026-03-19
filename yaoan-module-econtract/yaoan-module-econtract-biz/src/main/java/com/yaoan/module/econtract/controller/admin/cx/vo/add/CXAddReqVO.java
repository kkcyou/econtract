package com.yaoan.module.econtract.controller.admin.cx.vo.add;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/25 17:48
 */
@Data
public class CXAddReqVO {
    /**
     * 文档路径，必填
     */
    @NotNull(message = "文档路径不可为空")
    private String fileUrl;
    /**
     * json内容,可同时插入多个，必填
     */
    @NotNull(message = "json内容不可为空")
    private List<CXAddJsonDTO> jsonArr;
}

package com.yaoan.module.econtract.api.changxie.dto.insertcontent;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/28 16:07
 */
@Data
public class InsertDocContentRespDTO {
    /**
     * 1为成功，0为失败
     */
    private Integer code;
    /**
     *
     */
    private Boolean end;
    /**
     * 返回值为空表示失败
     */
    private String key;
    /**
     * 返回值为空表示失败
     */
    private List<ResultDocRspDTO> urls;

    /**
     * code为0时存在此项
     */
    private String msg;
}

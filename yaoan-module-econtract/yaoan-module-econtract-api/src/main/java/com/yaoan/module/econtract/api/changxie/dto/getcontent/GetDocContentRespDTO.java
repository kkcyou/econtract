package com.yaoan.module.econtract.api.changxie.dto.getcontent;

import lombok.Data;

import java.util.List;

/**
 * @description: 响应VO
 * 同步成功：
 * {
 * "code": 1,
 * "end": true,
 * "key": "12259ce0zp.tppnmo_5605",
 * "urls": {
 * "result.txt": "xxxxxxxxxxxxxxx"
 * }
 * }
 * @author: Pele
 * @date: 2024/4/28 15:36
 */
@Data
public class GetDocContentRespDTO {
    /**
     *
     */
    private Integer code;
    /**
     *
     */
    private Boolean end;
    /**
     *
     */
    private String key;
    /**
     *
     */
    private List<ResultTxtDTO> urls;

}

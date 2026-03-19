package com.yaoan.framework.security.config;

import lombok.Data;
import lombok.ToString;

/**
 * @author : doujl
 * @className : ClientInfo
 * @description :
 * @date : 2023年12月07日18:11:38
 */
@Data
@ToString
public class JwtInfo {

    private String userId;
    private String userName;
    /**
     *  1:监管用户 2:采购人 3:代理机构 4:供应商
     */
    private String userTypeNow;

    private String systemType;

}

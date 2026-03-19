package com.yaoan.module.system.controller.admin.oauth2.vo.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
* OAuth2 客户端 Base VO，提供给添加、修改、详细的子 VO 使用
* 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 * @author doujiale
 */
@Data
public class OAuth2JWTReqVO {

    private String iss;

    private String aud;

    private String sub;

    private String bizId;

    private String tenantId;

    private String region;

    private String action;

    private String page;

    private List<String> scope;

    private String jti;

    private Date iat;

    private Long exp;

    private String kid;
}

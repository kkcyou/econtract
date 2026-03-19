package com.yaoan.module.econtract.api.modelcategory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhc
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "客戶端模板分类关联表DTO对象", description = "客戶端模板分类关联表DTO对象")
public class ClientModelCategoryDTO implements Serializable {

    private static final long serialVersionUID = -2956556691539595359L;

    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * clientId
     */
    private String clientId;
    /**
     * 客户端主键id
     */
    private Long oauth2ClientId;
    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 0=没删除，1=已删除
     */
    private Boolean deleted;
}

package com.yaoan.module.econtract.api.modelcategory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Pele
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "模板分类DTO对象", description = "模板分类DTO对象")
public class ModelCategoryDTO extends PageParam implements Serializable {

    private static final long serialVersionUID = 5202579898085812006L;

    /**
     * 分类id
     */
    private Integer id;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类编码
     */
    private String code;


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
    /**
     * 公司id
     */
    private Long companyId;
}

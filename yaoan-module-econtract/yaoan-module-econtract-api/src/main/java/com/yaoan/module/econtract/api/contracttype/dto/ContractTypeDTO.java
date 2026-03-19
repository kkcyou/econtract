package com.yaoan.module.econtract.api.contracttype.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/8 16:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "合同类型DTO对象", description = "合同类型DTO对象")
public class ContractTypeDTO extends PageParam implements Serializable {

    private static final long serialVersionUID = 3543322423282204412L;

    /**
     * 合同类型ID
     */
    @NotBlank(message = "[合同类型ID]不能为空")
    @Size(max = 255, message = "编码长度不能超过255")
    @Length(max = 255, message = "编码长度不能超过255")
    @Schema(name = "id", title = "合同类型ID")
    private String id;

    /**
     * 合同类型名称
     */
    @Size(max = 255, message = "编码长度不能超过255")
    @Length(max = 255, message = "编码长度不能超过255")
    private String name;

    /**
     * 合同类型编号
     */
    @Size(max = 255, message = "编码长度不能超过255")
    @Length(max = 255, message = "编码长度不能超过255")
    private String code;

    /**
     * 合同类型分类
     */
    @Size(max = 255, message = "编码长度不能超过255")
    @Length(max = 255, message = "编码长度不能超过255")
    private String typeCategory;

    /**
     * 是否被调用（）
     */
    private Boolean used;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 0=没删除，1=已删除
     */
    private Boolean deleted;

    /**
     * 创建时间起始（查询范围）
     */
    private LocalDateTime startCreateTime;

    /**
     * 创建时间结束（查询范围）
     */
    private LocalDateTime endCreateTime;

    private String platId;
}

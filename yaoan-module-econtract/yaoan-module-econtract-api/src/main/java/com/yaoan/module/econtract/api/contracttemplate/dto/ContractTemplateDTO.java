package com.yaoan.module.econtract.api.contracttemplate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/9 16:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "合同范本DTO对象", description = "合同范本DTO对象")
public class ContractTemplateDTO extends PageParam implements Serializable {

    private static final long serialVersionUID = -621751173736196513L;

    /**
     * 范本主键ID
     */
    private String id;

    /**
     * 范本名称
     */
    private String name;

    /**
     * 范本编号
     */
    private String code;

    /**
     * 范本简介（最多500个字）
     */
    private String templateIntro;

    /**
     * 范本分类
     */
    private Integer templateCategoryId;

    /**
     * 范本来源(官方范本=1 或 标准范本=2))
     */
    private String templateSource;

    /**
     * 发布机构
     */
    private String publishOrgan;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 范本字数
     */
    private String wordCount;


    /**
     * 文件名称
     */
    private String fileName;

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
     * 发布时间起始（查询范围）
     */
    private LocalDateTime startPublishTime;

    /**
     * 发布时间结束（查询范围）
     */
    private LocalDateTime endPublishTime;

    /**
     * 本地文件地址
     */
    private String localFilePath;

    /**
     * 远端文件对应ID（上传后的文件）
     */
    private Long remoteFileId;

    /**
     * 需要上传的文件
     */
    private MultipartFile file;


}
